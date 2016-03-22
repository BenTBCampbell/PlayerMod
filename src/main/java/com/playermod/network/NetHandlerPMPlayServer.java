package com.playermod.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.demo.DemoWorldManager;
import net.minecraftforge.fml.common.network.handshake.NetworkDispatcher;

import com.playermod.entity.player.EntityPMPlayerServer;

public class NetHandlerPMPlayServer extends NetHandlerPlayServer implements INetHandlerPM {

	private MinecraftServer serverController;
	public NetworkManager clientNetManager;

	// public EntityPMPlayerServer playerEntity;

	public NetHandlerPMPlayServer(MinecraftServer server, NetworkManager netManager, NetworkManager clientNetManager, EntityPlayerMP player) {
		super(server, netManager, player);
		this.serverController = server;
		player.playerNetServerHandler = this;
		this.clientNetManager = clientNetManager;
		// this.playerEntity = player;
		System.out.println("New NetHandlerPMPlayServer");
	}

	@Override
	public void sendPacket(final Packet packet) {
		// System.out.println("send packet:" + packet);
		// super.sendPacket(packet);
	};

	@Override
	public void processPlayer(C03PacketPlayer packetIn) {
		// System.out.println("process");
		super.processPlayer(packetIn);
	}

	@Override
	public void update() {
		// System.out.println("update");
		// System.out.println("open channel:" +
		// this.netManager.channel().isOpen());
		// System.out.println("client open channel:" +
		// this.clientNetManager.channel().isOpen());
		super.update();
		// this.sendQueue.addToSendQueue(new
		// C03PacketPlayer.C05PacketPlayerLook(this.rotationYaw,
		// this.rotationPitch, this.onGround));
		// this.sendQueue.addToSendQueue(new C0CPacketInput(this.moveStrafing,
		// this.moveForward, this.movementInput.jump,
		// this.movementInput.sneak));

		this.processPlayer(new C03PacketPlayer.C06PacketPlayerPosLook(this.playerEntity.posX, this.playerEntity.getEntityBoundingBox().minY,
				this.playerEntity.posZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch, this.playerEntity.onGround));
		if (this.playerEntity.isDead) {
			System.out.println("Dead");
			this.processClientStatus(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
		}
		// this.playerEntity.onUpdateEntity();
	}

	@Override
	public void onDisconnect(IChatComponent reason) {
		System.out.println("disconnect:" + reason);
		System.out.println("open channel:" + this.netManager.channel().isOpen());
		System.out.println("client open channel:" + this.clientNetManager.channel().isOpen());
		super.onDisconnect(reason);
	}

	public void updateNetHandler() {
		System.out.println("update net handler");
		Packet packet = new C03PacketPlayer.C06PacketPlayerPosLook(this.playerEntity.posX, this.playerEntity.getEntityBoundingBox().minY,
				this.playerEntity.posZ, this.playerEntity.rotationYaw, this.playerEntity.rotationPitch, this.playerEntity.onGround);
		PacketThreadUtil.checkThreadAndEnqueue(packet, this, MinecraftServer.getServer());
		try {
			NetworkDispatcher netDisp = NetworkDispatcher.get(this.netManager);
			System.out.println(netDisp);
			// .acceptInboundMessage(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void respawnPlayer() {
		System.out.println("respawn player");
		try {
			// this.playerEntity =
			// this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity,
			// playerEntity.dimension, false);
			System.out.println(this.playerEntity);
			// EntityPlayer clientEntity =
			// Minecraft.getMinecraft().theWorld.getPlayerEntityByName(this.playerEntity.getName());
			// clientEntity.respawnPlayer();
			// MinecraftServer.getServer().worldServerForDimension(this.playerEntity.dimension).getEntityTracker()
			// .func_180245_a(this.playerEntity);

			// this.processClientStatus(new
			// C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
			this.playerEntity = this.recreatePlayerEntity(this.playerEntity, playerEntity.dimension, false, this.serverController.getConfigurationManager());
			// this.playerEntity.worldObj.

			System.out.println(this.playerEntity);
			// Minecraft.getMinecraft().getNetHandler().handleSpawnPlayer(new
			// S0CPacketSpawnPlayer(this.playerEntity));

			// WorldClient clientWorld = Minecraft.getMinecraft().theWorld;
			// EntityPlayer clientPlayer =
			// clientWorld.getPlayerEntityByName(this.playerEntity.getDisplayNameString());
			// S0CPacketSpawnPlayer clientRespawnPacket = new
			// S0CPacketSpawnPlayer(this.playerEntity);
			// System.out.println(clientPlayer);
			//
			// if (clientPlayer != null && false) {
			// clientWorld.removeEntity(clientPlayer);
			// Minecraft.getMinecraft().entityRenderer.updateCameraAndRender(1.0F);
			// MinecraftServer.getServer().worldServerForDimension(this.playerEntity.dimension).getEntityTracker().updateTrackedEntities();
			// };

			// this.serverController.getConfigurationManager().sendPacketToAllPlayersInDimension(new
			// S0CPacketSpawnPlayer(this.playerEntity),
			// this.playerEntity.dimension);

			// this.serverController.getConfigurationManager()
			System.out.println("player respawned");
			// System.out.println(this.playerEntity);
			// this.playerEntity =
			// this.serverController.getConfigurationManager().recreatePlayerEntity(this.playerEntity,
			// playerEntity.dimension, false);
		} catch (RuntimeException runtimeexception) {
			throw new IllegalStateException("respawn failed", runtimeexception);
		}
	}

	private EntityPMPlayerServer recreatePlayerEntity(EntityPlayerMP playerIn, int dimension, boolean conqueredEnd, ServerConfigurationManager scm) {
		World world = scm.getServerInstance().worldServerForDimension(dimension);
		if (world == null) {
			dimension = 0;
		} else if (!world.provider.canRespawnHere()) {
			dimension = world.provider.getRespawnDimension(playerIn);
		}

		playerIn.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(playerIn);
		playerIn.getServerForPlayer().getEntityTracker().untrackEntity(playerIn);
		playerIn.getServerForPlayer().getPlayerManager().removePlayer(playerIn);
		scm.playerEntityList.remove(playerIn);
		scm.getServerInstance().worldServerForDimension(playerIn.dimension).removePlayerEntityDangerously(playerIn);
		BlockPos blockpos = playerIn.getBedLocation(dimension);
		boolean flag1 = playerIn.isSpawnForced(dimension);
		playerIn.dimension = dimension;
		Object object;

		if (scm.getServerInstance().isDemo()) {
			object = new DemoWorldManager(scm.getServerInstance().worldServerForDimension(playerIn.dimension));
		} else {
			object = new ItemInWorldManager(scm.getServerInstance().worldServerForDimension(playerIn.dimension));
		}

		EntityPMPlayerServer entityplayermp1 = new EntityPMPlayerServer(scm.getServerInstance(), scm.getServerInstance().worldServerForDimension(
				playerIn.dimension), playerIn.getGameProfile(), (ItemInWorldManager) object);
		entityplayermp1.playerNetServerHandler = playerIn.playerNetServerHandler;
		entityplayermp1.clonePlayer(playerIn, conqueredEnd);
		entityplayermp1.dimension = dimension;
		entityplayermp1.setEntityId(playerIn.getEntityId());
		entityplayermp1.setCommandStats(playerIn);
		WorldServer worldserver = scm.getServerInstance().worldServerForDimension(playerIn.dimension);
		// scm.func_72381_a(entityplayermp1, playerIn, worldserver);
		entityplayermp1.theItemInWorldManager.setGameType(playerIn.theItemInWorldManager.getGameType());
		entityplayermp1.theItemInWorldManager.initializeGameType(worldserver.getWorldInfo().getGameType());

		BlockPos blockpos1;

		if (blockpos != null) {
			blockpos1 = EntityPlayer.getBedSpawnLocation(scm.getServerInstance().worldServerForDimension(playerIn.dimension), blockpos, flag1);

			if (blockpos1 != null) {
				entityplayermp1.setLocationAndAngles(blockpos1.getX() + 0.5F, blockpos1.getY() + 0.1F, blockpos1.getZ() + 0.5F, 0.0F, 0.0F);
				entityplayermp1.setSpawnPoint(blockpos, flag1);
			} else {
				entityplayermp1.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0F));
			}
		}

		worldserver.theChunkProviderServer.loadChunk((int) entityplayermp1.posX >> 4, (int) entityplayermp1.posZ >> 4);

		while (!worldserver.getCollidingBoundingBoxes(entityplayermp1, entityplayermp1.getEntityBoundingBox()).isEmpty() && entityplayermp1.posY < 256.0D) {
			entityplayermp1.setPosition(entityplayermp1.posX, entityplayermp1.posY + 1.0D, entityplayermp1.posZ);
		}

		entityplayermp1.playerNetServerHandler.sendPacket(new S07PacketRespawn(entityplayermp1.dimension, entityplayermp1.worldObj.getDifficulty(),
				entityplayermp1.worldObj.getWorldInfo().getTerrainType(), entityplayermp1.theItemInWorldManager.getGameType()));
		blockpos1 = worldserver.getSpawnPoint();
		entityplayermp1.playerNetServerHandler.setPlayerLocation(entityplayermp1.posX, entityplayermp1.posY, entityplayermp1.posZ, entityplayermp1.rotationYaw,
				entityplayermp1.rotationPitch);
		entityplayermp1.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(blockpos1));
		entityplayermp1.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(entityplayermp1.experience, entityplayermp1.experienceTotal,
				entityplayermp1.experienceLevel));

		WorldBorder worldborder = scm.getServerInstance().worldServers[0].getWorldBorder();
		S44PacketWorldBorder packet = new S44PacketWorldBorder(worldborder, S44PacketWorldBorder.Action.INITIALIZE);

		System.out.println("entityplayermp1:" + entityplayermp1);
		System.out.println("worldserver:" + worldserver);
		System.out.println("worldborder:" + worldborder);
		System.out.println("packet:" + packet);
		System.out.println("NetHandler:" + entityplayermp1.playerNetServerHandler);

		scm.updateTimeAndWeatherForPlayer(entityplayermp1, worldserver);
		worldserver.getPlayerManager().addPlayer(entityplayermp1);
		worldserver.spawnEntityInWorld(entityplayermp1);
		scm.playerEntityList.add(entityplayermp1);
		scm.uuidToPlayerMap.put(entityplayermp1.getUniqueID(), entityplayermp1);
		entityplayermp1.addSelfToInternalCraftingInventory();
		entityplayermp1.setHealth(entityplayermp1.getHealth());
		net.minecraftforge.fml.common.FMLCommonHandler.instance().firePlayerRespawnEvent(entityplayermp1);

		System.out.println("recreated player");

		return entityplayermp1;
	}

}
