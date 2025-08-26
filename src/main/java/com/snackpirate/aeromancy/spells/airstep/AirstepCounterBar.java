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
		int x = screenWidth/2 + 100, y = screenHeight - 43 - ((Minecraft.getInstance().player != null && Minecraft.getInstance().player.isCreative()) ? 0 : 10);
		guiGraphics.blit(COUNTER_TEXTURE, x, y, 0, 0, 12, 12, 12, 12);
		guiGraphics.drawString(Minecraft.getInstance().font, "x" + AAClientData.getAeroSpellData(Minecraft.getInstance().player).getAirStepHitsRemaining(), x+13, y+2, 0xffffff);
	}
	private static boolean shouldRender() {
		return Minecraft.getInstance().player != null && Minecraft.getInstance().player.hasEffect(AASpells.MobEffects.AIRSTEPPING) && !Minecraft.getInstance().options.hideGui && !Minecraft.getInstance().player.isSpectator();
	}

}
