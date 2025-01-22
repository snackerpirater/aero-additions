package com.snackpirate.aeromancy.mixin;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.spells.AASpells;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@Inject(method="onExplosionHit", at = @At(value = "TAIL"), cancellable = false)
	public void onExplosionHit(Entity entity, CallbackInfo ci) {
//		Aeromancy.LOGGER.info("explosion hit");
		ServerPlayer player = (ServerPlayer) (Object) this;
		player.setIgnoreFallDamageFromCurrentImpulse(entity != null && (entity.getType() == EntityType.WIND_CHARGE || entity.getType() == AASpells.Entities.MAGIC_WIND_CHARGE.get()));
	}
}
