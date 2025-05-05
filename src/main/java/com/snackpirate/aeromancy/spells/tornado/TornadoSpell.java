package com.snackpirate.aeromancy.spells.tornado;

import com.snackpirate.aeromancy.AASounds;
import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.*;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

@AutoSpellConfig
public class TornadoSpell extends AbstractSpell {
	@Override
	public ResourceLocation getSpellResource() {
		return Aeromancy.id("tornado");
	}
	public TornadoSpell() {
		this.manaCostPerLevel = 100;
		this.baseSpellPower = 1;
		this.spellPowerPerLevel = 1;
		this.castTime = 100;
		this.baseManaCost = 300;
	}
	private final DefaultConfig defaultConfig = new DefaultConfig()
			.setMinRarity(SpellRarity.LEGENDARY)
			.setSchoolResource(AASpells.Schools.WIND_RESOURCE)
			.setMaxLevel(6)
			.setCooldownSeconds(120)
			.build();
	@Override
	public DefaultConfig getDefaultConfig() {
		return defaultConfig;
	}

	@Override
	public CastType getCastType() {
		return CastType.LONG;
	}
	@Override
	public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {

		HitResult raycast = Utils.raycastForEntity(level, entity, 16, true);
		Vec3 center = raycast.getLocation();
//		level.playSound(null, center.x, center.y, center.z, SoundRegistry.BLACK_HOLE_CAST.get(), SoundSource.AMBIENT, 4, 1);

		TornadoEntity tornado = new TornadoEntity(level, entity);
		tornado.setSize(getSize(spellLevel, entity));
		tornado.moveTo(center);
		level.addFreshEntity(tornado);

		super.onCast(level, spellLevel, entity, castSource, playerMagicData);
	}


	private float getSize(int spellLevel, LivingEntity entity) {
		return 4 + (2*spellLevel);
	}

	@Override
	public Optional<SoundEvent> getCastStartSound() {
		return AASounds.WIND_CAST_REVERSED.asOptional();
	}

	@Override
	public AnimationHolder getCastStartAnimation() {
		return SpellAnimations.CHARGE_ANIMATION;
	}

	@Override
	public AnimationHolder getCastFinishAnimation() {
		return SpellAnimations.FINISH_ANIMATION;
	}


	@Override
	public boolean stopSoundOnCancel() {
		return true;
	}
}
