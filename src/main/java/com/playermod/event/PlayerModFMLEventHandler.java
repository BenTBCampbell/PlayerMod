package com.playermod.event;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class PlayerModFMLEventHandler {

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onEvent(PlayerLoggedOutEvent event) {
		System.out.println("Log out event:" + event.player.getName());
		// if (event.player instanceof EntityPlayerMP) {
		// if (event.player instanceof EntityPMPlayerServer) {
		// System.out.println("fake player");
		// return;
		// }
		// Iterator players =
		// MinecraftServer.getServer().getConfigurationManager().playerEntityList.iterator();
		//
		// // other real player found
		// Boolean flag = false;
		//
		// System.out.println("start check");
		//
		// while (players.hasNext() && !flag) {
		// EntityPlayerMP player = (EntityPlayerMP) players.next();
		// // System.out.println(player == event.player);
		// flag = (player instanceof EntityPMPlayerServer || player ==
		// event.player) ? false : true;
		// System.out.println(player.getName() + " flag:" + flag);
		// }
		//
		// // flag = true;
		//
		// if (flag) {
		// System.out.println("real player");
		// return;
		// } else {
		// System.out.println("no real players");
		// List<EntityPlayerMP> playerList =
		// MinecraftServer.getServer().getConfigurationManager().playerEntityList;
		//
		// for (int i = 0; i < playerList.size(); i++) {
		// EntityPlayerMP player = playerList.get(i);
		// System.out.println(player.getName() + ":");
		// if (player == event.player) {
		// System.out.println("real player (logging out)");
		// continue;
		// }
		// System.out.println("log out " + player.getName());
		// //
		// player.playerNetServerHandler.kickPlayerFromServer("Logged out computer player.");
		// }
		// System.out.println("Log out event done:" + event.player.getName());
		// }
		// }
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void playerTick(PlayerTickEvent event) {
		// System.out.println("Tick:" + event.player);

		// check if not client
		// if (event.phase == Phase.END || !(event.player instanceof
		// EntityOtherPlayerMP)) {
		// return;
		// }
		// System.out.println(event.player);
		// EntityPlayer serverPlayer =
		// MinecraftServer.getServer().worldServerForDimension(event.player.dimension)
		// .getPlayerEntityByName(event.player.getName());

		// System.out.println(event.player);

		// check if fake player
		// if (!(serverPlayer.playerNetServerHandler instanceof
		// NetHandlerPMPlayServer)) {
		// return;
		// }

		// EntityOtherPlayerMP player = (EntityOtherPlayerMP) event.player;
		// System.out.println("Tick:" + serverPlayer);

		// ((NetHandlerPMPlayServer)
		// serverPlayer.playerNetServerHandler).updateNetHandler();

		// System.out.println(serverPlayer.playerNetServerHandler.netManager.getDirection());
		// System.out.println(serverPlayer.playerNetServerHandler.netManager.getNetHandler());

		// if (!(player.playerNetServerHandler instanceof
		// NetHandlerPMPlayServer)) {
		// return;
		// }
		// System.out.println("Tick:" + player.getName());
		// player.playerNetServerHandler.update();
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void serverTick(ServerTickEvent event) {
		if (event.phase == Phase.END) {
			return;
		}
		MinecraftServer server = MinecraftServer.getServer();
	}
}
