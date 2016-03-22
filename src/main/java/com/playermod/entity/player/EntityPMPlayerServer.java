package com.playermod.entity.player;


import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.world.WorldServer;

import com.mojang.authlib.GameProfile;
import com.playermod.network.NetHandlerPMPlayServer;

public class EntityPMPlayerServer extends EntityPlayerMP {

	/**
	 * The NetServerHandler assigned to this player by the
	 * ServerConfigurationManager.
	 */
	// public NetHandlerPMPlayServer playerNetServerHandler;

	public EntityPMPlayerServer(MinecraftServer server, WorldServer worldIn,
			GameProfile profile, ItemInWorldManager interactionManager) {

		super(server, worldIn, profile, interactionManager);
		System.out.println("New EntityPMPlayerServer");

	}

	@Override
	public void entityInit() {
		super.entityInit();
		if (!(this.worldObj.playerEntities.contains(this))) {
			this.worldObj.playerEntities.add(this);
		};
		// System.out.println(this.capabilities)
		// if (!(player.getServerForPlayer().playerEntities.contains(player))) {
		// player.getServerForPlayer().playerEntities.add(player);
		// // System.out.println("added " + player);
		// }
	}

	@Override
	public void onUpdate() {
		// if (this.getHealth() > 0.0F && !this.isSpectator()) {
		// AxisAlignedBB axisalignedbb = null;
		//
		// if (this.ridingEntity != null && !this.ridingEntity.isDead) {
		// axisalignedbb =
		// this.getEntityBoundingBox().union(this.ridingEntity.getEntityBoundingBox()).expand(1.0D,
		// 0.0D, 1.0D);
		// } else {
		// axisalignedbb = this.getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D);
		// }
		//
		// List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this,
		// axisalignedbb);
		//
		// for (int i = 0; i < list.size(); ++i) {
		// Entity entity = (Entity) list.get(i);
		//
		// if (!entity.isDead) {
		// // entity.collideWithPlayer(entity);
		// System.out.println(this.getName() + " collided with " + entity);
		// }
		// }
		// }
		// System.out.println(this.playerNetServerHandler);

		// this.clientUpdate();

		// super.onUpdate();
		// this.onUpdateEntity();
	}

	public void clientUpdate() {
		// System.out.println("update");
		if (this.worldObj.isBlockLoaded(new BlockPos(this.posX, 0.0D, this.posZ))) {
			// super.onUpdate();

			if (this.isRiding()) {
				this.playerNetServerHandler.processPlayer(new C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch,
						this.onGround));
				this.playerNetServerHandler.processInput(new C0CPacketInput(this.moveStrafing, this.moveForward, this.isJumping, this
						.isSneaking()));
			} else {
				this.playerNetServerHandler.processPlayer(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX,
						this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw, this.rotationPitch, this.onGround));
			}
		}
	}

	// public void onUpdateWalkingPlayer()
	// {
	// boolean flag = this.isSprinting();
	//
	// // if (flag != this.serverSprintState)
	// // {
	// // if (flag)
	// // {
	// // this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this,
	// C0BPacketEntityAction.Action.START_SPRINTING));
	// // }
	// // else
	// // {
	// // this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this,
	// C0BPacketEntityAction.Action.STOP_SPRINTING));
	// // }
	// //
	// // this.serverSprintState = flag;
	// // }
	//
	// boolean flag1 = this.isSneaking();
	//
	// // if (flag1 != this.serverSneakState)
	// // {
	// // if (flag1)
	// // {
	// // this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this,
	// C0BPacketEntityAction.Action.START_SNEAKING));
	// // }
	// // else
	// // {
	// // this.sendQueue.addToSendQueue(new C0BPacketEntityAction(this,
	// C0BPacketEntityAction.Action.STOP_SNEAKING));
	// // }
	// //
	// // this.serverSneakState = flag1;
	// // }
	//
	// // if (this.isCurrentViewEntity())
	// // {
	// double d0 = this.posX - this.lastReportedPosX;
	// double d1 = this.getEntityBoundingBox().minY - this.lastReportedPosY;
	// double d2 = this.posZ - this.lastReportedPosZ;
	// double d3 = (double)(this.rotationYaw - this.lastReportedYaw);
	// double d4 = (double)(this.rotationPitch - this.lastReportedPitch);
	// boolean flag2 = d0 * d0 + d1 * d1 + d2 * d2 > 9.0E-4D ||
	// this.positionUpdateTicks >= 20;
	// boolean flag3 = d3 != 0.0D || d4 != 0.0D;
	//
	// if (this.ridingEntity == null)
	// {
	// if (flag2 && flag3)
	// {
	// this.sendQueue.addToSendQueue(new
	// C03PacketPlayer.C06PacketPlayerPosLook(this.posX,
	// this.getEntityBoundingBox().minY, this.posZ, this.rotationYaw,
	// this.rotationPitch, this.onGround));
	// }
	// else if (flag2)
	// {
	// this.sendQueue.addToSendQueue(new
	// C03PacketPlayer.C04PacketPlayerPosition(this.posX,
	// this.getEntityBoundingBox().minY, this.posZ, this.onGround));
	// }
	// else if (flag3)
	// {
	// this.sendQueue.addToSendQueue(new
	// C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw, this.rotationPitch,
	// this.onGround));
	// }
	// else
	// {
	// this.sendQueue.addToSendQueue(new C03PacketPlayer(this.onGround));
	// }
	// }
	// else
	// {
	// this.sendQueue.addToSendQueue(new
	// C03PacketPlayer.C06PacketPlayerPosLook(this.motionX, -999.0D,
	// this.motionZ, this.rotationYaw, this.rotationPitch, this.onGround));
	// flag2 = false;
	// }
	//
	// ++this.positionUpdateTicks;
	//
	// if (flag2)
	// {
	// this.lastReportedPosX = this.posX;
	// this.lastReportedPosY = this.getEntityBoundingBox().minY;
	// this.lastReportedPosZ = this.posZ;
	// this.positionUpdateTicks = 0;
	// }
	//
	// if (flag3)
	// {
	// this.lastReportedYaw = this.rotationYaw;
	// this.lastReportedPitch = this.rotationPitch;
	// }
	// // }
	// }
	//
	@Override
	public void onUpdateEntity() {
		// System.out.println("UPDATE!");
		super.onUpdateEntity();
	}

	// @Override
	// public void collideWithPlayer(Entity entity){
	// super.col
	// }

	@Override
	public void onDeath(DamageSource cause) {

		System.out.println(this.playerNetServerHandler);
		super.onDeath(cause);
		if (this.playerNetServerHandler instanceof NetHandlerPMPlayServer) {
			((NetHandlerPMPlayServer) this.playerNetServerHandler).respawnPlayer();
		}

	}

}