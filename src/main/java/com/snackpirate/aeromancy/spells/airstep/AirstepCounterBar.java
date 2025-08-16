package com.snackpirate.aeromancy.spells.airstep;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.network.AAClientData;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;

public class AirstepCounterBar implements LayeredDraw.Layer {
	public static final AirstepCounterBar instance = new AirstepCounterBar();
	private static final ResourceLocation COUNTER_TEXTURE = Aeromancy.id("textures/gui/airstep_counter.png");
	@Override
	public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
		if (!shouldRender()) return;
		var screenWidth = guiGraphics.guiWidth();
		var screenHeight = guiGraphics.guiHeight();
		guiGraphics.blit(COUNTER_TEXTURE, screenWidth/2+56, screenHeight/2, 0, 0, 12, 12, 12, 12);
		guiGraphics.drawString(Minecraft.getInstance().font, "x" + AAClientData.getAeroSpellData(Minecraft.getInstance().player).getAirStepHitsRemaining(), screenWidth/2 + 20 + 50, screenHeight/2, 0xffffff);
	}
	private static boolean shouldRender() {
		return Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(AASpells.MobEffects.AIRSTEPPING) && !Minecraft.getInstance().options.hideGui && !Minecraft.getInstance().player.isSpectator();
	}
}
