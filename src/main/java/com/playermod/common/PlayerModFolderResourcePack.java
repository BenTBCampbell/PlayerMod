package com.playermod.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.resources.FolderResourcePack;

public class PlayerModFolderResourcePack extends FolderResourcePack {

	public PlayerModFolderResourcePack(File p_i1291_1_) {
		super(p_i1291_1_);
	}

	@Override
	protected InputStream getInputStreamByName(String resourceName)
			throws IOException {
		try {
			return super.getInputStreamByName(resourceName);
		} catch (IOException ioe) {
			System.out.println("error. broken");
			throw ioe;
		}
	}

}
