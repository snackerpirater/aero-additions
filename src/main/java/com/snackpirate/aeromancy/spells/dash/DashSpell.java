package com.snackpirate.aeromancy.spells.dash;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.capabilities.magic.ImpulseCastData;
import io.redspace.ironsspellbooks.network.particles.TeleportParticlesPacket;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@AutoSpellConfig
public class DashSpell extends AbstractSpell {
	@Override
	public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
		return List.of(
				Component.translatable("ui.irons_spellbooks.strength", String.format("%s%%", (int) ((15 + getSpellPower(spellLevel, caster)) / 12f * 100 / 2.08f))));
	}

	private final DefaultConfig defaultConfig = new DefaultConfig()
			.setMinRarity(SpellRarity.UNCOMMON)
			.setSchoolResource(AASpells.Schools.WIND_RESOURCE)
			.setMaxLevel(5)
			.setCooldownSeconds(2)
			.build();
	@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("dash");
	}

	public DashSpell() {
		this.baseSpellPower = 10;
		this.spellPowerPerLevel = 5;
		this.baseManaCost = 20;
		this.manaCostPerLevel = 2;
		this.castTime = 0;
	}

	@Override
	public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
		if (castData instanceof ImpulseCastData bdcd) {
			entity.hasImpulse = bdcd.hasImpulse;
			entity.setDeltaMovement(entity.getDeltaMovement().add(bdcd.x, bdcd.y, bdcd.z));
		}

		super.onClientCast(level, spellLevel, entity, castData);
	}

	@Override
	public ICastDataSerializable getEmptyCastData() {
		return new ImpulseCastData();
	}
	//q.v. BurningDash
	@Override
	public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
		entity.hasImpulse = true;
		float multiplier = (15 + getSpellPower(spellLevel, entity)) / 12f;

		//Direction for Mobs to cast in
		Vec3 forward = entity.getLookAngle();
		//Create Dashing Movement Impulse
		var vec = forward.multiply(2, 1, 2).normalize().scale(multiplier);
		//Start Spin Attack
		if (entity.onGround()) {
			entity.setPos(entity.position().add(0, 1.5, 0));
			vec.add(0, 0.25, 0);
		}
		playerMagicData.setAdditionalCastData(new ImpulseCastData((float) vec.x, (float) vec.y, (float) vec.z, true));
		//entity.setDeltaMovement(entity.getDeltaMovement().add(vec));
		entity.setDeltaMovement(new Vec3(
				Mth.lerp(.75f, entity.getDeltaMovement().x, vec.x),
				Mth.lerp(.75f, entity.getDeltaMovement().y, vec.y),
				Mth.lerp(.75f, entity.getDeltaMovement().z, vec.z)
		));
		if (entity instanceof Player p) p.setIgnoreFallDamageFromCurrentImpulse(true);

		PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new DashParticlesPacket(entity.position(), vec));
		entity.invulnerableTime = 20;
		//startSpinAttack(entity, 10);
		super.onCast(world, spellLevel, entity, castSource, playerMagicData);
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
	public AnimationHolder getCastFinishAnimation() {
		return SpellAnimations.SELF_CAST_ANIMATION;
	}
}
