package com.snackpirate.aeromancy.spells;

import com.snackpirate.aeromancy.data.AADamageTypes;
import com.snackpirate.aeromancy.AASounds;
import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.data.AAItemTags;
import com.snackpirate.aeromancy.spells.airblast.AirblastSpell;
import com.snackpirate.aeromancy.spells.airstep.AirstepSpell;
import com.snackpirate.aeromancy.spells.asphyxiate.AsphyxiateSpell;
import com.snackpirate.aeromancy.spells.asphyxiate.BreathlessEffect;
import com.snackpirate.aeromancy.spells.dash.DashSpell;
import com.snackpirate.aeromancy.spells.feather_fall.FeatherFallSpell;
import com.snackpirate.aeromancy.spells.feather_fall.FlightEffect;
import com.snackpirate.aeromancy.spells.flush.FlushSpell;
import com.snackpirate.aeromancy.spells.summon_breeze.SummonBreezeSpell;
import com.snackpirate.aeromancy.spells.summon_breeze.SummonedBreeze;
import com.snackpirate.aeromancy.spells.tornado.TornadoEntity;
import com.snackpirate.aeromancy.spells.updraft.UpdraftEntity;
import com.snackpirate.aeromancy.spells.updraft.UpdraftSpell;
import com.snackpirate.aeromancy.spells.wind_blade.WindBladeProjectile;
import com.snackpirate.aeromancy.spells.wind_blade.WindBladeSpell;
import com.snackpirate.aeromancy.spells.wind_charge.MagicWindCharge;
import com.snackpirate.aeromancy.spells.wind_charge.WindChargeSpell;
import com.snackpirate.aeromancy.spells.wind_shield.WindShieldEffect;
import com.snackpirate.aeromancy.spells.wind_shield.WindShieldSpell;
import io.redspace.ironsspellbooks.api.attribute.MagicRangedAttribute;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AASpells {
	private static final DeferredRegister<AbstractSpell> SPELLS = DeferredRegister.create(SpellRegistry.SPELL_REGISTRY_KEY, Aeromancy.MOD_ID);

	private static Supplier<AbstractSpell> registerSpell(AbstractSpell spell) {
		return SPELLS.register(spell.getSpellName(), () -> spell);
	}
	public static void register(IEventBus eventBus) {
		SPELLS.register(eventBus);
		Entities.ENTITIES.register(eventBus);
		Schools.SCHOOLS.register(eventBus);
		Attributes.ATTRIBUTES.register(eventBus);
		MobEffects.MOB_EFFECTS.register(eventBus);
	}

	public static final Supplier<AbstractSpell> WIND_CHARGE = registerSpell(new WindChargeSpell());
	public static final Supplier<AbstractSpell> UPDRAFT = registerSpell(new UpdraftSpell());
	public static final Supplier<AbstractSpell> AIRSTEP = registerSpell(new AirstepSpell());
	public static final Supplier<AbstractSpell> ASPHYXIATE = registerSpell(new AsphyxiateSpell());
	public static final Supplier<AbstractSpell> FEATHER_FALL = registerSpell(new FeatherFallSpell());
	public static final Supplier<AbstractSpell> WIND_SHIELD = registerSpell(new WindShieldSpell());
	public static final Supplier<AbstractSpell> AIRBLAST = registerSpell(new AirblastSpell());
	public static final Supplier<AbstractSpell> WIND_BLADE = registerSpell(new WindBladeSpell());
	public static final Supplier<AbstractSpell> FLUSH = registerSpell(new FlushSpell());
	public static final Supplier<AbstractSpell> DASH = registerSpell(new DashSpell());
//	public static final Supplier<AbstractSpell> TORNADO = registerSpell(new TornadoSpell());
//	public static final Supplier<AbstractSpell> THUNDERCLAP = registerSpell(new ThunderclapSpell());
//	public static final Supplier<AbstractSpell> SUMMON_BREEZE = registerSpell(new SummonBreezeSpell());
//	public static final Supplier<AbstractSpell> TELELINK = registerSpell(new TelelinkSpell());
//	public static final Supplier<AbstractSpell> SHAPESHIFT = registerSpell(new ShapeshiftSpell());

	public static class Entities {
		private static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, Aeromancy.MOD_ID);
		private static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, Aeromancy.MOD_ID);

		public static final DeferredHolder<EntityType<?>, EntityType<MagicWindCharge>> MAGIC_WIND_CHARGE =
				ENTITIES.register("magic_wind_charge", () -> EntityType.Builder.<MagicWindCharge>of(MagicWindCharge::new, MobCategory.MISC)
						.sized(0.3125F, 0.3125F)
						.clientTrackingRange(64)
						.build(Aeromancy.id("magic_wind_charge").toString()));

		public static final DeferredHolder<EntityType<?>, EntityType<UpdraftEntity>> UPDRAFT_VISUAL_ENTITY = ENTITIES.register("updraft_visual", () -> EntityType.Builder.of(UpdraftEntity::new, MobCategory.MISC)
				.sized(0.5f,0.5f)
				.clientTrackingRange(64)
				.build(Aeromancy.id("updraft_visual").toString()));

		public static final DeferredHolder<EntityType<?>, EntityType<SummonedBreeze>> SUMMONED_BREEZE =
				ENTITIES.register("summoned_breeze", () -> EntityType.Builder.<SummonedBreeze>of(SummonedBreeze::new, MobCategory.CREATURE)
						.sized(0.6f, 1.77f)
						.clientTrackingRange(64)
						.build(Aeromancy.id("summoned_breeze").toString()));


		public static final DeferredHolder<EntityType<?>, EntityType<WindBladeProjectile>> WIND_BLADE_PROJECTILE =
				ENTITIES.register("wind_blade", () -> EntityType.Builder.<WindBladeProjectile>of(WindBladeProjectile::new, MobCategory.MISC)
						.sized(.5f, .5f)
						.clientTrackingRange(64)
						.build(Aeromancy.id("wind_blade").toString()));

		public static final DeferredHolder<EntityType<?>, EntityType<TornadoEntity>> TORNADO =
				ENTITIES.register("tornado", () -> EntityType.Builder.<TornadoEntity>of(TornadoEntity::new, MobCategory.MISC)
						.sized(10, 6)
						.clientTrackingRange(64)
						.build(Aeromancy.id("tornado").toString()));


	}
	public static class Schools {
		public static final ResourceLocation WIND_RESOURCE = Aeromancy.id("wind");
		private static final DeferredRegister<SchoolType> SCHOOLS = DeferredRegister.create(SchoolRegistry.SCHOOL_REGISTRY_KEY, Aeromancy.MOD_ID);


		public static final Supplier<SchoolType> WIND = registerSchool(new SchoolType(
				WIND_RESOURCE,
				AAItemTags.WIND_FOCUS,
				Component.translatable("school.aero_additions.wind").withColor(0xa3b6ff),
				Attributes.WIND_SPELL_POWER,
				Attributes.WIND_MAGIC_RESIST,
				AASounds.WIND_CAST,
				AADamageTypes.WIND_MAGIC
		));

		private static Supplier<SchoolType> registerSchool(SchoolType schoolType) {
			return SCHOOLS.register(schoolType.getId().getPath(), () -> schoolType);
		}
	}

	public static class Attributes {
		private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, Aeromancy.MOD_ID);
		public static final DeferredHolder<Attribute, Attribute> WIND_MAGIC_RESIST = newResistanceAttribute("wind");
		public static final DeferredHolder<Attribute, Attribute> WIND_SPELL_POWER = newPowerAttribute("wind");

		private static DeferredHolder<Attribute, Attribute> newResistanceAttribute(String id) {
			return ATTRIBUTES.register(id + "_magic_resist", () -> (new MagicRangedAttribute("attribute.aero_additions." + id + "_magic_resist", 1.0D, -100, 100).setSyncable(true)));
		}

		private static DeferredHolder<Attribute, Attribute> newPowerAttribute(String id) {
			return ATTRIBUTES.register(id + "_spell_power", () -> (new MagicRangedAttribute("attribute.aero_additions." + id + "_spell_power", 1.0D, -100, 100).setSyncable(true)));
		}
	}

	public static class MobEffects {
		public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Aeromancy.MOD_ID);

		public static final DeferredHolder<MobEffect, MobEffect> WIND_SHIELD = MOB_EFFECTS.register("wind_shield", () -> new WindShieldEffect(MobEffectCategory.BENEFICIAL, 0xd3ebea));
		public static final DeferredHolder<MobEffect, MobEffect> BREATHLESS = MOB_EFFECTS.register("breathless", () -> new BreathlessEffect(MobEffectCategory.HARMFUL, 0xd3ebea)
				.addAttributeModifier(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED,
						Aeromancy.id( "breathless_slow"),
						-0.3f,
						AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
				.addAttributeModifier(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE,
						Aeromancy.id("breathless_weak"),
						-0.3f,
						AttributeModifier.Operation.ADD_MULTIPLIED_BASE));


		public static final DeferredHolder<MobEffect, MobEffect> FLIGHT = MOB_EFFECTS.register("flight", () -> new FlightEffect(MobEffectCategory.BENEFICIAL, 0xd3ebea)
				.addAttributeModifier(net.minecraft.world.entity.ai.attributes.Attributes.WATER_MOVEMENT_EFFICIENCY, Aeromancy.id("effect.flight"), 1f, AttributeModifier.Operation.ADD_VALUE)
				.addAttributeModifier(net.minecraft.world.entity.ai.attributes.Attributes.MOVEMENT_SPEED, Aeromancy.id("effect.flight"), 0.15f, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
		public static final DeferredHolder<MobEffect, MobEffect> TELELINKED = MOB_EFFECTS.register("telelinked", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0xdb74ff));
		//marker signifying when to double jump
		public static final DeferredHolder<MobEffect, MobEffect> AIRSTEPPING = MOB_EFFECTS.register("airstepping", () -> new MagicMobEffect(MobEffectCategory.BENEFICIAL, 0xd3ebea));

        public static final DeferredHolder<MobEffect, StunEffect> STUNNED = MOB_EFFECTS.register("stunned", () -> new StunEffect(MobEffectCategory.HARMFUL, 0xFFFF00, ParticleTypes.ANGRY_VILLAGER));
    }

}
