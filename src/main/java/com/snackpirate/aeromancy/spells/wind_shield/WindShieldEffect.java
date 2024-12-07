package com.snackpirate.aeromancy.spells.wind_shield;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WindShieldEffect extends MagicMobEffect {

	public WindShieldEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
		super.onEffectRemoved(pLivingEntity, pAmplifier);
		//TODO: should we use ISS' data flags for the effect? potential conflicts?
		MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().removeEffects(256L);
	}

	@Override
	public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
		super.onEffectAdded(pLivingEntity, pAmplifier);
		MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().addEffects(256L);

	}
}
