package com.snackpirate.aeromancy.spells.updraft;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class UpdraftEntity extends Entity {

	public UpdraftEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
		this.setXRot(-90);
		this.setYRot(0);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {}

	@Override
	public void tick() {
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();
		if (this.tickCount > 8) {
			this.discard();
		} else {
			super.tick();
		}

		this.setPosRaw(x, y, z);
	}
}
