package com.playermod.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import com.playermod.client.model.ModelPather;

public class RenderPather extends RenderLiving {

	private ResourceLocation TEXTURE_PATHER = new ResourceLocation("playermod",
			"textures/entity/pather.png");

	public RenderPather(RenderManager p_i46153_1_) {
		super(p_i46153_1_, new ModelPather(), 0.25F);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TEXTURE_PATHER;
	}
}
