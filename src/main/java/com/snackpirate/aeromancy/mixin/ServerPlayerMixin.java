package com.snackpirate.aeromancy.mixin;

import com.snackpirate.aeromancy.spells.AASpells;
import com.snackpirate.aeromancy.spells.wind_charge.MagicWindCharge;
import io.redspace.ironsspellbooks.capabilities.magic.SummonManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@Inject(method="onExplosionHit", at = @At(value = "TAIL"))
	public void aero_additions$magicWindChargeFallDmg(Entity entity, CallbackInfo ci) {
//		Aeromancy.LOGGER.info("explosion hit");
		ServerPlayer player = (ServerPlayer) (Object) this;
		if (entity instanceof MagicWindCharge windCharge) {
			player.setIgnoreFallDamageFromCurrentImpulse(windCharge.getOwner() != null && (windCharge.getOwner().is(player) || (SummonManager.getOwner(windCharge.getOwner())!=null && SummonManager.getOwner(windCharge.getOwner()).is(player))));
		}
	}
}