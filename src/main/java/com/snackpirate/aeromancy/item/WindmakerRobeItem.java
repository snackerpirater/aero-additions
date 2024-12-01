package com.snackpirate.aeromancy.item;

import com.snackpirate.aeromancy.entity.armor.WindmakerArmorModel;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.entity.armor.GenericCustomArmorRenderer;
import io.redspace.ironsspellbooks.item.armor.ImbuableChestplateArmorItem;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.registries.ArmorMaterialRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ElytraItem;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WindmakerRobeItem extends ImbuableChestplateArmorItem {
	public WindmakerRobeItem(Holder<ArmorMaterial> pMaterial, Type pType, Properties pProperties, AttributeContainer... attributes) {
		super(pMaterial, pType, pProperties, attributes);
	}
	public WindmakerRobeItem(ArmorItem.Type slot, Properties settings) {
		super(ArmorMaterialRegistry.SCHOOL, slot, settings, schoolAttributes(AASpells.Attributes.WIND_SPELL_POWER));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public GeoArmorRenderer<?> supplyRenderer() {
		return new GenericCustomArmorRenderer<>(new WindmakerArmorModel());
	}
}
