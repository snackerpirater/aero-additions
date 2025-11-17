package com.snackpirate.aeromancy.spells.asphyxiate;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.data.AADamageTypes;
import com.snackpirate.aeromancy.network.AeromancySpellData;
import com.snackpirate.aeromancy.spells.wind_shield.WindySwirlRenderer;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BreathlessEffect extends MagicMobEffect implements ISyncedMobEffect {
	public BreathlessEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        Aeromancy.LOGGER.info("breathless tick");
		livingEntity.hurt(DamageSources.get(livingEntity.level(), AADamageTypes.ASPHYXIATION), 1+(0.2f*amplifier));
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        Aeromancy.LOGGER.info("should apply breathelss {}, {} -> {}", duration, amplifier, duration % (6-amplifier));
		return duration % (6 - amplifier) == 0;
	}

	@Override
	public void onEffectAdded(LivingEntity livingEntity, int amplifier) {
//		MagicData.getPlayerMagicData(livingEntity).getSyncedData().addEffects(WindySwirlRenderer.RENDER_ASPHYXIATION);\
		super.onEffectAdded(livingEntity, amplifier);
	}

	@Override
	public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
//		MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().removeEffects(WindySwirlRenderer.RENDER_ASPHYXIATION);
		super.onEffectRemoved(pLivingEntity, pAmplifier);
	}
}
