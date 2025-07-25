package com.snackpirate.aeromancy.spells.airstep;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.data.AAData;
import com.snackpirate.aeromancy.network.AeromancySpellData;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.ImpulseCastData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingFallEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
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
				.setMaxLevel(7)
				.setCooldownSeconds(6)
				.build();
		this.manaCostPerLevel = 7;
		this.baseSpellPower = 1;
		this.spellPowerPerLevel = 1;
		this.castTime = 0;
		this.baseManaCost = 40;
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
		return CastType.LONG;
	}

	@Override
	public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
		entity.addEffect(new MobEffectInstance(AASpells.MobEffects.AIRSTEPPING, 1000, spellLevel-1));
		AeromancySpellData.getAeromancyData(entity).setAirStepHitsRemaining(1 + spellLevel);
		super.onCast(level, spellLevel, entity, castSource, playerMagicData);
	}
	//when you hit the ground, perform a last-second jump to protect
	@SubscribeEvent
	static void protectFall(LivingFallEvent event) {
		LivingEntity entity = event.getEntity();
		if (!entity.hasEffect(AASpells.MobEffects.AIRSTEPPING)) return;
		int spellLevel = 1 + entity.getEffect(AASpells.MobEffects.AIRSTEPPING).getAmplifier();
		if (event.getDistance() >= event.getEntity().getAttributeValue(Attributes.SAFE_FALL_DISTANCE) && hasJumps(entity)) {
			event.setDamageMultiplier(0);
			if (!entity.level().isClientSide) MagicManager.spawnParticles(event.getEntity().level(), ParticleTypes.SPIT, entity.getX(), entity.getY(), entity.getZ(), 10, 0.2, 0, 0.2, 0.3, true);
			entity.playSound(SoundEvents.BREEZE_JUMP, 2, .9f + Utils.random.nextFloat() * .2f);
		}
		//once you hit the ground, regain ability to n-tuple jump
		AeromancySpellData.getAeromancyData(entity).setAirStepHitsRemaining(1 + spellLevel);
	}

	public static boolean airstepJump(LivingEntity entity) {
		if (!hasJumps(entity) || !entity.hasEffect(AASpells.MobEffects.AIRSTEPPING) || entity.onGround()) return false;
		Level level = entity.level();
		//dunno how else to get spell level other than to pass it through a mob effect, should change later
		int spellLevel = entity.getEffect(AASpells.MobEffects.AIRSTEPPING).getAmplifier() + 1;
		Vec3 motion = new Vec3(0, entity.getAttributeValue(Attributes.JUMP_STRENGTH) + entity.getJumpBoostPower() , 0);
		Vec3 angle = entity.getLookAngle();
		motion = angle.scale(Mth.invSqrt(angle.x*angle.x + angle.z*angle.z + 0.01f)).multiply(0.45, 0, 0.45).add(motion);
		entity.setDeltaMovement(motion);
		entity.resetFallDistance();
		if (!entity.level().isClientSide) MagicManager.spawnParticles(level, ParticleTypes.SPIT, entity.getX(), entity.getY(), entity.getZ(), 10, 0.2, 0, 0.2, 0.3, true);
		entity.hasImpulse = true;
		entity.playSound(SoundEvents.BREEZE_JUMP, 2, .9f + Utils.random.nextFloat() * .2f);
		AeromancySpellData.getAeromancyData(entity).subtractAirStepHits();
		return true;
	}

	@SubscribeEvent
	static void effectRemoved(MobEffectEvent.Remove event) {
		if (event.getEffectInstance().is(AASpells.MobEffects.AIRSTEPPING)) {
			AeromancySpellData.getAeromancyData(event.getEntity()).setAirStepHitsRemaining(0);
		}
	}
	static boolean hasJumps(LivingEntity entity) {
		return AeromancySpellData.getAeromancyData(entity).getAirStepHitsRemaining() > 0;
	}

	@SubscribeEvent
	static void effectRemoved(MobEffectEvent.Expired event) {
		if (event.getEffectInstance().is(AASpells.MobEffects.AIRSTEPPING)) {
			AeromancySpellData.getAeromancyData(event.getEntity()).setAirStepHitsRemaining(0);
		}
	}

	@Override
	public Optional<SoundEvent> getCastStartSound() {
		return super.getCastStartSound();
	}

	@Override
	public AnimationHolder getCastFinishAnimation() {
		return SpellAnimations.TOUCH_GROUND_ANIMATION;
	}

}