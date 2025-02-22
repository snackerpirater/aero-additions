package com.snackpirate.aeromancy.spells.tornado;

import com.snackpirate.aeromancy.spells.AASpells;
import io.netty.buffer.ByteBuf;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import net.minecraft.client.renderer.blockentity.BellRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BellBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class TornadoEntity extends Projectile implements AntiMagicSusceptible, GeoAnimatable {
	//higher level = bigger nado
	//size in blocks
	private static final EntityDataAccessor<Float> DATA_SIZE = SynchedEntityData.defineId(TornadoEntity.class, EntityDataSerializers.FLOAT);
	//tornado variants
	private static final EntityDataAccessor<Integer> DATA_EFFECT = SynchedEntityData.defineId(TornadoEntity.class, EntityDataSerializers.INT);

	private static final int loopSoundDurationInTicks = 320;
	private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
	private final AnimationController<TornadoEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);


	private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> animationState) {
		animationState.getController().setAnimation(RawAnimation.begin().then("spin", Animation.LoopType.LOOP));
		return PlayState.CONTINUE;
	}

	public TornadoEntity(EntityType<? extends Projectile> entityType, Level level) {
		super(entityType, level);
	}

	public TornadoEntity(Level pLevel, LivingEntity owner) {
		this(AASpells.Entities.TORNADO.get(), pLevel);
		setOwner(owner);
	}

	public void setSize(float size) {
		if (!this.level().isClientSide) {
			this.getEntityData().set(DATA_SIZE, Math.min(size, 48));
		}
	}

	public float getSize() {
		return this.getEntityData().get(DATA_SIZE);
	}

	public void setEffect(TornadoEffect effect) {
		if (!this.level().isClientSide) {
			this.getEntityData().set(DATA_EFFECT, effect.id());
		}
	}

	public TornadoEffect getEffect() {
		return TornadoEffect.BY_ID.apply(this.getEntityData().get(DATA_EFFECT));
	}

	public void refreshDimensions() {
		double d0 = this.getX();
		double d1 = this.getY();
		double d2 = this.getZ();
		super.refreshDimensions();
		this.setPos(d0, d1, d2);
	}


	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		if (DATA_SIZE.equals(pKey)) {
			this.refreshDimensions();
			if (getSize() < .1f)
				this.discard();
		}

		super.onSyncedDataUpdated(pKey);
	}

	protected void addAdditionalSaveData(CompoundTag pCompound) {
		pCompound.putFloat("Size", this.getSize());
		pCompound.putInt("Age", this.tickCount);
		pCompound.putInt("Effect", this.getEffect().id());

		super.addAdditionalSaveData(pCompound);
	}

	protected void readAdditionalSaveData(CompoundTag pCompound) {
		this.tickCount = pCompound.getInt("Age");
		this.setEffect(TornadoEffect.BY_ID.apply(pCompound.getInt("Effect")));
		if (pCompound.getInt("Size") > 0)
			this.setSize(pCompound.getFloat("Size"));

		super.readAdditionalSaveData(pCompound);

	}

	@Override
	public void onAntiMagic(MagicData playerMagicData) {

	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(DATA_SIZE, 1f);
		builder.define(DATA_EFFECT, TornadoEffect.DEFAULT.id());
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(controller);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}

	@Override
	public double getTick(Object object) {
		return this.tickCount;
	}

	@Override
	public void onAddedToLevel() {
		super.onAddedToLevel();

	}

	@Override
	public boolean displayFireAnimation() {
		return false;
	}

	@Override
	public EntityDimensions getDimensions(Pose pPose) {
		return EntityDimensions.scalable(this.getSize() * 1.5F, this.getSize());
	}


	@Override
	public void tick() {
		super.tick();
		int update = Math.max((int) (getSize() / 2), 2);
		//prevent lag from giagantic black holes
		if (tickCount % update == 0) {
			updateTrackingEntities();
		}
		var bb = this.getBoundingBox();
		float radius = (float) (bb.getXsize());
		boolean hitTick = this.tickCount % 10 == 0;
		for (Entity entity : trackingEntities) {
			if (entity != getOwner() && !DamageSources.isFriendlyFireBetween(getOwner(), entity) && !entity.isSpectator()) {
				Vec3 center = bb.getCenter();
				float distance = (float) center.distanceTo(entity.position());
				if (distance > radius) {
					continue;
				}
				float f = 1 - distance / radius;
				float scale = f * f * f * f * .25f;
				float resistance = entity instanceof LivingEntity livingEntity ? Mth.clamp(1 - (float) livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), .3f, 1f) : 1f;
				float bossResistance = entity.getType().is(Tags.EntityTypes.BOSSES) ? 0.5f : 1f;


				Vec3 diff = center.subtract(entity.position().add(0,4,0)).scale(scale * resistance * bossResistance);
				entity.push(diff.x, diff.y, diff.z);
				entity.fallDistance = 0;
			}
		}
		if (!level().isClientSide) {
			if (tickCount > 20 * 16 * 2) {
				this.discard();
//				this.playSound(SoundRegistry.BLACK_HOLE_CAST.get(), getRadius() / 2f, 1);
//				MagicManager.spawnParticles(level(), ParticleHelper.UNSTABLE_ENDER, getX(), getY() + getRadius(), getZ(), 200, 1, 1, 1, 1, true);
			} else if ((tickCount - 1) % loopSoundDurationInTicks == 0) {
//				this.playSound(SoundRegistry.BLACK_HOLE_LOOP.get(), getRadius() / 3f, 1);
			}
		}
	}

	private void updateTrackingEntities() {
		trackingEntities = level().getEntities(this, this.getBoundingBox().inflate(1));
	}

	List<Entity> trackingEntities = new ArrayList<>();

	public enum TornadoEffect {
		DEFAULT(0),
		FIRE(1),
		ENDER(2),
		NATURE(3),
		LIGHTNING(4),
		ICE(5);
		public static final IntFunction<TornadoEffect> BY_ID = ByIdMap.continuous(TornadoEntity.TornadoEffect::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
		public static final StreamCodec<ByteBuf, TornadoEffect> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, TornadoEffect::id);
		private final int id;

		TornadoEffect(int id) {
			this.id = id;
		}

		public int id() {
			return this.id;
		}
	}
}
