package com.snackpirate.aeromancy.entity.armor;

import com.snackpirate.aeromancy.Aeromancy;
import com.snackpirate.aeromancy.item.WindmakerRobeItem;
import io.redspace.ironsspellbooks.item.armor.IArmorCapeProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WindmakerArmorModel extends GeoModel<WindmakerRobeItem> {
	@Override
	public ResourceLocation getModelResource(WindmakerRobeItem animatable) {
		return ResourceLocation.fromNamespaceAndPath(Aeromancy.MOD_ID, "geo/windmaker_robes.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(WindmakerRobeItem animatable) {
		return ResourceLocation.fromNamespaceAndPath(Aeromancy.MOD_ID, "textures/models/armor/windmaker_robes.png");
	}

	@Override
	public ResourceLocation getAnimationResource(WindmakerRobeItem animatable) {
		return ResourceLocation.fromNamespaceAndPath(Aeromancy.MOD_ID, "animations/wizard_armor_animation.json");
	}

}
