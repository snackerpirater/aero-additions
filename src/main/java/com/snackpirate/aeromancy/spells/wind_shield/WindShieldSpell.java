package com.snackpirate.aeromancy.spells.wind_shield;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.player.Player;

public class WindShieldSpell extends AbstractSpell {
	@Override
	public ResourceLocation getSpellResource() {
		return null;
	}

	@Override
	public DefaultConfig getDefaultConfig() {
		return null;
	}

	@Override
	public CastType getCastType() {
		return CastType.LONG;
	}
}
