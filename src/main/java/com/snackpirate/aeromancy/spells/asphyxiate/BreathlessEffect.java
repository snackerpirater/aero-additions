package com.snackpirate.aeromancy.spells.asphyxiate;

import com.snackpirate.aeromancy.data.AADamageTypes;
import io.redspace.ironsspellbooks.damage.DamageSources;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BreathlessEffect extends MobEffect {
	public BreathlessEffect(MobEffectCategory category, int color) {
		super(category, color);
	}

	@Override
	public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
		livingEntity.hurt(DamageSources.get(livingEntity.level(), AADamageTypes.ASPHYXIATION), 1.5f);
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return duration % (6 - amplifier) == 0;
	}
}
