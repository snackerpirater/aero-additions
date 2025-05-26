package com.snackpirate.aeromancy.spells.feather_fall;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.data.AAItemTags;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public class FlightEffect extends MagicMobEffect {
	public FlightEffect(MobEffectCategory pCategory, int pColor) {
		super(pCategory, pColor);
	}

	//wearing armors (except for the windmaker robes) will weigh down the user in flight
	@Override
	public boolean applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
		if (pLivingEntity.tickCount % 20 == 0) {
			int weightScore = calculateWeightScore(pLivingEntity);
//			Aeromancy.LOGGER.info("flight weight {}", weightScore);

			AttributeInstance instance = pLivingEntity.getAttributes().getInstance(Attributes.GRAVITY);
			if (instance != null) {
				instance.removeModifier(Aeromancy.id("flight.gravity"));
				if (weightScore != 0)
					instance.addOrUpdateTransientModifier(new AttributeModifier(Aeromancy.id("flight.gravity"), (weightScore / 4f) - 0.08, AttributeModifier.Operation.ADD_VALUE));
//				Aeromancy.LOGGER.info("gravity attrib {}", (weightScore / 4f) - 0.16);
			}
		}
		return super.applyEffectTick(pLivingEntity, pAmplifier);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void onEffectRemoved(LivingEntity pLivingEntity, int pAmplifier) {
		Objects.requireNonNull(pLivingEntity.getAttribute(Attributes.GRAVITY)).removeModifier(Aeromancy.id("flight.gravity"));
		super.onEffectRemoved(pLivingEntity, pAmplifier);
	}

	private static int calculateWeightScore(LivingEntity living) {
		int totalScore = 0;
		for (EquipmentSlot slot: EquipmentSlot.values()) {
			int itemScore;
			ItemStack stackInSlot = living.getItemBySlot(slot);
//			Aeromancy.LOGGER.info("calculating weight of stack {}", stackInSlot.toString());
			if (stackInSlot.is(AAItemTags.FLIGHT_LIGHT) || !living.hasItemInSlot(slot)) itemScore = 0;
			else if (stackInSlot.is(AAItemTags.FLIGHT_HEAVY)) itemScore = 4;
			else if (stackInSlot.is(ItemTags.ANVIL)) itemScore = 10;
			else itemScore = 2;
			totalScore+=itemScore;
		}
		return totalScore;
	}

}
