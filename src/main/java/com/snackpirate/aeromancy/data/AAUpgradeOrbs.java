package com.snackpirate.aeromancy.data;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.item.AAItems;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.item.armor.UpgradeOrbType;
import io.redspace.ironsspellbooks.registries.UpgradeOrbTypeRegistry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AAUpgradeOrbs {

	public static ResourceKey<UpgradeOrbType> WIND_SPELL_POWER = ResourceKey.create(UpgradeOrbTypeRegistry.UPGRADE_ORB_REGISTRY_KEY, Aeromancy.id("wind_spell_power"));

	public static void bootstrap(BootstrapContext<UpgradeOrbType> bootstrap) {
		bootstrap.register(WIND_SPELL_POWER,
				new UpgradeOrbType(AASpells.Attributes.WIND_SPELL_POWER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_BASE, AAItems.WIND_UPGRADE_ORB));
	}
}
