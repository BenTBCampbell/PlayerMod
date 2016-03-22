package com.playermod.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import com.playermod.event.PlayerModEventHandler;
import com.playermod.event.PlayerModFMLEventHandler;

@Mod(modid = PlayerMod.MODID, name = PlayerMod.MODNAME, version = PlayerMod.MODVER)
public class PlayerMod {
	@Instance("playermod")
	public static PlayerMod instance;

	// Set the ID of the mod (Should be lower case).
	public static final String MODID = "playermod";
	// Set the "Name" of the mod.
	public static final String MODNAME = "Player Mod";
	// Set the version of the mod.
	public static final String MODVER = "1.0.0";

	@SidedProxy(clientSide = "com.playermod.common.ClientProxy", serverSide = "com.playermod.common.CommonProxy")
	public static CommonProxy proxy;

	public static void registerEventListeners() {

		MinecraftForge.EVENT_BUS.register(new PlayerModEventHandler());
		FMLCommonHandler.instance().bus().register(new PlayerModFMLEventHandler());

	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
//		Config.loadConfig(event);
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		proxy.load(event);

	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		proxy.serverLoad(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
