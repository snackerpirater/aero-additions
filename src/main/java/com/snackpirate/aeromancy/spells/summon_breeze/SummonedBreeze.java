package com.snackpirate.aeromancy.spells.summon_breeze;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import io.redspace.ironsspellbooks.entity.mobs.goals.*;
import io.redspace.ironsspellbooks.util.OwnerHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class SummonedBreeze extends Breeze implements IMagicSummon {

	public SummonedBreeze(EntityType<? extends Breeze> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		xpReward = 0;
	}

	public SummonedBreeze(Level pLevel, LivingEntity owner) {
		this(AASpells.Entities.SUMMONED_BREEZE.get(), pLevel);
		setSummoner(owner);
	}

	protected LivingEntity cachedSummoner;
	protected UUID summonerUUID;


	@Override
	public LivingEntity getSummoner() {
		return OwnerHelper.getAndCacheOwner(level(), cachedSummoner, summonerUUID);
	}

	public void setSummoner(@Nullable LivingEntity owner) {
		if (owner != null) {
			this.summonerUUID = owner.getUUID();
			this.cachedSummoner = owner;
		}
	}

	@Override
	public void onUnSummon() {
		if (!level().isClientSide) {
			MagicManager.spawnParticles(level(), ParticleTypes.POOF, getX(), getY(), getZ(), 25, .4, .8, .4, .03, false);
			discard();
		}
	}

	@Override
	public void tick() {
		if (this.getTargetFromBrain() != null) {
			Aeromancy.LOGGER.info("breeze brain target: {}", this.getTargetFromBrain().toString());
		}
		super.tick();
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(7, new GenericFollowOwnerGoal(this, this::getSummoner, 0.9f, 15, 5, false, 25));
		this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.targetSelector.addGoal(1, new BreezeBrainGoals.OwnerHurtByTarget(this, this::getSummoner));
		this.targetSelector.addGoal(2, new BreezeBrainGoals.OwnerHurtTarget(this, this::getSummoner));
		this.targetSelector.addGoal(3, new BreezeBrainGoals.CopyOwnerTarget(this, this::getSummoner));
		this.targetSelector.addGoal(4, (new GenericHurtByTargetGoal(this, (entity) -> entity == getSummoner())).setAlertOthers());
	}

	@Override
	public void die(DamageSource pDamageSource) {
		this.onDeathHelper();
		super.die(pDamageSource);
	}
	@Override
	public void onRemovedFromLevel() {
		super.onRemovedFromLevel();
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.summonerUUID = OwnerHelper.deserializeOwner(compoundTag);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		OwnerHelper.serializeOwner(compoundTag, summonerUUID);
	}
	@Override
	public boolean isAlliedTo(Entity pEntity) {
		return super.isAlliedTo(pEntity) || this.isAlliedHelper(pEntity);
	}

	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (shouldIgnoreDamage(pSource))
			return false;
		return super.hurt(pSource, pAmount);
	}
}
