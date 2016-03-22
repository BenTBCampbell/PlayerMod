package com.playermod.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import com.playermod.entity.player.EntityPlayerMob;

public class RenderPlayerMob extends RenderBiped {

	private boolean hasSmallArms;
	private ModelPlayer steveModel;
	private ModelPlayer alexModel;

	// render Player Mob

	public RenderPlayerMob(RenderManager rendermanager) {
		super(rendermanager, new ModelPlayer(0.0F, false), 0.5F);
		// setEntityTexture();
		this.steveModel = new ModelPlayer(0.0F, false);
		this.alexModel = new ModelPlayer(0.0F, true);
	}

	public void do_render_player_mob(EntityPlayerMob player, double x,
			double y, double z, float p_76986_8_, float partical_ticks) {
		this.check_model(player);
		super.doRender(player, x, y, z, p_76986_8_, partical_ticks);
	}

	private void check_model(EntityPlayerMob player) {
		if (player.has_small_arms()) {
			// System.out.println("changing " + player.get_username()
			// + "'s model to thin arms");
			this.mainModel = this.alexModel;
		} else {
			// System.out.println("changing " + player.get_username()
			// + "'s model to thick arms");
			this.mainModel = this.steveModel;
		}
		this.modelBipedMain = (ModelBiped) this.mainModel;
	}

	protected ResourceLocation get_entity_texture(EntityPlayerMob player) {
		return player.get_skin_location();
	};

	// render Entity Living

	@Override
	public void doRender(EntityLiving entity, double x, double y, double z,
			float p_76986_8_, float partialTicks) {
		this.do_render_player_mob((EntityPlayerMob) entity, x, y, z,
				p_76986_8_, partialTicks);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityLiving entity) {
		return get_entity_texture((EntityPlayerMob) entity);
	}

	@Override
	protected boolean canRenderName(EntityLiving targetEntity) {
		return true;
	}

	// render Entity Base
	public void doRender(EntityLivingBase entity, double x, double y, double z,
			float p_76986_8_, float partialTicks) {
		this.do_render_player_mob((EntityPlayerMob) entity, x, y, z,
				p_76986_8_, partialTicks);
	}

	// render Entity

	public void doRender(Entity entity, double x, double y, double z,
			float p_76986_8_, float partialTicks) {
		this.do_render_player_mob((EntityPlayerMob) entity, x, y, z,
				p_76986_8_, partialTicks);
	}

	protected ResourceLocation getEntityTexture(Entity player) {
		return get_entity_texture((EntityPlayerMob) player);
	}

}