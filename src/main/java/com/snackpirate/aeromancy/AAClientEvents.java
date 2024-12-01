package com.snackpirate.aeromancy;

import com.snackpirate.aeromancy.item.AAItems;
import com.snackpirate.aeromancy.spells.AASpells;
import com.snackpirate.aeromancy.spells.updraft.UpdraftVisualRenderer;
import com.snackpirate.aeromancy.spells.wind_charge.MagicWindChargeRenderer;
import io.redspace.ironsspellbooks.render.SpellBookCurioRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AAClientEvents {
	@SubscribeEvent
	public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(AASpells.Entities.MAGIC_WIND_CHARGE.get(), MagicWindChargeRenderer::new);
		event.registerEntityRenderer(AASpells.Entities.UPDRAFT_VISUAL_ENTITY.get(), UpdraftVisualRenderer::new);
		CuriosRendererRegistry.register(AAItems.UPDRAFT_TOME.get(), SpellBookCurioRenderer::new);
	}
}
