package com.snackpirate.aeromancy.spells.wind_blade;

import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

import java.util.Optional;

public class WindBladeProjectile extends AbstractMagicProjectile implements IEntityWithComplexSpawn {
	@Override
	public void trailParticles() {
		for (int i = 0; i < 1; i++) {
			double speed = .05;
			double dx = Utils.random.nextDouble() * 2 * speed - speed;
			double dy = Utils.random.nextDouble() * 2 * speed - speed;
			double dz = Utils.random.nextDouble() * 2 * speed - speed;
			level().addParticle(Utils.random.nextDouble() < .3 ? ParticleTypes.SPIT : ParticleTypes.POOF, this.getX() + dx, this.getY() + dy, this.getZ() + dz, dx, dy, dz);
		}
	}



    @Override
	public void impactParticles(double x, double y, double z) {
		MagicManager.spawnParticles(this.level(), ParticleTypes.SPIT, x, y, z, 5, .1, .1, .1, .25, true);
	}

	@Override
	public float getSpeed() {
		return 1.75f;
	}

	public WindBladeProjectile(Level levelIn, LivingEntity shooter) {
		this(AASpells.Entities.WIND_BLADE_PROJECTILE.get(), levelIn);
		setOwner(shooter);
	}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        super.defineSynchedData(pBuilder);
    }

    @Override
	public Optional<Holder<SoundEvent>> getImpactSound() {
		return Optional.of(SoundEvents.WIND_CHARGE_BURST);
	}


	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		discard();
	}


	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		super.onHitEntity(entityHitResult);
		var target = entityHitResult.getEntity();
		if (target instanceof LivingEntity livingTarget) livingTarget.knockback(this.getDamage(), this.getX() - target.getX(), this.getZ() - target.getZ());
		DamageSources.applyDamage(target, getDamage(), AASpells.WIND_BLADE.get().getDamageSource(this, getOwner()));
		discard();
	}

	public WindBladeProjectile(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

}
