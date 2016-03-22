package com.playermod.item;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.playermod.common.PlayerMod;
import com.playermod.entity.EntityPather;
import com.playermod.entity.player.EntityPMPlayerServer;

public class ItemPatherWand extends Item {

	private EntityPather pather;
	private static BlockPos target;
	private Integer delay = 0;
	private Integer max_delay = 15;
	private String name;

	public ItemPatherWand(String name) {
		this.name = name;
		this.setUnlocalizedName(PlayerMod.MODID + "." + name);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	public boolean isReady() {
		return this.delay >= this.max_delay;
	}

	public void resetDelay() {
		this.delay = 0;
	}

	public String getName() {
		return this.name;
	}

	public void setPather(EntityPather pather) {
		this.pather = pather;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn,
			int itemSlot, boolean isSelected) {
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
		if (!this.isReady()) {
			this.delay++;
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn,
			World worldIn, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ) {

		pos = pos.offset(side);
		if (!playerIn.canPlayerEdit(pos, side, stack)) {

			return false;

		} else if (this.isReady()) {
			this.resetDelay();
			this.target = pos;

			if (this.pather != null) {
				// send pather to coordinates
				if (this.pather.getNavigator().tryMoveToXYZ(pos.getX(),
						pos.getY(), pos.getZ(), 0.4F)) {

					playerIn.addChatMessage(new ChatComponentText(
							"Setting destination to " + pos.getX() + ", "
									+ pos.getY() + ", " + pos.getZ()));

				} else {

					playerIn.addChatMessage(new ChatComponentText(
							"Can't reach."));
				}
			} else {
				// No pather set. Do something else.
				// List worldPlayers = playerIn.getEntityWorld().playerEntities;
				Iterator clientIter = Minecraft.getMinecraft().theWorld.playerEntities
						.iterator();
				Iterator serverIter = MinecraftServer.getServer()
						.worldServerForDimension(playerIn.dimension).playerEntities
						.iterator();
				Iterator scmIter = MinecraftServer.getServer()
						.getConfigurationManager().playerEntityList.iterator();

				EntityPMPlayerServer player = null;

				while (clientIter.hasNext()) {
					EntityPlayer entry = (EntityPlayer) clientIter.next();
					// System.out.println("Dead:" + entry.isDead);
					// entry.isDead = false;
					System.out.println("Player:" + entry);
				}

				while (serverIter.hasNext()) {
					EntityPlayer entry = (EntityPlayer) serverIter.next();
					System.out.println("Server Player:" + entry);
				}

				while (scmIter.hasNext()) {
					EntityPlayer entry = (EntityPlayer) scmIter.next();
					System.out.println("SCM Player:" + entry);
				}
				
				// Iterator netManagers =
				// MinecraftServer.getServer().getNetworkSystem()

			}

			return true;

		}
		return true;
	}
}
