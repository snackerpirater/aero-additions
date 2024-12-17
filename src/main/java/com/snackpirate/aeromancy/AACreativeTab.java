package com.snackpirate.aeromancy;

import com.snackpirate.aeromancy.item.AAItems;
import com.snackpirate.aeromancy.spells.AASpells;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.registries.CreativeTabRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AACreativeTab {
	private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Aeromancy.MOD_ID);


	public static void register(IEventBus eventBus) {
		TABS.register(eventBus);
	}
	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = TABS.register("spellbook_equipment", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup." + Aeromancy.MOD_ID + ".main_tab"))
			.icon(() -> new ItemStack(AAItems.UPDRAFT_TOME))
			.displayItems((enabledFeatures, entries) -> {
				entries.accept(AAItems.WIND_RUNE.get());
				entries.accept(AAItems.WIND_UPGRADE_ORB.get());
				entries.accept(AAItems.UPDRAFT_TOME.get());
				entries.accept(AAItems.AIR_STAFF.get());
				entries.accept(AAItems.WINDMAKER_HEADPIECE.get());
				entries.accept(AAItems.WINDMAKER_ROBES.get());
				entries.accept(AAItems.WINDMAKER_SKIRT.get());
				entries.accept(AAItems.WINDMAKER_BOOTS.get());
			})
			.withTabsBefore(CreativeTabRegistry.EQUIPMENT_TAB.getId())
			.build());

	@SubscribeEvent
	public static void fillCreativeTabs(final BuildCreativeModeTabContentsEvent event) {
		if (/*event.getTab() == CreativeModeTabs.searchTab() || */event.getTab() == MAIN_TAB.get()) {
			SpellRegistry.getEnabledSpells().stream()
					.filter(spellType -> spellType != SpellRegistry.none() && spellType.getSchoolType().equals(AASpells.Schools.WIND.get()))
					.forEach(spell -> {
						for (int i = spell.getMinLevel(); i <= spell.getMaxLevel(); i++) {
							var itemstack = new ItemStack(ItemRegistry.SCROLL.get());
							var spellList = ISpellContainer.createScrollContainer(spell, i, itemstack);
//							spellList.save(itemstack);
							event.accept(itemstack);
						}
					});
		}
	}
}
