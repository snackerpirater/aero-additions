package com.snackpirate.aeromancy.spells.airstep;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.capabilities.magic.ImpulseCastData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoSpellConfig
public class AirstepSpell extends AbstractSpell {
	private final DefaultConfig defaultConfig;
	@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("airstep");
	}

	@Override
	public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
		return List.of(Component.translatable("spell.aero_additions.airstep.max_jumps", spellLevel));
	}

	public AirstepSpell() {
		this.defaultConfig = (new DefaultConfig())
				.setMinRarity(SpellRarity.RARE)
				.setSchoolResource(AASpells.Schools.WIND_RESOURCE)
				.setMaxLevel(5)
				.setCooldownSeconds(20)
				.build();
		this.manaCostPerLevel = 1;
		this.baseSpellPower = 3;
		this.spellPowerPerLevel = 2;
		this.castTime = 0;
		this.baseManaCost = 20;
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
	public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
		return spellLevel;
	}

	@Override
	public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
		return !(entity.isFallFlying() || entity.isInWaterOrBubble());
	}

	@Override
	public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
		if (castData instanceof ImpulseCastData data) {
			entity.hasImpulse = data.hasImpulse;
			double y = Math.max(entity.getDeltaMovement().y, data.y);
			entity.setDeltaMovement(data.x, y, data.z);
		}
		super.onClientCast(level, spellLevel, entity, castData);
	}
	@Override
	public ICastDataSerializable getEmptyCastData() {
		return new ImpulseCastData();
	}

	@Override
	public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
		if (!playerMagicData.getPlayerRecasts().hasRecastForSpell(getSpellId())) {
			playerMagicData.getPlayerRecasts().addRecast(new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 80, castSource, null), playerMagicData);
		}
		Vec3 motion = new Vec3(0, entity.getAttributeValue(Attributes.JUMP_STRENGTH) + entity.getJumpBoostPower(), 0);
		Vec3 angle = entity.getLookAngle();
		motion = angle.scale(Mth.invSqrt(angle.x*angle.x + angle.z*angle.z)).multiply(0.45, 0, 0.45).add(motion);
		playerMagicData.setAdditionalCastData(new ImpulseCastData((float) motion.x, (float) motion.y, (float) motion.z, true));
		entity.setDeltaMovement(motion);
		MagicManager.spawnParticles(level, ParticleTypes.SPIT, entity.getX(), entity.getY(), entity.getZ(), 10, 0.2, 0, 0.2, 0.3, true);
		entity.hasImpulse = true;
		super.onCast(level, spellLevel, entity, castSource, playerMagicData);
	}
}