package com.playermod.command;

import java.lang.reflect.Field;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetworkSystem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

import com.google.common.base.Charsets;
import com.mojang.authlib.GameProfile;
import com.playermod.network.NetHandlerPMPlayServer;

public class CommandLogin extends CommandBase {

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "login";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "login [username]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {

		System.out.println("Login");

		Boolean customClass = false;

		if (args.length < 0 || args.length > 1) {
			throw new WrongUsageException("/login [username]", new Object[0]);
		}

		if (!(sender instanceof EntityPlayer) && args.length != 1) {
			throw new CommandException("Must use /login <username> in command blocks", new Object[0]);
		}

		MinecraftServer server = MinecraftServer.getServer();
		ServerConfigurationManager scm = server.getConfigurationManager();
		NetworkSystem netSystem = server.getNetworkSystem();

		try {

			// construct profile and player
			String username;

			if (args.length == 1) {
				// username given. Check if player is already logged in.
				username = args[0];

				if (!(scm.getPlayerByUsername(args[0]) == null)) {
					throw new CommandException(String.format("%s is already logged in", username));
				}

			} else {
				// no username given. generate Player_(number)
				username = "Player_" + (scm.playerEntityList.size() + 1);

			}

			UUID uuid = UUID.nameUUIDFromBytes(username.getBytes(Charsets.UTF_8));
			GameProfile profile = new GameProfile(uuid, username);
			EntityPlayerMP fakePlayer = scm.createPlayerForUser(profile);

			// C00PacketLoginStart packetLoginStart = new
			// C00PacketLoginStart(profile);

			// setup network manager and handler for player
			// NetworkManager netManager = new
			// NetworkManager(EnumPacketDirection.SERVERBOUND);
			// NetHandlerPMPlayServer netHandler = new
			// NetHandlerPMPlayServer(server, netManager, fakePlayer);
			// // netManager.setNetHandler(new NetHandlerPMLoginServer(server,
			// // netManager, profile));

			// create server and client network managers
			SocketAddress address = netSystem.addLocalEndpoint();
			NetworkManager CNetManager = NetworkManager.provideLocalClient(address);

			// get manager from net system list via reflection
			NetworkManager SNetManager = null;
			Field netManagersField = null;
			try {
				netManagersField = netSystem.getClass().getDeclaredField("networkManagers");
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			List<NetworkManager> networkManagers = null;

			if (netManagersField != null) {
				netManagersField.setAccessible(true);
				try {
					networkManagers = (List<NetworkManager>) netManagersField.get(netSystem);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (networkManagers != null) {
				Iterator managerIterator = networkManagers.iterator();

				while (SNetManager == null && managerIterator.hasNext()) {
					NetworkManager result = (NetworkManager) managerIterator.next();
					System.out.println("result handler:" + result.getNetHandler());
					if (!result.hasNoChannel() && address == result.channel().localAddress()) {
						SNetManager = result;
					};
				}

				if (SNetManager == null) {
					throw new CommandException("Failed to log in. Unable to find Network Manager.");
				}

			} else {
				throw new CommandException("Failed to log in. Unable to find Network Manager List.");
			}

			// now that we have the manager, we can create the handler.
			NetHandlerPMPlayServer netHandler = new NetHandlerPMPlayServer(server, SNetManager, CNetManager, fakePlayer);
			System.out.println(netHandler.netManager);
			// SNetManager.setNetHandler(netHandler);
			// SNetManager.checkDisconnected();
			// CNetManager.setNetHandler(new NetHandlerPMPlayClient(CNetManager,
			// profile));

			// everything is ready! initialize connection.
			scm.initializeConnectionToPlayer(SNetManager, fakePlayer, netHandler);

			// Minecraft.get.sendPacket(new C00Handshake(47,
			// address.toString(), 0, EnumConnectionState.LOGIN, true));
			// MinecraftServer.getServer().get
			// NetworkManager CNetManager =
			// NetworkManager.provideLocalClient(address);
			// CNetManager.setNetHandler(new NetHandlerLoginClient(CNetManager,
			// Minecraft.getMinecraft(), (GuiScreen) null));
			// CNetManager.sendPacket(new C00Handshake(47,
			// socketaddress.toString(), 0, EnumConnectionState.LOGIN, true));
			// CNetManager.sendPacket(packetLoginStart);

			// NetworkManager netManager = new
			// NetworkManager(EnumPacketDirection.SERVERBOUND);
			// MinecraftServer.getServer().getNetworkSystem().

			// login
			// EntityPlayerMP fakePlayer = scm.createPlayerForUser(profile);

			// WorldServer overworldServer = MinecraftServer.getServer()
			// .worldServerForDimension(0);
			// ItemInWorldManager worldItemManager = new ItemInWorldManager(
			// overworldServer);

			// EntityPlayerMP player;
			//
			// if (customClass) {
			// player = new EntityPMPlayerServer(MinecraftServer.getServer(),
			// overworldServer, profile, worldItemManager);
			// } else {
			//
			// player = new EntityPlayerMP(MinecraftServer.getServer(),
			// overworldServer, profile, worldItemManager);
			// }

			// SocketAddress address =
			// Minecraft.getMinecraft().getIntegratedServer().getNetworkSystem().addLocalEndpoint();
			// SocketAddress address = null;

			// NetworkManager netManager =
			// NetworkManager.provideLocalClient(address);
			//
			// NetHandlerPlayServer nhps;
			//
			// if (customClass || true) {
			// nhps = new NetHandlerPMPlayServer(scm.getServerInstance(),
			// netManager, player);
			// } else {
			// nhps = new NetHandlerPlayServer(scm.getServerInstance(),
			// netManager, player);
			// }

			// NetHandlerPlayServer nhps = new NetHandlerPMPlayServer(
			// scm.getServerInstance(), netManager, player);
			// NetHandlerPlayServer nhps = new NetHandlerPlayServer(
			// scm.getServerInstance(), netManager, player);

			// scm.initializeConnectionToPlayer(netManager, player, nhps);

			// if
			// (!(player.getServerForPlayer().playerEntities.contains(player)))
			// {
			// player.getServerForPlayer().playerEntities.add(player);
			// // System.out.println("added " + player);
			// }

			// EntityPlayer clientEntry = Minecraft.getMinecraft().theWorld
			// .getPlayerEntityByName(username);
			// EntityPlayer serverEntry = MinecraftServer.getServer()
			// .worldServerForDimension(player.dimension)
			// .getPlayerEntityByName(username);
			// EntityPlayer scmEntry = scm.getPlayerByUsername(username);

			// System.out.println("Client:" + clientEntry);
			// wc.addEntityToWorld(p_73027_1_, player);

		}

		catch (RuntimeException runtimeexception) {
			runtimeexception.printStackTrace();
			throw new CommandException(String.format("Failed to log in"));
		}

		// if (player == null){
		//
		// throw new CommandException(String.format("Failed to log in"),
		// new Object[0]);
		//
		// }
	}

}
