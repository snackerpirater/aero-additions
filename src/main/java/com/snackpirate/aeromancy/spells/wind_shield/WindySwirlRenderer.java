package com.snackpirate.aeromancy.spells.wind_shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.snackpirate.aeromancy.AAClientEvents;
import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.network.AAClientData;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import io.redspace.ironsspellbooks.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@OnlyIn(Dist.CLIENT)
public class WindySwirlRenderer {
	private static final int COLOR = RenderHelper.colorf(0.5f, 0.5f, 0.5f);
	public static final long RENDER_ASPHYXIATION = 512;


	public static class Vanilla extends RenderLayer<Player, HumanoidModel<Player>> {
		public static ModelLayerLocation LAYER = new ModelLayerLocation(IronsSpellbooks.id("energy_layer"), "main");
		private final HumanoidModel<Player> model;
		private final ResourceLocation TEXTURE;
		private final Long shouldRenderFlag;

		public Vanilla(RenderLayerParent pRenderer, ResourceLocation texture, Long shouldRenderFlag) {
			super(pRenderer);
			this.model = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(LAYER));
			this.TEXTURE = texture;
			this.shouldRenderFlag = shouldRenderFlag;
		}

		@Override
		public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, Player pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTicks, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
			if (shouldRender(pLivingEntity, shouldRenderFlag)) {
				pMatrixStack.scale(1.1f, 1.0f, 1.1f);
				float f = (float) pLivingEntity.tickCount + pPartialTicks;
				HumanoidModel<Player> entitymodel = this.model;
				VertexConsumer vertexconsumer = pBuffer.getBuffer(getRenderType(Aeromancy.id("textures/entity/wind_shield_layer.png"), f));
				this.getParentModel().copyPropertiesTo(entitymodel);
				entitymodel.body.render(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, COLOR);
				entitymodel.leftArm.render(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, COLOR);
				entitymodel.rightArm.render(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, COLOR);
				entitymodel.leftLeg.render(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, COLOR);
				entitymodel.rightLeg.render(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, COLOR);
			}
			if (shouldRender(pLivingEntity, RENDER_ASPHYXIATION)) {
				pMatrixStack.scale(1.1f, 1.0f, 1.1f);
				float f = (float) pLivingEntity.tickCount + pPartialTicks;
				HumanoidModel<Player> entitymodel = this.model;
				VertexConsumer vertexconsumer = pBuffer.getBuffer(getRenderType(Aeromancy.id("textures/entity/wind_shield_layer.png"), f));
				this.getParentModel().copyPropertiesTo(entitymodel);
				entitymodel.head.render(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, COLOR);
			}
		}

		protected HumanoidModel<Player> model() {
			return model;
		}

	}
	public static class Geo extends GeoRenderLayer<AbstractSpellCastingMob> {
		private final ResourceLocation TEXTURE/* = new ResourceLocation(IronsSpellbooks.MODID, "textures/entity/evasion.png")*/;
		private final Long shouldRenderFlag;

		public Geo(GeoEntityRenderer<AbstractSpellCastingMob> entityRendererIn, ResourceLocation texture, Long shouldRenderFlag) {
			super(entityRendererIn);
			this.TEXTURE = texture;
			this.shouldRenderFlag = shouldRenderFlag;

		}

		@Override
		public void render(PoseStack poseStack, AbstractSpellCastingMob animatable, BakedGeoModel bakedModel, RenderType renderType2, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
			if (shouldRender(animatable, shouldRenderFlag)) {
				float f = (float) animatable.tickCount + partialTick;
				var renderType = getRenderType(Aeromancy.id("textures/entity/wind_shield_layer.png"), f);
				VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
				poseStack.pushPose();
				bakedModel.getBone("body").ifPresent((rootBone) -> {
					rootBone.getChildBones().forEach(bone -> {
						bone.updateScale(1.1f, 1.1f, 1.1f);
					});
				});
				this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick,
						packedLight, OverlayTexture.NO_OVERLAY, COLOR);

				bakedModel.getBone("body").ifPresent((rootBone) -> {
					rootBone.getChildBones().forEach(bone -> {
						bone.updateScale(1f, 1f, 1f);
					});
				});
				poseStack.popPose();
			}
			else if (shouldRender(animatable, shouldRenderFlag)) {
				float f = (float) animatable.tickCount + partialTick;
				var renderType = getRenderType(Aeromancy.id("textures/entity/wind_shield_layer.png"), f);
				VertexConsumer vertexconsumer = bufferSource.getBuffer(renderType);
				poseStack.pushPose();
				bakedModel.getBone("head").ifPresent((bone) -> {
					bone.setHidden(true);
				});
				bakedModel.getBone("body").ifPresent((rootBone) -> {
					rootBone.getChildBones().forEach(bone -> {
						bone.updateScale(1.1f, 1.1f, 1.1f);
					});
				});
				this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, vertexconsumer, true, partialTick,
						packedLight, OverlayTexture.NO_OVERLAY, COLOR);

				bakedModel.getBone("body").ifPresent((rootBone) -> {
					rootBone.getChildBones().forEach(bone -> {
						bone.updateScale(1f, 1f, 1f);
					});
				});
				poseStack.popPose();
			}
		}

	}


	private static RenderType getRenderType(ResourceLocation texture, float f) {
		return RenderType.energySwirl(texture, f * 0.02F % 1.0F, f * 0.01F % 1.0F);
	}
	private static boolean shouldRender(LivingEntity entity, Long shouldRenderFlag) {
		return AAClientData.getAeroSpellData(entity).hasEffect(shouldRenderFlag);
	}
}
