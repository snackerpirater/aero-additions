package com.snackpirate.aeromancy.item;

import com.snackpirate.aeromancy.Aeromancy;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@mezz.jei.api.JeiPlugin
public class JeiCompat implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return Aeromancy.id("jei_compat");
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		registration.addItemStackInfo(new ItemStack(AAItems.UPDRAFT_TOME.get()), Component.translatable("item.aero_additions.updraft_tome.guide"));
	}
}
