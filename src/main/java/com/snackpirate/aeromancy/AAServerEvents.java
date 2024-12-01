package com.snackpirate.aeromancy;

import com.snackpirate.aeromancy.spells.AASpells;
import net.minecraft.world.entity.EntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AAServerEvents {
	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, AASpells.Attributes.WIND_SPELL_POWER);
		event.add(EntityType.PLAYER, AASpells.Attributes.WIND_MAGIC_RESIST);
	}
}
