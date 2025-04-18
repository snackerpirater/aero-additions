package com.snackpirate.aeromancy;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AASounds {
	private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(Registries.SOUND_EVENT, Aeromancy.MOD_ID);

	public static void register(IEventBus eventBus) {
		SOUND_EVENTS.register(eventBus);
	}

	private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
		return SOUND_EVENTS.register(name, () -> {
			return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Aeromancy.MOD_ID, name));
		});
	}

	public static DeferredHolder<SoundEvent, SoundEvent> WIND_CAST = registerSoundEvent("cast.generic.wind");
	public static DeferredHolder<SoundEvent, SoundEvent> AIRBLAST_CAST = registerSoundEvent("cast.generic.airblast");
	public static DeferredHolder<SoundEvent, SoundEvent> THUNDERCLAP_CAST = registerSoundEvent("cast.generic.thunderclap");
	public static DeferredHolder<SoundEvent, SoundEvent> WIND_CAST_REVERSED = registerSoundEvent("cast.generic.wind_reversed");
}
