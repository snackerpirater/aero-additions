package com.snackpirate.aeromancy.spells.wind_shield;

import com.snackpirate.aeromancy.network.AeromancySpellData;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class WindShieldEffect extends MagicMobEffect implements ISyncedMobEffect {

	public WindShieldEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
		super.onEffectRemoved(pLivingEntity, pAmplifier);
//		MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().removeEffects(256L);
//		AeromancySpellData.getAeromancyData(pLivingEntity).removeEffects(AeromancySpellData.WIND_SHIELD);
	}

	@Override
	public void onEffectAdded(LivingEntity pLivingEntity, int pAmplifier) {
		super.onEffectAdded(pLivingEntity, pAmplifier);
//		MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().addEffects(256L);
//		AeromancySpellData.getAeromancyData(pLivingEntity).addEffects(AeromancySpellData.WIND_SHIELD);

	}
}
