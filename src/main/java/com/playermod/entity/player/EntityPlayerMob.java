package com.playermod.entity.player;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.playermod.common.Config;
import com.playermod.util.PlayerModUtil;

public class EntityPlayerMob extends EntityCreature implements INpc, IMob {

	/** The current experience level the player is on. */
	public int experienceLevel;
	private int experienceTotal;
	private float experience;
	private int lastXPSound;

	public EntityPlayerMob(World worldIn) {
		super(worldIn);
		this.experienceValue = 5;
		((PathNavigateGround) this.getNavigator()).setCanSwim(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAITempt(this, 1.0D,
				Items.diamond_sword, false));
		// this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
		// EntityMob.class, true));
		// this.tasks.addTask(2, new EntityAIAttackOnCollide(this,
		// EntityLiving.class, 1.0D, true));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks
				.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest2(this,
				EntityPlayer.class, 8.0F, 1.0F));
		this.tasks.addTask(7, new EntityAIWatchClosest2(this,
				EntityVillager.class, 8.0F, 1.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this,
				EntityLiving.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.applyEntityAI();
		this.setSize(0.6F, 1.8F);
	}

	protected void applyEntityAI() {
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this,
				EntityLiving.class, 1.0D, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
				EntityMob.class, false));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
				EntityChicken.class, false));
		// this.tasks.addTask(4, new EntityAIAttackOnCollide(this,
		// EntityPlayer.class, 1.0D, true));
		// this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this,
		// EntityPlayer.class, false));

	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(
				SharedMonsterAttributes.attackDamage);
		this.getEntityAttribute(SharedMonsterAttributes.followRange)
				.setBaseValue(50.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed)
				.setBaseValue(0.313D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage)
				.setBaseValue(1.0D);
	}

	protected void entityInit() {
		super.entityInit();

		// skin location
		this.dataWatcher.addObject(12, "");

		// has small arms
		this.getDataWatcher().addObject(13, Byte.valueOf((byte) 0));

		this.setCanPickUpLoot(false);
		this.setAlwaysRenderNameTag(true);
	}

	public boolean skin_exists(String skin) {
		ResourceLocation skin_location = new ResourceLocation("playermod",
				"textures/entity/playermob/" + skin + ".png");
		try {
			((SimpleReloadableResourceManager) Minecraft.getMinecraft()
					.getResourceManager()).getResource(skin_location);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public ResourceLocation get_skin_location() {
		return new ResourceLocation("playermod", "textures/entity/playermob/"
				+ this.get_skin() + ".png");
	}

	public String get_skin() {
		return this.getDataWatcher().getWatchableObjectString(12);
	}

	protected void set_skin(String username) {

		// get entry
		String[] entry = this.get_config_entry(username);
		if (entry == null) {
			// no entry. pick random one
			entry = this.get_config_entry_with_skin();
		}

		// check if skin for entry exists
		if (!this.skin_exists(entry[0])) {
			entry = get_config_entry_with_skin();
		}

		this.dataWatcher.updateObject(12, entry[0]);
		this.set_small_arms(entry[1].contains("true"));
	}

	public boolean has_small_arms() {
		return this.getDataWatcher().getWatchableObjectByte(13) == 1;
	}

	public void set_small_arms(boolean small_arms) {
		this.getDataWatcher().updateObject(13,
				Byte.valueOf((byte) (small_arms ? 1 : 0)));
	}

	/**
	 * gets config entry as an array of it's contents. May return null
	 * 
	 * @param entry
	 *            Config entry
	 */
	private String[] get_config_entry(String username) {
		// search config for username
		for (int i = 0; i < Config.players.length; i++) {
			if (Config.players[i].contains(username)) {
				// entry found
				String entry = Config.players[i];
				String[] split_entry;
				int index = entry.indexOf(" ");

				if (index != -1) {
					// split the entry at the space
					split_entry = new String[] { entry.substring(0, index),
							entry.substring(index) };
					if (split_entry[1].contains("true")
							|| split_entry[1].contains("false"))
						// the 2nd part is valid. return
						return split_entry;
				}
				// no space or 2nd entry not valid. default arm size false
				return new String[] { entry, " false" };
			}
		}
		// no entry found
		return null;
	}

	private String[] get_config_entry_with_skin() {
		String name = Config.players_with_skins.get(this.rand
				.nextInt(Config.players_with_skins.size()));
		return this.get_config_entry(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		String skin = nbt.getString("Skin");

		if (!this.getCustomNameTag().isEmpty()
				&& this.getCustomNameTag() != nbt.getString("CustomName")) {

			if (this.skin_exists(nbt.getString("CustomName"))) {
				this.set_skin(nbt.getString("CustomName"));
				skin = nbt.getString("CustomName");
			}
		}

		super.readFromNBT(nbt);

		if (this.getCustomNameTag().isEmpty()) {

			String name = this.get_config_entry_with_skin()[0];
			this.setCustomNameTag(name);
			this.set_skin(name);

		} else if (this.get_skin() != skin) {

			this.set_skin(skin);

		}

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);

		nbt.setString("Skin", this.get_skin());
	}

	public void onLivingUpdate() {
		if (this.isRiding()) {
			((EntityLiving) this.ridingEntity).getNavigator().setPath(
					this.getNavigator().getPath(), 1.5D);
		}

		this.updateArmSwingProgress();
		float f = this.getBrightness(1.0F);

		if (f > 0.5F) {
			this.entityAge += 2;
		}

		super.onLivingUpdate();
	}

	public boolean attackEntityAsMob(Entity p_70652_1_) {
		float f = (float) this.getEntityAttribute(
				SharedMonsterAttributes.attackDamage).getAttributeValue();
		int i = 0;

		if (p_70652_1_ instanceof EntityLivingBase) {
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItem(),
					((EntityLivingBase) p_70652_1_).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
		}

		boolean flag = p_70652_1_.attackEntityFrom(
				DamageSource.causeMobDamage(this), f);

		if (flag) {
			if (i > 0) {
				p_70652_1_.addVelocity(
						(double) (-MathHelper.sin(this.rotationYaw
								* (float) Math.PI / 180.0F)
								* (float) i * 0.5F),
						0.1D,
						(double) (MathHelper.cos(this.rotationYaw
								* (float) Math.PI / 180.0F)
								* (float) i * 0.5F));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0) {
				p_70652_1_.setFire(j * 4);
			}

			this.applyEnchantments(this, p_70652_1_);
		}

		return flag;
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (this.isEntityInvulnerable(source)) {
			return false;
		} else if (super.attackEntityFrom(source, amount)) {
			Entity entity = source.getEntity();
			return this.riddenByEntity != entity && this.ridingEntity != entity ? true
					: true;
		} else {
			return false;
		}
	}

	@Override
	public void onKillEntity(EntityLivingBase killed_entity) {
		if (!killed_entity.worldObj.isRemote
				&& killed_entity.worldObj.getGameRules()
						.getBoolean("doMobLoot")) {
			int i = PlayerModUtil.getExperiencePoints(killed_entity);
			System.out.println(i);

			while (i > 0) {
				int j = EntityXPOrb.getXPSplit(i);
				System.out.println(j);
				i -= j;
				this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj,
						this.posX, this.posY, this.posZ, j));
			}
		}
	}

	@Override
	/**
	 * Get the experience points the entity currently has.
	 */
	protected int getExperiencePoints(EntityPlayer player) {
		if (this.worldObj.getGameRules().getBoolean("keepInventory")) {
			return 0;
		} else {
			int i = this.experienceLevel * 7;
			return i > 100 ? 100 : i;
		}
	}

	public void addExperienceLevel(int levels) {
		this.experienceLevel += levels;

		if (this.experienceLevel < 0) {
			this.experienceLevel = 0;
			this.experience = 0.0F;
			this.experienceTotal = 0;
		}

		if (levels > 0
				&& this.experienceLevel % 5 == 0
				&& (float) this.lastXPSound < (float) this.ticksExisted - 100.0F) {
			float f = this.experienceLevel > 30 ? 1.0F
					: (float) this.experienceLevel / 30.0F;
			this.worldObj.playSoundAtEntity(this, "random.levelup", f * 0.75F,
					1.0F);
			this.lastXPSound = this.ticksExisted;
		}
	}

	protected String getSwimSound() {
		return "game.player.swim";
	}

	protected String getSplashSound() {
		return "game.player.swim.splash";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "game.player.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "game.player.die";
	}

	protected String getFallSoundString(int damageValue) {
		return damageValue > 4 ? "game.player.hurt.fall.big"
				: "game.player.hurt.fall.small";
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return false;
	}

	@Override
	protected boolean isPlayer() {
		return true;
	}
}
