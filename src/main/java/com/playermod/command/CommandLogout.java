package com.playermod.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;

import com.playermod.network.NetHandlerPMPlayServer;

public class CommandLogout extends CommandBase {

	@Override
	public String getCommandName() {
		return "logout";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "logout <username>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args)
			throws CommandException {

		if (args.length == 1) {
			EntityPlayerMP player = MinecraftServer.getServer()
					.getConfigurationManager().getPlayerByUsername(args[0]);

			if (player == null) {
				throw new PlayerNotFoundException();
			} else if (!(player.playerNetServerHandler instanceof NetHandlerPMPlayServer)) {

				throw new CommandException(String.format("%s is not a cpu",
						args[0]), new Object[0]);

			} else {

				player.playerNetServerHandler
						.kickPlayerFromServer("Logged out computer player.");

			}
		} else {
			throw new WrongUsageException("commands.kick.usage", new Object[0]);
		}

		/*
		 * if (args.length != 1) { throw new
		 * WrongUsageException("/logout <username>", new Object[0]); }
		 * 
		 * String username = args[0];
		 * 
		 * try {
		 * 
		 * ServerConfigurationManager scm = MinecraftServer.getServer()
		 * .getConfigurationManager();
		 * 
		 * // check if player exists if (scm.getPlayerByUsername(args[0]) ==
		 * null) { throw new
		 * CommandException(String.format("%s is not logged in", username)); }
		 * 
		 * // logout EntityPlayerMP player = scm.getPlayerByUsername(username);
		 * NetworkManager netManager = player.playerNetServerHandler.netManager;
		 * netManager.closeChannel(new ChatComponentText("Quitting"));
		 * netManager.checkDisconnected();
		 * 
		 * } catch (RuntimeException runtimeexception) { throw new
		 * CommandException(String.format("Failed To Log %s Out", username)); }
		 */

	}

	@Override
	public List addTabCompletionOptions(ICommandSender sender, String[] args,
			BlockPos pos) {
		return args.length == 1 ? getListOfStringsMatchingLastWord(args,
				MinecraftServer.getServer().getAllUsernames()) : null;
	}

}
