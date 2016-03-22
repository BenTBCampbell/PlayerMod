package com.playermod.entity.playermob;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.stats.StatBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;

import com.mojang.authlib.GameProfile;

public class EntityPlayerMob2 extends EntityPlayer {

	

	private static WorldServer OverworldServer = MinecraftServer.getServer()
			.worldServerForDimension(0);

	private static GameProfile profile = new GameProfile(UUID.randomUUID(),
			"Steve");
	
	public EntityPlayerMob2(World worldIn) {
		super(worldIn, profile);
		// TODO Auto-generated constructor stub
	}
//	@Override
//	playerNetServerHandler = NetworkDispatcher.getNetHandler();
//
//	public EntityPlayerMob2(World worldIn) {
//		super(FMLCommonHandler.instance().getMinecraftServerInstance(),
//				OverworldServer, profile, new ItemInWorldManager(worldIn));
//		playerNetServerHandler = NetworkDispatcher.getNetHandler();
//		// TODO Auto-generated constructor stub
//	}

	// public EntityPlayerMob2(World world) {
	// super(world, profile);

	// }

	@Override
	public boolean isSpectator() {
		return false;
	}

	private String username;
	private boolean hasSmallArms;
	private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation(
			"minecraft:textures/entity/alex.png");
	private static final ResourceLocation TEXTURE_STEVE = new ResourceLocation(
			"minecraft:textures/entity/steve.png");

	public ResourceLocation getLocationSkin() {
		return hasSmallArms ? TEXTURE_ALEX : TEXTURE_STEVE;
	}

	public boolean hasSmallArms() {
		return this.hasSmallArms;
	}

	public void setSmallArms(boolean b) {
		this.hasSmallArms = b;
	}

}
