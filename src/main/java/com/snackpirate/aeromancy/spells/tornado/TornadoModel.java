package com.snackpirate.aeromancy.spells.tornado;

import com.snackpirate.aeromancy.Aeromancy;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class TornadoModel extends DefaultedEntityGeoModel<TornadoEntity> {

	public TornadoModel() {
		super(Aeromancy.id("tornado"));
	}


	@Override
	public ResourceLocation getModelResource(TornadoEntity animatable) {
		return Aeromancy.id("geo/tornado.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(TornadoEntity animatable) {
		return Aeromancy.id("textures/entity/tornado.png");
	}

	@Override
	public ResourceLocation getAnimationResource(TornadoEntity animatable) {
		return Aeromancy.id("animations/tornado.animation.json");
	}
}
