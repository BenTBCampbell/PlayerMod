package com.playermod.common;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.playermod.client.renderer.entity.RenderPather;
import com.playermod.client.renderer.entity.RenderPlayerMob;
import com.playermod.entity.EntityPather;
import com.playermod.entity.player.EntityPlayerMob;
import com.playermod.item.ModItems;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		// do common stuff
		super.preInit(event);
	}

	@Override
	public void load(FMLInitializationEvent event) {
		// do common stuff
		super.load(event);

		// do client-specific stuff
		// registerKeyBindings();
		// registerEntityRenderers();
		RenderingRegistry
				.registerEntityRenderingHandler(EntityPlayerMob.class,
						new RenderPlayerMob(Minecraft.getMinecraft()
								.getRenderManager()));
		// RenderingRegistry.registerEntityRenderingHandler(
		// EntityPMPlayer.class, new RenderPlayerMob2(Minecraft
		// .getMinecraft().getRenderManager()));
		RenderingRegistry.registerEntityRenderingHandler(EntityPather.class,
				new RenderPather(Minecraft.getMinecraft().getRenderManager()));
		ModItems.registerItemRenderers();
		// registerBlockRenderers();
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		// do common stuff
		super.postInit(event);

		// do client-specific stuff
		// BlockSmith.versionChecker = new VersionChecker();
		// Thread versionCheckThread = new Thread(BlockSmith.versionChecker,
		// "Version Check");
		// versionCheckThread.start();
	}
}
