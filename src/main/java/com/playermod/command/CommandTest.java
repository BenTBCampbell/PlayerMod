package com.playermod.command;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkSystem;
import net.minecraft.server.MinecraftServer;

import com.playermod.entity.player.EntityPMPlayerServer;
import com.playermod.network.NetHandlerPMPlayServer;

public class CommandTest extends CommandBase {

	@Override
	public String getCommandName() {
		return "test";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/test";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {

		Iterator playersIter = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();
		EntityPlayerMP fakePlayer = null;

		while (playersIter.hasNext() && fakePlayer == null) {
			EntityPlayerMP player = (EntityPlayerMP) playersIter.next();
			if (player.playerNetServerHandler instanceof NetHandlerPMPlayServer) {
				fakePlayer = player;
			}
		}

		if (fakePlayer == null) {
			// throw new CommandException(String.format("No Fake Players"), new
			// Object[0]);
		}
		// System.out.println("Test command player:" + fakePlayer.getName());
		// ((NetHandlerPMPlayServer)
		// fakePlayer.playerNetServerHandler).updateNetHandler();
		EntityPlayerSP CPlayer = Minecraft.getMinecraft().thePlayer;
		EntityPlayerMP SPlayer = MinecraftServer.getServer().getConfigurationManager().getPlayerByUsername(CPlayer.getName());
		NetworkManager CManager = CPlayer.sendQueue.getNetworkManager();
		NetworkManager SManager = SPlayer.playerNetServerHandler.getNetworkManager();
		// System.out.println("Client:" + CPlayer.sendQueue);
		// System.out.println("Server:" + SPlayer.playerNetServerHandler);
		// System.out.println("Test:" + (SPlayer.playerNetServerHandler
		// instanceof NetHandlerPlayServer));

		Iterator clientIter = Minecraft.getMinecraft().theWorld.playerEntities.iterator();
		Iterator serverIter = MinecraftServer.getServer().worldServerForDimension(0).playerEntities.iterator();
		Iterator scmIter = MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();

		EntityPMPlayerServer player = null;

		// while (clientIter.hasNext()) {
		// EntityPlayer entry = (EntityPlayer) clientIter.next();
		// // System.out.println("Dead:" + entry.isDead);
		// // entry.isDead = false;
		// System.out.println("Player:" + entry);
		// if (entry instanceof EntityPlayerSP) {
		// System.out.println(((EntityPlayerSP) entry).sendQueue);
		// }
		// }
		//
		// while (serverIter.hasNext()) {
		// EntityPlayerMP entry = (EntityPlayerMP) serverIter.next();
		// System.out.println("Server Player:" + entry);
		// System.out.println(entry.playerNetServerHandler);
		// }

		while (scmIter.hasNext()) {
			EntityPlayer entry = (EntityPlayer) scmIter.next();
			System.out.println("SCM Player:" + entry);
		}

		// MinecraftServer.getServer().getNetworkSystem().addLocalEndpoint();

		Class<NetworkSystem> netSystemClass = NetworkSystem.class;
		Field netManagersField = null;
		try {
			netManagersField = netSystemClass.getDeclaredField("networkManagers");
			// netManagersField =
			// netSystemClass.getDeclaredField("networkManagers");
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		netManagersField.setAccessible(true);
		List networkManagers = null;
		try {
			networkManagers = (List) netManagersField.get(MinecraftServer.getServer().getNetworkSystem());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (networkManagers != null) {
			// networkManagers.add(new
			// NetworkManager(EnumPacketDirection.SERVERBOUND));
			// MinecraftServer.getServer().getNetworkSystem().addLocalEndpoint();
			for (int i = 0; i < networkManagers.size(); i++) {
				System.out.println(((NetworkManager) networkManagers.get(i)).channel().isOpen());
				((NetworkManager) networkManagers.get(i)).checkDisconnected();
			}
		}

		// System.out.println("Player Dir:" +
		// fakePlayer.playerNetServerHandler.getNetworkManager().getDirection());

		// System.out.println(PlayerSelector2.matchEntities(sender, args[0],
		// Entity.class));

	}

}
