package com.snackpirate.aeromancy;

import com.snackpirate.aeromancy.spells.AASpells;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

import static com.snackpirate.aeromancy.spells.wind_shield.WindShieldSpell.chanceToDeflect;

@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AAServerEvents {
	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, AASpells.Attributes.WIND_SPELL_POWER);
		event.add(EntityType.PLAYER, AASpells.Attributes.WIND_MAGIC_RESIST);
	}

	@EventBusSubscriber(modid=Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.GAME)
	public static class Game {
		@SubscribeEvent
		public static void windShieldDeflection(ProjectileImpactEvent event) {
			if (event.getRayTraceResult() instanceof EntityHitResult result && result.getEntity() instanceof LivingEntity entity) {
				//level 1: 30
				//level 2: 40
				//level 3: 50
				//level 4: 60
				//level 5: 70
				if (entity.hasEffect(AASpells.MobEffects.WIND_SHIELD)) {
					if (entity.getRandom().nextFloat() < chanceToDeflect(entity.getEffect(AASpells.MobEffects.WIND_SHIELD).getAmplifier())) {
						event.setCanceled(true);
						entity.level().playSound(null, entity, SoundEvents.BREEZE_DEFLECT, entity.getSoundSource(), 1f, 1f);
						event.getProjectile().deflect(ProjectileDeflection.REVERSE, entity, entity, entity instanceof Player);
					}
				}
			}
		}


	}
}
