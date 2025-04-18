package com.snackpirate.aeromancy.spells.thunderclap;

import com.snackpirate.aeromancy.AASounds;
import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

//Like Hulk's thunderclap move, a big loud clap that sends a shockwave of pressurized air forwards
//Does damage, knockback, and stuns
//Awesome sfx
@AutoSpellConfig
public class ThunderclapSpell extends AbstractSpell {
	public ThunderclapSpell() {
		this.manaCostPerLevel = 20;
		this.baseSpellPower = 10;
		this.spellPowerPerLevel = 1;
		this.castTime = 30;
		this.baseManaCost = 100;
	}
	private final DefaultConfig defaultConfig = new DefaultConfig()
			.setMinRarity(SpellRarity.RARE)
			.setSchoolResource(AASpells.Schools.WIND_RESOURCE)
			.setMaxLevel(5)
			.setCooldownSeconds(15)
			.build();
	@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("thunderclap");
	}

	@Override
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}

	@Override
	public CastType getCastType() {
		return CastType.LONG;
	}

	@Override
	public Optional<SoundEvent> getCastStartSound() {
		return AASounds.WIND_CAST_REVERSED.asOptional();
	}

	@Override
	public AnimationHolder getCastStartAnimation() {
		return SpellAnimations.CHARGE_ANIMATION;
	}

	@Override
	public AnimationHolder getCastFinishAnimation() {
		return SpellAnimations.FINISH_ANIMATION;
	}

	@Override
	public Optional<SoundEvent> getCastFinishSound() {
		return AASounds.THUNDERCLAP_CAST.asOptional();
	}
}
