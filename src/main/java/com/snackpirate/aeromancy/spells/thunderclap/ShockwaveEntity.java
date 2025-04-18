package com.snackpirate.aeromancy.spells.thunderclap;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.AbstractMagicProjectile;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class ShockwaveEntity extends AbstractMagicProjectile implements AntiMagicSusceptible {
	@Override
	public void trailParticles() {
	}

	@Override
	public void impactParticles(double x, double y, double z) {
	}
	@Override
	public float getSpeed() {
		return 0;
	}

	@Override
	public Optional<Holder<SoundEvent>> getImpactSound() {
		return Optional.empty();
	}

	protected ShockwaveEntity(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

	@Override
	public void onAntiMagic(MagicData playerMagicData) {
		super.onAntiMagic(playerMagicData);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {

	}
}
