package com.playermod.common;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.playermod.command.CommandLogin;
import com.playermod.command.CommandLogout;
import com.playermod.command.CommandTest;
import com.playermod.entity.EntityPather;
import com.playermod.entity.player.EntityPMPlayerServer;
import com.playermod.entity.player.EntityPlayerMob;
import com.playermod.item.ModItems;

public class CommonProxy {

	protected int modEntityID = 0;

	public void preInit(FMLPreInitializationEvent event) {
		// load configuration before doing anything else, because might use
		// config values during registration
		// initConfig(event);
		// register stuff
		// registerBlocks();
		ModItems.registerItems();
		// registerTileEntities();
		// registerModEntities();
		EntityRegistry.registerModEntity(EntityPlayerMob.class, "PlayerMob",
				++modEntityID, PlayerMod.instance, 80, 3, false);
		EntityRegistry.registerModEntity(EntityPMPlayerServer.class,
				"PMPlayer",
				++modEntityID, PlayerMod.instance, 80, 3, false);
		EntityRegistry.registerModEntity(EntityPather.class, "Pather",
				++modEntityID, PlayerMod.instance, 80, 3, false);

		// Class[] params = new Class[2];
		// params[0] = EntitySpawnMessage.class;
		// params[1] = Entity.class;
		// Function<EntitySpawnMessage, Entity> customSpawn =
		// EntityPMPlayerServer.customSpawningFunc;

		EntityRegistry.instance()
				.lookupModSpawn(EntityPMPlayerServer.class, false)
				.setCustomSpawning(
null, true);

		// PMEntityPlayerSpawnHandler.customSpawningFunc
		// EntityPMPlayerServer.class.getMethod("customSpawning");

		// registerFuelHandlers();
		// registerSimpleNetworking();
		// registerVillagers();
	}

	public void load(FMLInitializationEvent event) {
		// register custom event listeners
		PlayerMod.registerEventListeners();
		// if (event.getSide() == Side.CLIENT) {
		// RenderItem render_item = Minecraft.getMinecraft().getRenderItem();
		//
		// ItemPatherWand pather_wand = new ItemPatherWand("pather_wand");
		// render_item.getItemModelMesher().register(
		// pather_wand,
		// 0,
		// new ModelResourceLocation(PlayerMod.MODID + ":"
		// + pather_wand.getName(), "inventory"));
		// }

		// register recipes here to allow use of items from other mods
		// registerRecipes();

		// register achievements here to allow use of items from other mods
		// registerAchievements();

		// register gui handlers
		// registerGuiHandlers();
	}

	public void serverLoad(FMLServerStartingEvent event) {
		// register server commands
		event.registerServerCommand(new CommandLogin());
		event.registerServerCommand(new CommandLogout());
		event.registerServerCommand(new CommandTest());
	}

	public void postInit(FMLPostInitializationEvent event) {
	}

}
