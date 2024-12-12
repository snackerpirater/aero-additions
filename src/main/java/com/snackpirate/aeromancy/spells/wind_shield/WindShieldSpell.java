package com.snackpirate.aeromancy.spells.wind_shield;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SpongeBlock;

import java.util.List;

@AutoSpellConfig
public class WindShieldSpell extends AbstractSpell {
	public WindShieldSpell() {
		this.manaCostPerLevel = 30;
		this.baseSpellPower = 30;
		this.spellPowerPerLevel = 8;
		this.castTime = 60;
		this.baseManaCost = 100;
	}

	@Override
	public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
		return List.of(
				Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(getSpellPower(spellLevel, caster) * 20, 1)),
				Component.translatable("spell.aero_additions.wind_shield.chance", Utils.stringTruncation(chanceToDeflect(spellLevel-1)*100, 2)
				));
	}
		@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("wind_shield");
	}

	@Override
	public DefaultConfig getDefaultConfig() {
		return new DefaultConfig()
				.setMinRarity(SpellRarity.EPIC)
				.setSchoolResource(AASpells.Schools.WIND_RESOURCE)
				.setMaxLevel(5)
				.setCooldownSeconds(120)
				.build();
	}
	@Override
	public CastType getCastType() {
		return CastType.LONG;
	}

	@Override
	public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
		entity.addEffect(new MobEffectInstance(AASpells.MobEffects.WIND_SHIELD, (int) getSpellPower(spellLevel, entity) * 20, spellLevel-1, false, false, true));
		super.onCast(level, spellLevel, entity, castSource, playerMagicData);
	}
	public static float chanceToDeflect(int amplifier) {
		float chanceToDeflect = amplifier / 10f + 0.3f;
		return chanceToDeflect;
	}
}
