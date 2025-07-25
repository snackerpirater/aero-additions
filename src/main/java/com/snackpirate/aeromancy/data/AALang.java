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
		addEffect(AASpells.MobEffects.AIRSTEPPING, "Airstepping");
		add("spell.aero_additions.airstep.max_jumps", "%s Jumps");
		addSpell(AASpells.ASPHYXIATE.get(), "Asphyxiate", "Draw the air out of the target's lungs, slowly killing them.");
		addSpell(AASpells.FEATHER_FALL.get(), "Feather Fall", "Give you and your nearby allies the Slow Falling effect.");
		addItem(AAItems.AIR_STAFF, "Air Staff");
		addItem(AAItems.UPDRAFT_TOME, "Updraft Tome");
		add(AAItems.UPDRAFT_TOME.get().getDescriptionId() + ".guide", "Can be obtained from Ominous Vaults in Trial Chambers.");
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
		addEffect(AASpells.MobEffects.WIND_SHIELD, "Wind Shielded");
		addSpell(AASpells.WIND_SHIELD.get(), "Wind Shield", "Form a swirling shield of air around your body, reflecting any projectile coming your way.");
		add("spell.aero_additions.wind_shield.chance", "%s%% Chance To Deflect Projectiles");
		add("itemGroup.aero_additions.main_tab", "Aeromancy Additions");
		add("material.aero_additions.rune_wind", "Wind Rune");
		addSpell(AASpells.AIRBLAST.get(), "Airblast", "Quickly fire a small gust of air, capable of reflecting projectiles. Mmmph mmmph!");
		add("spell.aero_additions.airblast.max_range", "%s Block Range");
		add("spell.aero_additions.airblast.deflection_power", "%s%% Reflection Power");
		add("spell.aero_additions.airblast.degree_width", "%s° Arc");
		addItem(AAItems.WIND_SWORD, "Tempest Blade");
		addSpell(AASpells.WIND_BLADE.get(), "Wind Blade", "Conjure a swiftly-moving blade of pure, swirling wind, dealing low damage but high knockback upon impact.");
		addEntityType(AASpells.Entities.WIND_BLADE_PROJECTILE, "Wind Blade");
		add("death.attack.aero_additions.wind_blade", "%1$s was struck down by %2$s's Wind Blade");
		addEntityType(AASpells.Entities.MAGIC_WIND_CHARGE, "Magic Wind Charge");
		addEntityType(AASpells.Entities.UPDRAFT_VISUAL_ENTITY, "Gust");
		addEntityType(AASpells.Entities.SUMMONED_BREEZE, "Summoned Breeze");
		addSpell(AASpells.FLUSH.get(), "Flush", "Expel a large quantity of water around you.");
		addSpell(AASpells.DASH.get(), "Dash", "Quickly dash forward.");
//		addSpell(AASpells.THUNDERCLAP.get(), "Thunderclap", "With a booming clap, send forward an unstoppable shockwave of pressurized air that stuns enemies in its path.");
		addEntityType(AASpells.Entities.TORNADO, "Tornado");
//		addSpell(AASpells.TORNADO.get(), "Tornado", "Conjure forth a powerful tornado, dragging enemies into its vortex and launching them into the air.");
		addEffect(AASpells.MobEffects.FLIGHT, "Feather Flight");
//		addSpell(AASpells.TELELINK.get(), "Telelink", "Cast to select multiple targets to link them and yourself together with a binding force that synchronizes your teleports.");
		addEffect(AASpells.MobEffects.TELELINKED, "Telelinked");
	}
	public void addSpell(AbstractSpell spell, String name, String guide) {
		this.add("spell.aero_additions." + spell.getSpellResource().getPath(), name);
		this.add("spell.aero_additions." + spell.getSpellResource().getPath() + ".guide", guide);
	}

	public void addSchool(SchoolType school, String name) {
		this.add("school.aero_additions." + school.getId().getPath(), name);
	}
}
