package com.playermod.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.playermod.common.PlayerMod;

public class ModItems {

	public static ItemPatherWand pather_wand;

	public static void registerItems() {

		pather_wand = new ItemPatherWand("PatherWand");
		GameRegistry.registerItem(pather_wand, pather_wand.getName());

	}

	public static void registerItemRenderers() {

		ItemModelMesher model_mesher = Minecraft.getMinecraft().getRenderItem()
				.getItemModelMesher();
		ModelResourceLocation pather_resource_loc = new ModelResourceLocation(
				PlayerMod.MODID + ":" + pather_wand.getName(), "inventory");
		model_mesher.register(pather_wand, 0, pather_resource_loc);

	}
}
