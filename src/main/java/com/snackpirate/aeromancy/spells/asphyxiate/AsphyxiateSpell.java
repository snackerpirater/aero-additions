package com.snackpirate.aeromancy.spells.asphyxiate;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.data.AAEntityTypeTags;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

@AutoSpellConfig
public class AsphyxiateSpell extends AbstractSpell {
	private final DefaultConfig defaultConfig;
	@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("asphyxiate");
	}

	@Override
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}

	@Override
	public CastType getCastType() {
		return CastType.CONTINUOUS;
	}

	@Override
	public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
		if (playerMagicData != null && playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData && ((playerMagicData.getCastDurationRemaining()) % 10 == 0)) {
//			Aeromancy.LOGGER.info("asphyx cast tick");
			var target = ((TargetEntityCastData) playerMagicData.getAdditionalCastData()).getTarget((ServerLevel) level);
			if (target != null) target.addEffect(new MobEffectInstance(AASpells.MobEffects.BREATHLESS, 10, spellLevel-1));
		}
		super.onServerCastTick(level, spellLevel, entity, playerMagicData);
	}

	@Override
	public Optional<SoundEvent> getCastStartSound() {
		return Optional.of(SoundEvents.BREEZE_IDLE_GROUND);
	}

	@Override
	public Optional<SoundEvent> getCastFinishSound() {
		return Optional.of(SoundEvents.BREEZE_IDLE_GROUND);
	}

	@Override
	public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
		if (Utils.preCastTargetHelper(level, entity, playerMagicData, this, 15, .15f)) {
			var target = ((TargetEntityCastData) playerMagicData.getAdditionalCastData()).getTarget((ServerLevel) level);
			if (target == null || target.getType().is(AAEntityTypeTags.ASPHYXIATION_IMMUNE)) {
				return false;
			}
			playerMagicData.setAdditionalCastData(new TargetEntityCastData(target));
			return true;
		} else {
			return false;
		}
	}

	@Override
	public AnimationHolder getCastStartAnimation() {
		return SpellAnimations.ANIMATION_CONTINUOUS_OVERHEAD;
	}

	public AsphyxiateSpell() {
		this.defaultConfig = (new DefaultConfig())
				.setMinRarity(SpellRarity.EPIC)
				.setSchoolResource(AASpells.Schools.WIND_RESOURCE)
				.setMaxLevel(3)
				.setCooldownSeconds(20)
				.build();
		this.manaCostPerLevel = 3;
		this.baseSpellPower = 3;
		this.spellPowerPerLevel = 2;
		this.castTime = 100;
		this.baseManaCost = 9;
	}

	@Override
	public int getCastTime(int spellLevel) {
		return castTime + 100 * spellLevel;
	}
}
