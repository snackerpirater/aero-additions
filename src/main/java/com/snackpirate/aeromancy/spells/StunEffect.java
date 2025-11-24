package com.snackpirate.aeromancy.spells;

import com.snackpirate.aeromancy.Aeromancy;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.PlayerCooldowns;
import io.redspace.ironsspellbooks.effect.ISyncedMobEffect;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = Aeromancy.MOD_ID)
public class StunEffect extends MobEffect implements ISyncedMobEffect {
    protected StunEffect(MobEffectCategory category, int color, ParticleOptions particle) {
        super(category, color, particle);
    }

    //prevent stunlocking
    @SubscribeEvent
    public static void tryApply(MobEffectEvent.Applicable event) {
        LivingEntity entity = event.getEntity();
        if (entity.hasEffect(AASpells.MobEffects.STUNNED)) event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
    }

    @SubscribeEvent
    public static void onApply(MobEffectEvent.Added event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player p && event.getEffectInstance() != null && event.getEffectInstance().is(AASpells.MobEffects.STUNNED)) {
            MagicData magicData = MagicData.getPlayerMagicData(entity);
            SpellSelectionManager selections = new SpellSelectionManager(p);
            PlayerCooldowns cooldowns = magicData.getPlayerCooldowns();
            selections.getAllSpells().forEach(selectionOption -> {
                AbstractSpell spell = selectionOption.spellData.getSpell();
//                Aeromancy.LOGGER.info("stun effect cd {}", spell.getSpellResource().toString());
                //ignores CDR because epic
                if (!cooldowns.isOnCooldown(spell)) cooldowns.addCooldown(spell, spell.getSpellCooldown()/2);

            });
            if (p instanceof ServerPlayer sp) cooldowns.syncToPlayer(sp);
        }
    }
}
