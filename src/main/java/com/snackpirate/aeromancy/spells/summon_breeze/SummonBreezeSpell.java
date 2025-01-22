package com.snackpirate.aeromancy.spells.summon_breeze;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellSummonEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;

@AutoSpellConfig
public class SummonBreezeSpell extends AbstractSpell {

	private final DefaultConfig defaultConfig = new DefaultConfig()
			.setMinRarity(SpellRarity.RARE)
			.setSchoolResource(AASpells.Schools.WIND_RESOURCE)
			.setMaxLevel(5)
			.setCooldownSeconds(180)
			.build();

	public SummonBreezeSpell() {
		this.manaCostPerLevel = 10;
		this.baseSpellPower = 4;
		this.spellPowerPerLevel = 1;
		this.castTime = 20;
		this.baseManaCost = 50;
	}

	@Override
	public CastType getCastType() {
		return CastType.LONG;
	}

	@Override
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}

	@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("summon_breeze");
	}
	@Override
	public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
		int summonTime = 20 * 60 * 10;

		SummonedBreeze breeze = new SummonedBreeze(world, entity);
		breeze.setPos(entity.position());

		breeze.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(20);
		breeze.setHealth(breeze.getMaxHealth());
		var event = NeoForge.EVENT_BUS.post(new SpellSummonEvent<>(entity, breeze, getSpellResource(), spellLevel));
		world.addFreshEntity(event.getCreature());

		breeze.addEffect(new MobEffectInstance(AASpells.MobEffects.BREEZE_TIMER, summonTime, 0, false, false, false));
		int effectAmplifier = 0;
		if (entity.hasEffect(AASpells.MobEffects.BREEZE_TIMER))
			effectAmplifier += entity.getEffect(AASpells.MobEffects.BREEZE_TIMER).getAmplifier() + 1;
		entity.addEffect(new MobEffectInstance(AASpells.MobEffects.BREEZE_TIMER, summonTime, effectAmplifier, false, false, true));

		super.onCast(world, spellLevel, entity, castSource, playerMagicData);
	}

}
