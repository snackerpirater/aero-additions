package com.snackpirate.aeromancy.mixin;

import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.spells.evocation.GustSpell;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GustSpell.class)
public abstract class GustSpellMixin extends AbstractSpell {

	@Override
	public SchoolType getSchoolType() {
		return AASpells.Schools.WIND.get();
	}
}
