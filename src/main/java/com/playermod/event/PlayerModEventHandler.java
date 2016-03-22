package com.playermod.event;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.google.common.base.Predicate;
import com.playermod.entity.EntityPather;
import com.playermod.entity.player.EntityPMPlayerServer;
import com.playermod.entity.player.EntityPlayerMob;
import com.playermod.network.NetHandlerPMPlayServer;

public class PlayerModEventHandler {

	public class AISpiderTargetPlayerMob extends
			EntityAINearestAttackableTarget {

		public AISpiderTargetPlayerMob(EntitySpider spider) {
			super(spider, EntityPlayerMob.class, true);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute() {
			float f = this.taskOwner.getBrightness(1.0F);
			return f >= 0.5F ? false : super.shouldExecute();
		}
	}

	class AISpiderAttack extends EntityAIAttackOnCollide {
		private static final String __OBFID = "CL_00002197";

		public AISpiderAttack(EntitySpider spider) {
			super(spider, EntityPlayerMob.class, 1.0D, true);
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean continueExecuting() {
			float f = this.attacker.getBrightness(1.0F);

			if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
				this.attacker.setAttackTarget((EntityLivingBase) null);
				return false;
			} else {
				return super.continueExecuting();
			}
		}

		@Override
		protected double func_179512_a(EntityLivingBase p_179512_1_) {
			return 4.0F + p_179512_1_.width;
		}
	}

	class PlayerMobGuardianTargetSelector implements Predicate {
		private EntityGuardian guardian;

		PlayerMobGuardianTargetSelector(EntityGuardian guardian) {
			this.guardian = guardian;
		}

		public boolean func_179915_a(EntityLivingBase p_179915_1_) {
			return (p_179915_1_ instanceof EntityPlayerMob)
					&& p_179915_1_.getDistanceSqToEntity(this.guardian) > 9.0D;
		}

		@Override
		public boolean apply(Object p_apply_1_) {
			return this.func_179915_a((EntityLivingBase) p_apply_1_);
		}
	}

	class AIFindPlayerMob implements Predicate {
		private EntityEnderman enderman;

		private boolean shouldAttackPlayerMob(EntityPlayerMob player) {

			Vec3 vec3 = player.getLook(1.0F).normalize();
			Vec3 vec31 = new Vec3(enderman.posX - player.posX,
					enderman.getEntityBoundingBox().minY
							+ enderman.height / 2.0F
							- (player.posY + player.getEyeHeight()),
					enderman.posZ - player.posZ);
			double d0 = vec31.lengthVector();
			vec31 = vec31.normalize();
			double d1 = vec3.dotProduct(vec31);
			return d1 > 1.0D - 0.15D / d0 ? player.canEntityBeSeen(enderman)
					: false;
		}

		AIFindPlayerMob(EntityEnderman enderman) {
			this.enderman = enderman;
		}

		public boolean findPlayer(EntityLivingBase player) {

			if (player instanceof EntityPlayerMob
					&& (shouldAttackPlayerMob((EntityPlayerMob) player))) {
				if (!enderman.isScreaming()) {
					enderman.setScreaming(true);
					enderman.playSound("mob.endermen.stare", 1.0F, 1.0F);
				}
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean apply(Object p_apply_1_) {
			return this.findPlayer((EntityLivingBase) p_apply_1_);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onEvent(EntityJoinWorldEvent event) {
		// Register extended entity properties

		if (event.entity instanceof EntityMob
				&& !(event.entity instanceof EntityPlayerMob)) {

			EntityMob entity = (EntityMob) event.entity;
			if (!(entity instanceof EntitySilverfish))
				entity.tasks.addTask(8, new EntityAIWatchClosest(entity,
						EntityPlayerMob.class, 8.0F));

			if (!(entity instanceof EntityWither
					|| entity instanceof EntityEnderman
					|| entity instanceof EntityGuardian || entity instanceof EntitySpider))
				entity.targetTasks.addTask(2,
						new EntityAINearestAttackableTarget(entity,
								EntityPlayerMob.class, true));

			if (entity instanceof EntitySpider) {
				entity.targetTasks.addTask(2,
						new PlayerModEventHandler.AISpiderTargetPlayerMob(
								(EntitySpider) entity));
				entity.tasks.addTask(4,
						new PlayerModEventHandler.AISpiderAttack(
								(EntitySpider) entity));
			}
			if (entity instanceof EntityGuardian) {
				entity.targetTasks.addTask(1,
						new EntityAINearestAttackableTarget(entity,
								EntityLivingBase.class, 10, true, false,
								new PlayerMobGuardianTargetSelector(
										(EntityGuardian) entity)));
			}
			if (entity instanceof EntityEnderman) {
				entity.targetTasks.addTask(2,
						new EntityAINearestAttackableTarget(entity,
								EntityPlayerMob.class, 10, true, false,
								new AIFindPlayerMob((EntityEnderman) entity)));
			}

			if (entity instanceof EntityZombie
					|| entity instanceof EntityEndermite
					|| entity instanceof EntitySilverfish)
				entity.tasks.addTask(2, new EntityAIAttackOnCollide(entity,
						EntityPlayerMob.class, 1.0D, false));

		}

		if (event.entity instanceof EntityPMPlayerServer) {
			event.world.playerEntities.remove(event.entity);
		}

		if (event.entity instanceof EntityPather) {
			NBTTagCompound tag = new NBTTagCompound();
			event.entity.writeToNBT(tag);
			tag.setBoolean("Invulnerable", true);
			event.entity.readFromNBT(tag);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onEvent(LivingDeathEvent event) {

		if (event.entity instanceof EntityPlayerMP && false) {
			System.out.println("Player Died");
			EntityPlayerMP player = (EntityPlayerMP) event.entity;
			if (!(player.playerNetServerHandler instanceof NetHandlerPMPlayServer)) {
				EntityPlayer clientEntity = Minecraft.getMinecraft().theWorld
						.getPlayerEntityByName(player.getName());
				clientEntity.respawnPlayer();
			}
			// MinecraftServer
			// .getServer()
			// .getConfigurationManager()
			// .recreatePlayerEntity((EntityPlayerMP) event.entity, 0,
			// true);
		}
	}

	// @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	// public void onEvent(PlayerLoggedOutEvent event) {
	// System.out.println("Log out");
	// // if (event.player instanceof EntityPlayerMP) {
	// // Iterator players =
	// //
	// MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();
	// // Boolean realPlayer = false;
	// // while (players.hasNext() && !realPlayer) {
	// // realPlayer = (players.next() instanceof EntityPMPlayerServer) ? false
	// // : true;
	// // }
	// //
	// // if (realPlayer) {
	// // return;
	// // } else {
	// // System.out.println("no real players");
	// // players =
	// //
	// MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();
	// //
	// // while (players.hasNext()) {
	// // EntityPlayerMP player = (EntityPlayerMP) players.next();
	// // System.out.println("log out " + player.getName());
	// //
	// player.playerNetServerHandler.kickPlayerFromServer("Logged out computer player.");
	// // }
	// // }
	// // }
	// }

	// @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	// public void onClientDisconnect(ClientDisconnectionFromServerEvent event)
	// {
	// System.out.println("Client disconnect");
	// }

	// @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	// public void onServerDisconnect(ServerDisconnectionFromClientEvent event)
	// {
	// System.out.println("Server disconnect");
	// }

}
