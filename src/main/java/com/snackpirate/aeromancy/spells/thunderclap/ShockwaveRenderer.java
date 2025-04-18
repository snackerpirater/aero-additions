package com.snackpirate.aeromancy.spells.thunderclap;

import io.redspace.ironsspellbooks.entity.spells.blood_slash.BloodSlashRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ShockwaveRenderer extends EntityRenderer<ShockwaveEntity> {
	protected ShockwaveRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(ShockwaveEntity shockwaveEntity) {
		return null;
	}
}
