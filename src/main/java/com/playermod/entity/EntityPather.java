package com.playermod.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.playermod.item.ItemPatherWand;
import com.playermod.pathfinding.PathNavigatePlayer;

public class EntityPather extends EntityLiving {

	private boolean has_gravity;
	private PathEntity previous_path;
	private PathPoint[] previous_points;

	public EntityPather(World worldIn) {
		super(worldIn);
		this.setSize(0.25F, 0.25F);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
	}

	@Override
	public void onLivingUpdate() {

		super.onLivingUpdate();

		if (!this.navigator.noPath()) {
			System.out.println(this.worldObj.isRemote);
			PathEntity path = this.navigator.getPath();
			if (path.getCurrentPathLength() > 0) {

				if (this.previous_points != null && this.previous_points.length > 0) {
					System.out.println("Hi");
					for (int i = 0; i < this.previous_points.length; i++) {

						PathPoint point = this.previous_points[i];
						BlockPos blockPos = new BlockPos(point.xCoord, point.yCoord, point.zCoord);
						this.worldObj.setBlockState(blockPos, Blocks.air.getDefaultState());

					}
				}

				if (this.previous_path != null && this.previous_path.getCurrentPathLength() > 0) {
					for (int i = 0; i < this.previous_path.getCurrentPathLength(); i++) {

						Vec3 vec = this.previous_path.getVectorFromIndex(this, i);
						BlockPos blockPos = new BlockPos(vec);
						this.worldObj.setBlockState(blockPos, Blocks.air.getDefaultState());

					}
				}

				PathPoint[] points = ((PathNavigatePlayer) this.navigator).getNodeProcessor().getAllPoints();

				for (int i = 0; i < points.length; i++) {

					PathPoint point = points[i];
					BlockPos blockPos = new BlockPos(point.xCoord, point.yCoord, point.zCoord);
					this.worldObj.setBlockState(blockPos, Blocks.glass.getDefaultState());

				}

				for (int i = 0; i < path.getCurrentPathLength(); i++) {

					Vec3 vec = path.getVectorFromIndex(this, i);
					BlockPos blockPos = new BlockPos(vec);
					this.worldObj.setBlockState(blockPos, Blocks.stained_glass.getStateFromMeta(14));

				}

				PathPoint end = path.getFinalPathPoint();
				this.setPosition(end.xCoord + 0.5, end.yCoord, end.zCoord + 0.5);
				this.previous_points = points;
				this.previous_path = path;
				this.navigator.clearPathEntity();

			}
		}

	}

	/**
	 * Returns new PathNavigateGround instance
	 */
	@Override
	protected PathNavigate getNewNavigator(World worldIn) {
		return new PathNavigatePlayer(this, worldIn);
	}

	@Override
	public boolean interact(EntityPlayer player) {
		ItemStack itemstack = player.inventory.getCurrentItem();
		if (itemstack != null && itemstack.getItem() instanceof ItemPatherWand && !this.worldObj.isRemote) {
			if (((ItemPatherWand) itemstack.getItem()).isReady()) {
				((ItemPatherWand) itemstack.getItem()).resetDelay();
				((ItemPatherWand) itemstack.getItem()).setPather(this);
				player.addChatMessage(new ChatComponentText("Pather Set."));
			}
		} else {
			return super.interact(player);
		}
		return true;
	}

}
