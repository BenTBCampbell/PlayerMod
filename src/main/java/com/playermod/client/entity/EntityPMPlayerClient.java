package com.playermod.client.entity;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mojang.authlib.GameProfile;

@SideOnly(Side.CLIENT)
public class EntityPMPlayerClient extends EntityOtherPlayerMP {

	public EntityPMPlayerClient(World worldIn, GameProfile profile) {
		super(worldIn, profile);

	}

}
