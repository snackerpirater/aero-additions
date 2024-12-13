package com.snackpirate.aeromancy;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.snackpirate.aeromancy.spells.AASpells;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//copied from ScreenEffectsOverlay / OverlayRegistry
public class AAOverlay implements LayeredDraw.Layer {
	private static float ticks;
	public static AAOverlay instance = new AAOverlay();
	@SubscribeEvent
	public static void register(RegisterGuiLayersEvent event) {
		event.registerAboveAll(Aeromancy.id("screen_effects"), instance);
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
		ticks+=deltaTracker.getGameTimeDeltaTicks();
		if (Minecraft.getInstance().options.hideGui || (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isSpectator())) {
			return;
		}
		var screenWidth = guiGraphics.guiWidth();
		var screenHeight = guiGraphics.guiHeight();


		Player player = Minecraft.getInstance().player;
		if (player == null) {
			return;
		}
		if (player.hasEffect(AASpells.MobEffects.BREATHLESS)) {
			renderOverlay(guiGraphics, Aeromancy.id("textures/entity/wind_shield_layer.png"), 0.35f, 0.35f, 0.35f, 0.1f, screenWidth, screenHeight);
		}
	}

	private static void renderOverlay(GuiGraphics gui, ResourceLocation texture, float r, float g, float b, float a, int screenWidth, int screenHeight) {
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE
		);
		gui.setColor(r, g, b, a);
		gui.blit(texture, 0, 0, -90, 80*ticks, ticks, screenWidth, screenHeight, 2*screenWidth, 2*screenHeight);
		gui.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.defaultBlendFunc();
		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
	}


}
