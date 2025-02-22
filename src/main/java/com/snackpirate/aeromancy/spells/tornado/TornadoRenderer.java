package com.snackpirate.aeromancy.spells.tornado;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class TornadoRenderer extends GeoEntityRenderer<TornadoEntity> {
	public TornadoRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new TornadoModel());
	}

	@Override
	public @Nullable RenderType getRenderType(TornadoEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
		float f = (float)animatable.tickCount + partialTick;
		return RenderType.breezeWind(texture, this.xOffset(f)%1f, 0f);
	}

	@Override
	public Color getRenderColor(TornadoEntity animatable, float partialTick, int packedLight) {
		return new Color(0x80FFFFFF);
	}

	private float xOffset(float tickCount) {
		return tickCount * 0.02F;
	}

	@Override
	public void preRender(PoseStack poseStack, TornadoEntity animatable, BakedGeoModel model, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
		poseStack.scale(animatable.getSize() / 6.5f, animatable.getSize()  / 6.5f, animatable.getSize()  / 6.5f);
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
	}
}
