package com.snackpirate.aeromancy.mixin;

import com.snackpirate.aeromancy.spells.AASpells;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class) //this should probably only affect players
public abstract class PlayerMixin  {

	@Inject(method="updateSwimming", at=@At("TAIL"), cancellable = true)
	public void flightSwimming(CallbackInfo ci) {
		if ((Object) this instanceof LivingEntity entity) {
			if (entity.isSprinting() && entity.hasEffect(AASpells.MobEffects.FLIGHT)) {
				entity.setSwimming(true);
				ci.cancel();
			}
		}
	}
}
