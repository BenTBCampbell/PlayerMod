package com.playermod.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.authlib.GameProfile;

public class NetHandlerPMLoginServer implements INetHandler, ITickable, INetHandlerPM {

	private static final Logger logger = LogManager.getLogger();
	private final MinecraftServer server;
	public final NetworkManager networkManager;
	private GameProfile loginGameProfile;

	public NetHandlerPMLoginServer(MinecraftServer server, NetworkManager manager, GameProfile profile) {
		this.server = server;
		this.networkManager = manager;
		this.loginGameProfile = profile;
	}

	@Override
	public void onDisconnect(IChatComponent reason) {
		// TODO Auto-generated method stub
		logger.info("lost connection: " + reason.getUnformattedText());
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		System.out.println("Update login net handler");
	}

	public void acceptPlayer() {

	}

}
