package com.snackpirate.aeromancy.spells.wind_charge;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class WindChargeSpell extends AbstractSpell {

	public WindChargeSpell() {
		this.manaCostPerLevel = 8;
		this.baseSpellPower = 10;
		this.spellPowerPerLevel = 3;
		this.castTime = 0;
		this.baseManaCost = 20;
	}

	@Override
	public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
		return List.of(
				Component.translatable("ui.irons_spellbooks.strength", String.format("%s%%", (int) (getDamage(spellLevel, caster) * 100)))
		);
	}

	private final DefaultConfig defaultConfig = new DefaultConfig()
			.setMinRarity(SpellRarity.COMMON)
			.setSchoolResource(AASpells.Schools.WIND_RESOURCE)
			.setMaxLevel(5)
			.setCooldownSeconds(1)
			.build();

	@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("wind_charge");
	}

	@Override
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}

	@Override
	public SchoolType getSchoolType() {
		return AASpells.Schools.WIND.get();
	}

	@Override
	public CastType getCastType() {
		return CastType.INSTANT;
	}

	@Override
	public AnimationHolder getCastStartAnimation() {
		return SpellAnimations.ANIMATION_INSTANT_CAST;
	}

	@Override
	public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
//		Aeromancy.LOGGER.info("wind charge cast");
		MagicWindCharge charge = new MagicWindCharge(level, entity);
		charge.setPos(entity.position().add(0, entity.getEyeHeight() - charge.getBoundingBox().getYsize() * .5f, 0));
		charge.setDamage(getDamage(spellLevel, entity));
//		Aeromancy.LOGGER.info("damage {}", charge.getDamage());
		charge.shoot(entity.getLookAngle());
		level.addFreshEntity(charge);
		super.onCast(level, spellLevel, entity, castSource, playerMagicData);
	}

	private float getDamage(int spellLevel, LivingEntity entity) {
		//1 = 1 wind charge; 2 = 2 wind charge
		return 0.2f * (float) Math.sqrt(getSpellPower(spellLevel, entity))/0.6324555f;
	}

}
