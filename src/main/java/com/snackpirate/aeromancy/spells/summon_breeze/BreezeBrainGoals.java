package com.snackpirate.aeromancy.spells.summon_breeze;

import com.snackpirate.aeromancy.Aeromancy;
import io.redspace.ironsspellbooks.entity.mobs.IMagicSummon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

import java.util.EnumSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/*
Needed since ISS' goals don't work for a reason
 */
public class BreezeBrainGoals {
	public static class OwnerHurtByTarget extends TargetGoal {

		private final SummonedBreeze entity;
		private final Supplier<LivingEntity> owner;
		private LivingEntity ownerLastHurtBy;
		private int timestamp;

		public OwnerHurtByTarget(SummonedBreeze entity, Supplier<LivingEntity> getOwner) {
			super(entity, false);
			this.entity = entity;
			this.owner = getOwner;
			this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		}

		@Override
		public boolean canUse() {
			LivingEntity owner = this.owner.get();
			if (owner == null) {
				return false;
			} else {
				this.ownerLastHurtBy = owner.getLastHurtByMob();
				if (ownerLastHurtBy == null || ownerLastHurtBy.isAlliedTo(mob))
					return false;
				int i = owner.getLastHurtByMobTimestamp();
				return i != this.timestamp && this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && !(this.ownerLastHurtBy instanceof IMagicSummon summon && summon.getSummoner() == owner);
			}
		}
		@Override
		public void start() {
			this.mob.setTarget(this.ownerLastHurtBy);
			this.mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, this.ownerLastHurtBy, 200L);
			LivingEntity owner = this.owner.get();
			if (owner != null) {
				this.timestamp = owner.getLastHurtByMobTimestamp();
			}

			super.start();
		}
	}

	public static class OwnerHurtTarget extends TargetGoal {

		private final SummonedBreeze entity;
		private final Supplier<LivingEntity> owner;
		private LivingEntity ownerLastHurt;
		private int timestamp;


		public OwnerHurtTarget(SummonedBreeze entity, Supplier<LivingEntity> ownerGetter) {
			super(entity, false);
			this.entity = entity;
			this.owner = ownerGetter;
			this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		}
		@Override
		public boolean canUse() {
			LivingEntity owner = this.owner.get();
			if (owner == null) {
				return false;
			} else {
				//mob.getLastHurtByMobTimestamp() == mob.tickCount - 1
				this.ownerLastHurt = owner.getLastHurtMob();
				int i = owner.getLastHurtMobTimestamp();


				return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT) && !(this.ownerLastHurt instanceof IMagicSummon summon && summon.getSummoner() == owner);
			}
		}
		@Override
		public void start() {
			this.mob.setTarget(this.ownerLastHurt);
			Aeromancy.LOGGER.debug("GenericOwnerHurtTargetGoal.start");
			Aeromancy.LOGGER.debug("Brain before: {}",this.mob.getBrain().getMemories().keySet().stream()
					.map(key -> key + "=" + this.mob.getBrain().getMemories().get(key))
					.collect(Collectors.joining(", ", "{", "}")));
			this.mob.getBrain().setMemoryWithExpiry(MemoryModuleType.NEAREST_ATTACKABLE, this.ownerLastHurt, 200L);
			Aeromancy.LOGGER.debug("Brain After: {}",this.mob.getBrain().getMemories().keySet().stream()
					.map(key -> key + "=" + this.mob.getBrain().getMemories().get(key))
					.collect(Collectors.joining(", ", "{", "}")));
			LivingEntity owner = this.owner.get();
			if (owner != null) {
				this.timestamp = owner.getLastHurtMobTimestamp();
			}

			super.start();
		}
	}

	public static class CopyOwnerTarget extends TargetGoal {

		private final Supplier<LivingEntity> ownerGetter;

		public CopyOwnerTarget(PathfinderMob pMob, Supplier<LivingEntity> ownerGetter) {
			super(pMob, false);
			this.ownerGetter = ownerGetter;

		}

		@Override
		public boolean canUse() {
			return ownerGetter.get() instanceof Mob owner && owner.getTarget() != null && !(owner.getTarget() instanceof IMagicSummon summon && summon.getSummoner() == owner);

		}
		@Override
		public void start() {
			var target = ((Mob) ownerGetter.get()).getTarget();
			mob.setTarget(target);
			this.mob.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 200L);

			super.start();
		}
	}
}
