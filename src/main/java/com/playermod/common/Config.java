package com.playermod.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {

	public static String[] players;
	public static List<String> players_with_skins = new ArrayList<String>();

	public static void loadConfig(FMLPreInitializationEvent event) {
		// resource pack file locations
		File mcmeta = new File(event.getModConfigurationDirectory().toString()
				+ "/playermod/pack.mcmeta");
		File steve_png = new File(event.getModConfigurationDirectory()
				.toString()
				+ "/playermod/assets/playermod/textures/entity/steve.png");
		File alex_png = new File(event.getModConfigurationDirectory()
				.toString()
				+ "/playermod/assets/playermod/textures/entity/alex.png");

		// make missing resource pack files
		if (!mcmeta.exists()) {
			ResourceLocation mcmeta_base = new ResourceLocation(
					"mymod:pack.mcmeta");
			createFile(
					mcmeta,
					"{\"pack\":{\"pack_format\":1, \"description\":\"Player Mod extra skins resource pack\"}}");
		}

		if (!steve_png.exists()) {
			try {
				ExportResourceFromTo(
						"assets/minecraft/textures/entity/steve.png",
						event.getModConfigurationDirectory().toString()
								+ "/playermod/assets/playermod/textures/entity/playermob/Steve.png");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!alex_png.exists()) {
			try {
				ExportResourceFromTo(
						"assets/minecraft/textures/entity/alex.png",
						event.getModConfigurationDirectory().toString()
								+ "/playermod/assets/playermod/textures/entity/playermob/Alex.png");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// paths to config/resources
		File config_location = new File(event.getModConfigurationDirectory()
				.toString() + "/playermod/playermod.cfg");
		File resource_location = new File(event.getModConfigurationDirectory()
				.toString() + "/playermod");

		// create config
		Configuration config = new Configuration(config_location);

		config.load();

		players = config
				.getStringList(
						"extra_players",
						Configuration.CATEGORY_GENERAL,
						new String[] { "Steve false", "Alex true" },
						"Add names to this list to have them appear in the game."
								+ "\nPut the name and if the skin has slim arms(like Alex) or not (this means its arms are like Steve's)"
								+ "\nSkins go in the .minecraft/config/playermod(the folder this file is in)/assets/playermod/textures/entity/playermob folder. "
								+ "\nSkins must be named EXACTLY the same as the name in this list to be associated correctly. Steve.png, not steve.png for Steve"
								+ "\nExamples: Steve.png for Steve, Alex.png for Alex"
								+ "\nSkins must also be 64x64. They won't render correctly if they are only 64x32."
								+ "\nSee http://minecraft.gamepedia.com/Skin#Creating_a_skin for more details on skins."
								+ "\n");

		config.save();

		// create / load resource pack
		// FolderResourcePack resources = new
		// FolderResourcePack(resource_location);
		//
		// List<IResourcePack> defaultResourcePacks =
		// ObfuscationReflectionHelper
		// .getPrivateValue(Minecraft.class, Minecraft.getMinecraft(),
		// "defaultResourcePacks", "field_110449_ao");
		//
		// defaultResourcePacks.add(resources);
		//
		// Minecraft.getMinecraft().refreshResources();

		// add names to players_with_skins[]
		for (String entry : players) {

			int index = entry.indexOf(" ");
			if (index != -1) {
				// get username part of entry
				entry = entry.substring(0, index);
			}

			ResourceLocation username_skin_location = new ResourceLocation(
					"playermod", "textures/entity/playermob/" + entry + ".png");

			try {
				// skin!
				((SimpleReloadableResourceManager) Minecraft.getMinecraft()
						.getResourceManager())
						.getResource(username_skin_location);
				players_with_skins.add(entry);
			} catch (IOException e) {
				// no skin
				System.out.println("[load_config] missing skin for " + entry);
			}
		}

	}

	public static void createFile(File file, String text) {
		try {
			if (file.getParentFile() != null) {
				file.getParentFile().mkdirs();
			}

			if (!file.exists() && !file.createNewFile()) {
				return;
			}

			if (file.canWrite()) {
				FileOutputStream fos = new FileOutputStream(file);
				BufferedWriter buffer = new BufferedWriter(
						new OutputStreamWriter(fos, "UTF-8"));

				buffer.write(text);

				buffer.close();
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public void ExportResourceFromTo(String inputPath, String outputPath)
			throws Exception {

		File f = new File(outputPath);
		if (f.getParentFile() != null) {
			f.getParentFile().mkdirs();
		}
		InputStream is = Config.class.getClassLoader().getResourceAsStream(
				inputPath); // get the input stream
		FileOutputStream fos = new FileOutputStream(f);
		System.out.println("exporting " + is + " to " + fos);
		while (is.available() > 0) { // write contents of 'is' to 'fos'
			fos.write(is.read());
		}
		fos.close();
		is.close();
	}

}
