package com.snackpirate.aeromancy.spells.tornado;

import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.spells.dragon_breath.DragonBreathPool;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
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
	public @NotNull EntityDimensions getDimensions(Pose pPose) {
		return EntityDimensions.scalable(this.getSize() * 1.5F, this.getSize());
	}


	@Override
	public void tick() {
		super.tick();
		int update = Math.max((int) (getSize() / 2), 2);
		//prevent lag from giagantic 'nados
		if (tickCount % update == 0) {
//			Aeromancy.LOGGER.info("update trackin");
			updateTrackingEntities();
		}
		var bb = this.getBoundingBox();
		float radius = (float) (bb.getXsize());
		boolean hitTick = this.tickCount % 10 == 0;
		for (Entity entity : trackingEntities) {
			if (
					entity != getOwner() &&  !DamageSources.isFriendlyFireBetween(getOwner(), entity) &&
							!entity.isSpectator()) {
				//tornado suction:
				//the closer it gets to the tornado column, the greater upwards velocity
				//the higher in the tornado column, the greater spin velocity
				//once it gets to the top, eject
				//andd there's also like latent inwards suction i guess
				Vec3 centerFromY = bb.getBottomCenter();
				centerFromY = new Vec3(centerFromY.x, entity.getY(), centerFromY.z);
				float distanceToColumn = (float) centerFromY.distanceTo(entity.position());
				if (distanceToColumn > radius) {
					continue;
				}
				if (distanceToColumn < 2) {
					checkForApplicableEffects(entity);
				}
				if (hitTick && entity instanceof LivingEntity living && distanceToColumn < 2) applyDamage(living);
				float f = 1 - distanceToColumn / radius;

				float heightInColumn = Math.max((float) (entity.getY() - bb.getMinPosition().y),1)/3;
				float scale = f * f * f * f * .25f;
				Vec3 inwardsSuction = centerFromY.subtract(entity.position()).normalize().scale(scale*heightInColumn);
				Vec3 spinny = inwardsSuction.yRot(270 * Mth.DEG_TO_RAD).scale(scale*2*heightInColumn);
//				if (distanceToColumn < 1) inwardsSuction = inwardsSuction.multiply(0, 1, 0);
				//if we are at the top of the tornado, crank up the spinning to eject
				if (entity.position().y > bb.getMaxPosition().y-1.5) {
					spinny = spinny.scale(4);
					if (entity instanceof LivingEntity living && !living.hasEffect(MobEffectRegistry.AIRBORNE)) {
						living.addEffect(new MobEffectInstance(MobEffectRegistry.AIRBORNE, 200, (int)getSize()));
					}
				}
				float resistance = entity instanceof LivingEntity livingEntity ? Mth.clamp(1 - (float) livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), .3f, 1f) : 1f;
				float bossResistance = entity.getType().is(Tags.EntityTypes.BOSSES) ? 0.5f : 1f;
				Vec3 pushForce = new Vec3(0, scale * resistance * bossResistance * 0.7, 0).add(inwardsSuction).add(spinny);
				entity.push(pushForce);
				entity.fallDistance = 0;
			}
		}
		if (!level().isClientSide) {
			if (tickCount > 20 * 160 * 2) {
				this.discard();
//				this.playSound(SoundRegistry.BLACK_HOLE_CAST.get(), getRadius() / 2f, 1);
//				MagicManager.spawnParticles(level(), ParticleHelper.UNSTABLE_ENDER, getX(), getY() + getRadius(), getZ(), 200, 1, 1, 1, 1, true);
			} else if ((tickCount - 1) % loopSoundDurationInTicks == 0) {
//				this.playSound(SoundRegistry.BLACK_HOLE_LOOP.get(), getRadius() / 3f, 1);

			}

		}
	}
	public void applyDamage(LivingEntity e) {
		switch (this.getEffect()) {
			case DEFAULT -> {}
			case FIRE -> {
				DamageSources.applyDamage(e, 2, SpellRegistry.FIRE_BREATH_SPELL.get().getDamageSource(this, getOwner()));
			}
			case ENDER -> {
				DamageSources.applyDamage(e, 2, SpellRegistry.DRAGON_BREATH_SPELL.get().getDamageSource(this, getOwner()));
			}
			case NATURE -> {
				if (DamageSources.applyDamage(e, 2, SpellRegistry.POISON_BREATH_SPELL.get().getDamageSource(this, getOwner())) && e instanceof LivingEntity livingEntity) livingEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
			}
			case LIGHTNING -> {
				DamageSources.applyDamage(e, 2, SpellRegistry.ELECTROCUTE_SPELL.get().getDamageSource(this, getOwner()));
				if (!level().isClientSide()) MagicManager.spawnParticles(level(), ParticleHelper.ELECTRICITY, e.getX(), e.getY() + e.getBbHeight() / 2, e.getZ(), 10, e.getBbWidth() / 3, e.getBbHeight() / 3, e.getBbWidth() / 3, 0.1, false);
			}
		}
	}
	public void checkForApplicableEffects(Entity e) {
		for (TornadoEffect effect: TornadoEffect.values()) {
			if (e.getType().equals(effect.entity)) {
				this.setEffect(effect);
			}
		}
	}
	private void updateTrackingEntities() {
		trackingEntities = level().getEntities(this, this.getBoundingBox().inflate(1));
//		Aeromancy.LOGGER.info(trackingEntities.toString());
	}

	List<Entity> trackingEntities = new ArrayList<>();

	public enum TornadoEffect {
		DEFAULT(0,   AASpells.Entities.MAGIC_WIND_CHARGE.get(),     AASpells.WIND_CHARGE.get()),
		FIRE(1,      EntityRegistry.FIRE_BREATH_PROJECTILE.get(),   SpellRegistry.FIRE_BREATH_SPELL.get()),
		ENDER(2,     EntityRegistry.DRAGON_BREATH_PROJECTILE.get(), SpellRegistry.DRAGON_BREATH_SPELL.get()),
		NATURE(3,    EntityRegistry.POISON_BREATH_PROJECTILE.get(), SpellRegistry.POISON_BREATH_SPELL.get()),
		LIGHTNING(4, EntityRegistry.ELECTROCUTE_PROJECTILE.get(),   SpellRegistry.ELECTROCUTE_SPELL.get());
		public static final IntFunction<TornadoEffect> BY_ID = ByIdMap.continuous(TornadoEffect::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
		private final int id;
		private final EntityType<?> entity;
		private final AbstractSpell spell;

		TornadoEffect(int id, EntityType<?> entity, AbstractSpell spell) {
			this.id = id;
			this.entity = entity;
			this.spell = spell;
		}

		public int id() {
			return this.id;
		}
		public EntityType<?> entity() {
			return this.entity;
		}
		public AbstractSpell spell() {
			return this.spell;
		}
	}
}
