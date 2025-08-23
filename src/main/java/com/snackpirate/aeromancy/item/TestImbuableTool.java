package com.snackpirate.aeromancy.item;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.world.item.*;

public class TestImbuableTool extends Item implements IPresetSpellContainer {

	public TestImbuableTool(Properties properties) {
		super(properties);
	}

	@Override
	public void initializeSpellContainer(ItemStack itemStack) {

		if (itemStack.getItem() instanceof TestImbuableTool) {
			if (!ISpellContainer.isSpellContainer(itemStack)) {
				var spellContainer = ISpellContainer.create(1, true, false).mutableCopy();
				spellContainer.addSpell(SpellRegistry.SPECTRAL_HAMMER_SPELL.get(), 6, true);
				itemStack.set(ComponentRegistry.SPELL_CONTAINER, spellContainer.toImmutable());
			}
		}
	}
}
