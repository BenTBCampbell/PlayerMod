package net.minecraftforge.fml.common.network.internal;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.internal.FMLMessage.EntitySpawnMessage;

import com.google.common.base.Function;

public class SpawnEntityPMPlayerServer {

	public static Function<EntitySpawnMessage, Entity> customSpawnFunction = new Function<EntitySpawnMessage, Entity>() {

		@Override
		public Entity apply(EntitySpawnMessage message) {
			System.out.println("Custom Spawn");
			System.out.println(message.entity);
			return message.entity;
		}

	};

}
