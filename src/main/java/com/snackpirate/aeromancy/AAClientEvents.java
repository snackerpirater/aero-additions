package com.snackpirate.aeromancy;

import com.snackpirate.aeromancy.item.AAItems;
import com.snackpirate.aeromancy.network.AeromancySpellData;
import com.snackpirate.aeromancy.spells.AASpells;
import com.snackpirate.aeromancy.spells.tornado.TornadoRenderer;
import com.snackpirate.aeromancy.spells.updraft.UpdraftVisualRenderer;
import com.snackpirate.aeromancy.spells.wind_blade.WindBladeRenderer;
import com.snackpirate.aeromancy.spells.wind_charge.MagicWindChargeRenderer;
import com.snackpirate.aeromancy.spells.wind_shield.WindySwirlRenderer;
import io.redspace.ironsspellbooks.render.*;
import net.minecraft.client.renderer.entity.BreezeRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;


@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AAClientEvents {

	@SubscribeEvent
	public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
		CuriosRendererRegistry.register(AAItems.UPDRAFT_TOME.get(), SpellBookCurioRenderer::new);

		event.registerEntityRenderer(AASpells.Entities.MAGIC_WIND_CHARGE.get(), MagicWindChargeRenderer::new);
		event.registerEntityRenderer(AASpells.Entities.UPDRAFT_VISUAL_ENTITY.get(), UpdraftVisualRenderer::new);
		event.registerEntityRenderer(AASpells.Entities.SUMMONED_BREEZE.get(), BreezeRenderer::new);
		event.registerEntityRenderer(AASpells.Entities.WIND_BLADE_PROJECTILE.get(), WindBladeRenderer::new);
		event.registerEntityRenderer(AASpells.Entities.TORNADO.get(), TornadoRenderer::new);


	}

	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.AddLayers event) {
		addLayerToPlayerSkin(event, PlayerSkin.Model.SLIM);
		addLayerToPlayerSkin(event, PlayerSkin.Model.WIDE);

//		for (EntityType<?> type : event.getEntityTypes()) {
//			var renderer = event.getRenderer(type);
//			if (renderer instanceof GeoLivingEntityRenderer geoRenderer) {
//				geoRenderer.addRenderLayer(new WindySwirlRenderer.Geo(geoRenderer, Aeromancy.id("textures/entity/wind_shield_layer.png"), 256L));
//			}
//		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static void addLayerToPlayerSkin(EntityRenderersEvent.AddLayers event, PlayerSkin.Model skinName) {
		EntityRenderer<? extends Player> render = event.getSkin(skinName);
		if (render instanceof LivingEntityRenderer livingRenderer) {
			livingRenderer.addLayer(new WindySwirlRenderer.Vanilla(livingRenderer, Aeromancy.id("textures/entity/wind_shield_layer.png"), AeromancySpellData.WIND_SHIELD));
		}
	}
}
