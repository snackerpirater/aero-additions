package com.snackpirate.aeromancy.data;

import com.snackpirate.aeromancy.Aeromancy;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class AADamageTypes {
	public static ResourceKey<DamageType> register(String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, Aeromancy.id(name));
	}

	public static final ResourceKey<DamageType> WIND_MAGIC = register("wind_magic");
	public static final ResourceKey<DamageType> ASPHYXIATION = register("asphyxiation");
	public static void bootstrap(BootstrapContext<DamageType> context) {

		context.register(WIND_MAGIC, new DamageType(WIND_MAGIC.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f));
		context.register(ASPHYXIATION, new DamageType(ASPHYXIATION.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0f));
	}
}
