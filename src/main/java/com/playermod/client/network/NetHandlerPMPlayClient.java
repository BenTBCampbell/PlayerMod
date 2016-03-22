package com.playermod.client.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.mojang.authlib.GameProfile;

@SideOnly(Side.CLIENT)
public class NetHandlerPMPlayClient implements INetHandler {

	private NetworkManager netManager;
	private GameProfile profile;

	public NetHandlerPMPlayClient(NetworkManager netManager, GameProfile profile) {
		this.netManager = netManager;
		this.profile = profile;
	}

	@Override
	public void onDisconnect(IChatComponent reason) {
		System.out.println("disconnect:" + reason);
	}

}
