package com.snackpirate.aeromancy.spells;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.updraft.UpdraftEntity;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.*;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@AutoSpellConfig
public class TelelinkSpell extends AbstractSpell {
	private final DefaultConfig defaultConfig;

	@Override
	public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
		return List.of(
				Component.translatable("ui.irons_spellbooks.max_victims", this.getRecastCount(spellLevel, caster)));
	}

	public TelelinkSpell() {
		this.defaultConfig = (new DefaultConfig())
				.setMinRarity(SpellRarity.RARE)
				.setSchoolResource(SchoolRegistry.ENDER_RESOURCE)
				.setMaxLevel(5)
				.setCooldownSeconds(30)
				.build();
		this.manaCostPerLevel = 20;
		this.baseSpellPower = 3;
		this.spellPowerPerLevel = 2;
		this.castTime = 0;
		this.baseManaCost = 80;
	}
	@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("telelink");
	}


	@Override
	public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
		return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 64, .15f);
	}


	@Override
	public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
		if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetEntityCastData) {
			var recasts = playerMagicData.getPlayerRecasts();
			if (!recasts.hasRecastForSpell(getSpellId())) {
				recasts.addRecast(new RecastInstance(getSpellId(), spellLevel, getRecastCount(spellLevel, entity), 80, castSource, new MultiTargetEntityCastData(targetEntityCastData.getTarget((ServerLevel) level))), playerMagicData);
			} else {
				var instance = recasts.getRecastInstance(this.getSpellId());
				if (instance != null && instance.getCastData() instanceof MultiTargetEntityCastData targetingData) {
					targetingData.addTarget(targetEntityCastData.getTargetUUID());
				}
			}
		}
		super.onCast(level, spellLevel, entity, castSource, playerMagicData);
	}

	@Override
	public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
		super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
		Level level = serverPlayer.level();
		Vec3 origin = serverPlayer.getEyePosition().add(serverPlayer.getForward().normalize().scale(0.20000000298023224));
		if (castDataSerializable instanceof MultiTargetEntityCastData targetingData) {
			serverPlayer.addEffect(new MobEffectInstance(AASpells.MobEffects.TELELINKED, getEffectDuration(recastInstance.getSpellLevel(), serverPlayer)));
			targetingData.getTargets().forEach((uuid) -> {
				LivingEntity target = (LivingEntity) ((ServerLevel) serverPlayer.level()).getEntity(uuid);
				if (target != null) {
					target.addEffect(new MobEffectInstance(AASpells.MobEffects.TELELINKED, getEffectDuration(recastInstance.getSpellLevel(), serverPlayer)));
				}
			});
		}
	}

	@Override
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}

	@Override
	public CastType getCastType() {
		return CastType.INSTANT;
	}

	@Override
	public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
		return 1 + spellLevel;
	}

	public int getEffectDuration(int spellLevel, LivingEntity entity) {
		return (int) ((3*spellLevel*getSpellPower(spellLevel, entity) + 12) * 20);
	}

	@Override
	public ICastDataSerializable getEmptyCastData() {
		return new MultiTargetEntityCastData();
	}
}
