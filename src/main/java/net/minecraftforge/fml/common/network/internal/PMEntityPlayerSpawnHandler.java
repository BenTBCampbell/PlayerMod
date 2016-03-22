package net.minecraftforge.fml.common.network.internal;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.internal.FMLMessage.EntitySpawnMessage;

import com.google.common.base.Function;

public class PMEntityPlayerSpawnHandler {

	public static Function<EntitySpawnMessage, Entity> customSpawningFunc = new Function<EntitySpawnMessage, Entity>() {

		@Override
		public Entity apply(EntitySpawnMessage spawnMsg) {
			// TODO Auto-generated method stub
			System.out.println("Custom EntityPMPlayerServer Spawning");
			// System.out.println("IsRemote:" +
			// spawnMsg.entity.getEntityWorld().);
			printMessageContents(spawnMsg);
			// System.out.println("EntitySpawnMessage:" + t.toString());
			// ModContainer mc =
			// Loader.instance().getIndexedModList().get(spawnMsg.modId);
			// EntityRegistration er =
			// EntityRegistry.instance().lookupModSpawn(mc,
			// spawnMsg.modEntityTypeId);
			// WorldClient wc = FMLClientHandler.instance().getWorldClient();
			// ServerConfigurationManager scm = MinecraftServer.getServer()
			// .getConfigurationManager();
			// EntityPlayer player = scm.getPlayerByUUID(playerUUID);
			// // Class<? extends Entity> cls = er.getEntityClass();
			//
			// // Entity entity = new Entity;
			//
			// Entity entity = new EntityOtherPlayerMP(wc, null);
			//
			// int offset = spawnMsg.entityId - entity.getEntityId();
			// entity.setEntityId(spawnMsg.entityId);
			// entity.setLocationAndAngles(spawnMsg.scaledX, spawnMsg.scaledY,
			// spawnMsg.scaledZ, spawnMsg.scaledYaw, spawnMsg.scaledPitch);
			// if (entity instanceof EntityLiving) {
			// ((EntityLiving) entity).rotationYawHead = spawnMsg.scaledHeadYaw;
			// }
			//
			// Entity parts[] = entity.getParts();
			// if (parts != null) {
			// for (int j = 0; j < parts.length; j++) {
			// parts[j].setEntityId(parts[j].getEntityId() + offset);
			// }
			// }
			return null;
		}

	};

	public static void printMessageContents(EntitySpawnMessage message) {
		System.out.println("Print Message Start");
		System.out.println("entityId:" + message.entityId);
		System.out.println("modEntityTypeId:" + message.modEntityTypeId);
		System.out.println("modId:" + message.modId);
		System.out.println("rawX:" + message.rawX);
		System.out.println("rawY:" + message.rawY);
		System.out.println("rawZ:" + message.rawZ);
		System.out.println("scaledHeadYaw:" + message.scaledHeadYaw);
		System.out.println("scaledPitch:" + message.scaledPitch);
		System.out.println("scaledX:" + message.scaledX);
		System.out.println("scaledY:" + message.scaledY);
		System.out.println("scaledYaw:" + message.scaledYaw);
		System.out.println("scaledZ:" + message.scaledZ);
		System.out.println("speedScaledX:" + message.speedScaledX);
		System.out.println("speedScaledY:" + message.speedScaledY);
		System.out.println("speedScaledZ:" + message.speedScaledZ);
		System.out.println("dataStream:" + message.dataStream);
		System.out.println("dataWatcherList:" + message.dataWatcherList);
		System.out.println("entity:" + message.entity);
		System.out.println("Print Message End");
	}

}
