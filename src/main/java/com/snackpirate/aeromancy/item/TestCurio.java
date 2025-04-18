package com.snackpirate.aeromancy.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class TestCurio extends Item implements ICurioItem {
	public TestCurio(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canEquip(SlotContext slotContext, ItemStack stack) {
		return CuriosApi.getCuriosInventory(slotContext.entity()).flatMap(curiosInventory -> curiosInventory.findFirstCurio(this)).isEmpty();
	}

}
