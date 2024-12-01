package com.snackpirate.aeromancy.data;

import com.snackpirate.aeromancy.item.AAItems;
import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class AALang extends LanguageProvider {

	public AALang(PackOutput output, String modid, String locale) {
		super(output, modid, locale);
	}

	public AALang(PackOutput p) {
		this(p, Aeromancy.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		addSpell(AASpells.WIND_CHARGE.get(), "Wind Charge", "Fire a powerful wind charge, which launches enemies on impact.");
		addSchool(AASpells.Schools.WIND.get(), "Wind");
		add("ui.aero_additions.stun_duration", "%ss Stun Duration");
		add("ui.aero_additions.recast_targets", "%s Targets");
		addSpell(AASpells.UPDRAFT.get(), "Updraft", "Launch enemies into the air, temporarily stunning them.");
		addSpell(AASpells.AIRSTEP.get(), "Airstep", "Execute a series of mid-air jumps, in rapid succession.");
		addSpell(AASpells.ASPHYXIATE.get(), "Asphyxiate", "Draw the air out of the target's lungs, slowly killing them.");
		addSpell(AASpells.FEATHER_FALL.get(), "Feather Fall", "Give you and your nearby allies the Slow Falling effect.");
		addItem(AAItems.AIR_STAFF, "Air Staff");
		addItem(AAItems.UPDRAFT_TOME, "Updraft Tome");
		add("attribute.aero_additions.wind_spell_power", "Wind Spell Power");
		add("attribute.aero_additions.wind_magic_resist", "Wind Spell Resistance");
		addItem(AAItems.WIND_RUNE, "Wind Rune");
		addItem(AAItems.WIND_UPGRADE_ORB, "Wind Upgrade Orb");
		addItem(AAItems.WINDMAKER_HEADPIECE, "Windmaker Headpiece");
		addItem(AAItems.WINDMAKER_ROBES, "Windmaker Robes");
		addItem(AAItems.WINDMAKER_SKIRT, "Windmaker Leggings");
		addItem(AAItems.WINDMAKER_BOOTS, "Windmaker Boots");
		add("death.attack.asphyxiation", "%s discovered that air is essential to life");
		addEffect(AASpells.MobEffects.BREATHLESS, "Breathless");
	}
	public void addSpell(AbstractSpell spell, String name, String guide) {
		this.add("spell.aero_additions." + spell.getSpellResource().getPath(), name);
		this.add("spell.aero_additions." + spell.getSpellResource().getPath() + ".guide", guide);
	}
	public void addSchool(SchoolType school, String name) {
		this.add("school.aero_additions." + school.getId().getPath(), name);
	}
}
