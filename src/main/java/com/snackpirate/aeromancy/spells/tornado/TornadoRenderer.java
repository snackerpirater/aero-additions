package com.snackpirate.aeromancy.spells.tornado;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.snackpirate.aeromancy.Aeromancy;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.specialty.DynamicGeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class TornadoRenderer extends DynamicGeoEntityRenderer<TornadoEntity> {
	public TornadoRenderer(EntityRendererProvider.Context renderManager) {
		super(renderManager, new TornadoModel());
	}

	@Override
	protected @Nullable ResourceLocation getTextureOverrideForBone(GeoBone bone, TornadoEntity animatable, float partialTick) {
		return !bone.getName().contains("top") ? Aeromancy.id("textures/entity/tornado.png") : Aeromancy.id("textures/entity/tornado_top.png");
	}

	@Override
	public @Nullable RenderType getRenderType(TornadoEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
		float f = (float)animatable.tickCount + partialTick;
		return RenderType.breezeWind(texture, this.xOffset(f)%1f, 0f);
	}

	@Override
	protected @Nullable RenderType getRenderTypeOverrideForBone(GeoBone bone, TornadoEntity animatable, ResourceLocation texturePath, MultiBufferSource bufferSource, float partialTick) {
		float f = (float)animatable.tickCount + partialTick;
		return RenderType.breezeWind(texturePath, !bone.getName().contains("top") ? this.xOffset(f)%1f : 0, 0f);
	}

	@Override
	public Color getRenderColor(TornadoEntity animatable, float partialTick, int packedLight) {
		int colorValue = switch (animatable.getEffect()) {
			case DEFAULT -> 0xFFFFFF;
			case FIRE -> 0xFFA500;
			case ENDER -> 0xFF00FF;
			case NATURE -> 0x00FF00;
			case LIGHTNING -> 0x00FFFF;
			case ICE -> 0x50FFFF;
		};
		return new Color(0xD0000000 + colorValue);
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
