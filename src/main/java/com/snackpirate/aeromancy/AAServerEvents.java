package com.snackpirate.aeromancy;

import be.florens.expandability.api.EventResult;
import be.florens.expandability.api.forge.PlayerSwimEvent;
import com.snackpirate.aeromancy.data.AAEntityTypeTags;
import com.snackpirate.aeromancy.network.AeromancySpellData;
import com.snackpirate.aeromancy.spells.AASpells;
import com.snackpirate.aeromancy.spells.summon_breeze.SummonedBreeze;
import io.redspace.ironsspellbooks.api.events.SpellOnCastEvent;
import io.redspace.ironsspellbooks.api.events.SpellTeleportEvent;
import io.redspace.ironsspellbooks.capabilities.magic.PocketDimensionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import static com.snackpirate.aeromancy.spells.wind_shield.WindShieldSpell.chanceToDeflect;

@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AAServerEvents {
	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, AASpells.Attributes.WIND_SPELL_POWER);
		event.add(EntityType.PLAYER, AASpells.Attributes.WIND_MAGIC_RESIST);
	}
	@SubscribeEvent
	public static void onAttributeCreate(EntityAttributeCreationEvent event) {
		event.put(AASpells.Entities.SUMMONED_BREEZE.get(), SummonedBreeze.createAttributes().build());
	}

	@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
	public static class Game {
		@SubscribeEvent
		public static void windShieldDeflection(ProjectileImpactEvent event) {
			if (event.getRayTraceResult() instanceof EntityHitResult result && result.getEntity() instanceof LivingEntity entity) {
				//level 1: 30
				//level 2: 40
				//level 3: 50
				//level 4: 60
				//level 5: 70
				if (entity.hasEffect(AASpells.MobEffects.WIND_SHIELD) && !event.getProjectile().getType().is(AAEntityTypeTags.REFLECTION_IMMUNE)) {
					if (entity.getRandom().nextFloat() < chanceToDeflect(entity.getEffect(AASpells.MobEffects.WIND_SHIELD).getAmplifier())) {
						event.setCanceled(true);
						entity.level().playSound(null, entity, SoundEvents.BREEZE_DEFLECT, entity.getSoundSource(), 1f, 1f);
						event.getProjectile().deflect(ProjectileDeflection.REVERSE, entity, entity, entity instanceof Player);

					}
				}
			}
		}

		@SubscribeEvent
		public static void onStartTracking(final PlayerEvent.StartTracking event) {
			if (event.getEntity() instanceof ServerPlayer serverPlayer && event.getTarget() instanceof ServerPlayer targetPlayer) {
				AeromancySpellData.getAeromancyData(serverPlayer).syncToPlayer(targetPlayer);
			}
		}
		@SubscribeEvent
		public static void onRespawn(final PlayerEvent.PlayerRespawnEvent event) {
			if (event.getEntity() instanceof ServerPlayer serverPlayer) {
				AeromancySpellData.getAeromancyData(serverPlayer).syncToPlayer(serverPlayer);
			}
		}

		@SubscribeEvent
		public static void onChangeDim(final PlayerEvent.PlayerChangedDimensionEvent event) {
			if (event.getEntity() instanceof ServerPlayer serverPlayer) {
				AeromancySpellData.getAeromancyData(serverPlayer).syncToPlayer(serverPlayer);
			}

		}


		@SubscribeEvent
		public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
			if (event.getEntity() instanceof ServerPlayer serverPlayer) {
				AeromancySpellData.getAeromancyData(serverPlayer).syncToPlayer(serverPlayer);
			}
		}

		@SubscribeEvent
		public static void activateFlight(PlayerSwimEvent event) {
			if (event.getEntity().hasEffect(AASpells.MobEffects.FLIGHT)) event.setResult(EventResult.SUCCESS);
		}

		@SubscribeEvent
		public static void telelinkTeleport(SpellTeleportEvent event) {
			Aeromancy.LOGGER.info("spell teleport event");
			if (event.getEntity() instanceof LivingEntity entity && entity.hasEffect(AASpells.MobEffects.TELELINKED)) {
//				Aeromancy.LOGGER.info("spell teleport event 2");
				event.getEntity().level().getEntities(EntityTypeTest.forClass(LivingEntity.class), event.getEntity().getBoundingBox().inflate(32), (living) -> living.hasEffect(AASpells.MobEffects.TELELINKED))
						.forEach((livingEntity) -> {
//							Aeromancy.LOGGER.info("spell teleport event 3");
							livingEntity.teleportTo(
									event.getTargetX() + livingEntity.getX() - event.getPrevX(),
									event.getTargetY() + livingEntity.getY() - event.getPrevY(),
									event.getTargetZ() + livingEntity.getZ() - event.getPrevZ());
						});
			}
		}
		@SubscribeEvent
		public static void telelinkPocketDim(SpellOnCastEvent event) {
			if (event.getSpellId().equals("irons_spellbooks:pocket_dimension")) {
				if (event.getEntity() instanceof ServerPlayer serverPlayer && serverPlayer.hasEffect(AASpells.MobEffects.TELELINKED)) {
					BlockPos portalPos = PocketDimensionManager.INSTANCE.findPortalForStructure(serverPlayer.serverLevel(), PocketDimensionManager.INSTANCE.structurePosForPlayer(event.getEntity()));
//					Aeromancy.LOGGER.info("spell teleport event 2");
					event.getEntity().level().getEntities(EntityTypeTest.forClass(LivingEntity.class), event.getEntity().getBoundingBox().inflate(32), (living) -> living.hasEffect(AASpells.MobEffects.TELELINKED) && !living.equals(event.getEntity()))
							.forEach((livingEntity) -> {
//								Aeromancy.LOGGER.info("spell teleport event 3");
								livingEntity.changeDimension(new DimensionTransition(serverPlayer.getServer().getLevel(PocketDimensionManager.POCKET_DIMENSION), portalPos.getBottomCenter(), Vec3.ZERO, 180, serverPlayer.getXRot(), DimensionTransition.DO_NOTHING));

				});
			}
		} else if (event.getSpellId().equals("irons_spellbooks:recall")) {
				if (event.getEntity() instanceof ServerPlayer serverPlayer && serverPlayer.hasEffect(AASpells.MobEffects.TELELINKED)) {
//					Aeromancy.LOGGER.info("spell teleport event 2");
					event.getEntity().level().getEntities(EntityTypeTest.forClass(LivingEntity.class), event.getEntity().getBoundingBox().inflate(32), (living) -> living.hasEffect(AASpells.MobEffects.TELELINKED) && !living.equals(event.getEntity()))
							.forEach((livingEntity) -> {
								livingEntity.changeDimension(serverPlayer.findRespawnPositionAndUseSpawnBlock(true, DimensionTransition.DO_NOTHING));
							});
				}
		}
	}
}}
