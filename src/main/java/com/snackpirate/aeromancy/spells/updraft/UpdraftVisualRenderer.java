package com.snackpirate.aeromancy.spells.updraft;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.gust.GustCollider;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public class UpdraftVisualRenderer extends EntityRenderer<UpdraftEntity> {
	public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "gust_model"), "main");
	private static final ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/trident_riptide.png");
	private final ModelPart body;

	public UpdraftVisualRenderer(EntityRendererProvider.Context context) {
		super(context);
		ModelPart modelpart = context.bakeLayer(MODEL_LAYER_LOCATION);
		this.body = modelpart.getChild("body");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 32.0F, 16.0F), PartPose.ZERO);
		return LayerDefinition.create(meshdefinition, 64, 64);
	}
	public void render(UpdraftEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
		poseStack.pushPose();
		poseStack.translate(0.0, entity.getBoundingBox().getYsize() * 0.5, 0.0);
		poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot() - 180.0F));
		poseStack.mulPose(Axis.XP.rotationDegrees(-entity.getXRot() - 90.0F));
		poseStack.scale(0.25F, 0.10F, 0.25F);
		float f = (float)entity.tickCount + partialTicks;
		float scale = Mth.lerp(Mth.clamp(f / 6.0F, 0.0F, 1.0F), 1.0F, 2.3F);
		VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity)));
		float alpha = 1.0F - f / 10.0F;

		for(int i = 0; i < 3; ++i) {
			poseStack.mulPose(Axis.YP.rotationDegrees(f * 10.0F));
			poseStack.scale(scale, scale, scale);
			poseStack.translate(0.0F, scale - 1.0F, 0.0F);
			this.body.render(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int)(alpha * 255.0F), 255, 255, 255));
		}

		poseStack.popPose();
		super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
	}

	@Override
	public ResourceLocation getTextureLocation(UpdraftEntity updraftEntity) {
		return TEXTURE;
	}
}
