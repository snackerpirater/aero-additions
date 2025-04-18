package com.snackpirate.aeromancy.spells.wind_blade;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

@AutoSpellConfig
public class WindBladeSpell extends AbstractSpell {

	@Override
	public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
		return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation(getDamage(spellLevel, caster), 2)));
	}

	private final DefaultConfig defaultConfig = new DefaultConfig()
			.setMinRarity(SpellRarity.COMMON)
			.setSchoolResource(AASpells.Schools.WIND_RESOURCE)
			.setMaxLevel(10)
			.setCooldownSeconds(1)
			.build();

	@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("wind_blade");
	}
	public WindBladeSpell() {
		this.manaCostPerLevel = 2;
		this.baseSpellPower = 12;
		this.spellPowerPerLevel = 1;
		this.castTime = 0;
		this.baseManaCost = 10;
	}

	@Override
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}

	@Override
	public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
		WindBladeProjectile windBlade = new WindBladeProjectile(world, entity);
		windBlade.setPos(entity.position().add(0, entity.getEyeHeight() - windBlade.getBoundingBox().getYsize() * .5f, 0));
		windBlade.shoot(entity.getLookAngle());
		windBlade.setDamage(getDamage(spellLevel, entity));
		windBlade.setNoGravity(true);
		world.addFreshEntity(windBlade);
		super.onCast(world, spellLevel, entity, castSource, playerMagicData);
	}
	@Override
	public CastType getCastType() {
		return CastType.INSTANT;
	}

	@Override
	public AnimationHolder getCastFinishAnimation() {
		return SpellAnimations.SLASH_ANIMATION;
	}

	private float getDamage(int spellLevel, LivingEntity entity) {
		return getSpellPower(spellLevel, entity) * .5f;
	}

}
