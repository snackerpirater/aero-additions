package com.snackpirate.aeromancy.data;

import com.snackpirate.aeromancy.item.AAItems;
import com.snackpirate.aeromancy.Aeromancy;
import io.redspace.ironsspellbooks.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class AAItemTags extends ItemTagsProvider {
	public static final TagKey<Item> WIND_FOCUS = TagKey.create(Registries.ITEM, Aeromancy.id("wind_focus"));
	public static final TagKey<Item> FLIGHT_LIGHT = TagKey.create(Registries.ITEM, Aeromancy.id("flight_light"));
	public static final TagKey<Item> FLIGHT_HEAVY = TagKey.create(Registries.ITEM, Aeromancy.id("flight_heavy"));
	public AAItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
		super(output, lookupProvider, blockTags);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(WIND_FOCUS).add(Items.BREEZE_ROD);
		tag(ModTags.SCHOOL_FOCUS).add(Items.BREEZE_ROD);
		tag(ModTags.INSCRIBED_RUNES).add(AAItems.WIND_RUNE.get());
		tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "spellbook"))).add(AAItems.UPDRAFT_TOME.get());
		tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "staff"))).add(AAItems.AIR_STAFF.get());
		tag(Tags.Items.ENCHANTABLES).add(AAItems.AIR_STAFF.get());
		tag(ItemTags.SWORD_ENCHANTABLE).add(AAItems.AIR_STAFF.get());
		tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(AAItems.AIR_STAFF.get());
		tag(ItemTags.HEAD_ARMOR_ENCHANTABLE).add(AAItems.WINDMAKER_HEADPIECE.get());
		tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(AAItems.WINDMAKER_ROBES.get());
		tag(ItemTags.LEG_ARMOR_ENCHANTABLE).add(AAItems.WINDMAKER_SKIRT.get());
		tag(ItemTags.FOOT_ARMOR_ENCHANTABLE).add(AAItems.WINDMAKER_BOOTS.get());

		tag(ItemTags.HEAD_ARMOR).add(AAItems.WINDMAKER_HEADPIECE.get());
		tag(ItemTags.CHEST_ARMOR).add(AAItems.WINDMAKER_ROBES.get());
		tag(ItemTags.LEG_ARMOR).add(AAItems.WINDMAKER_SKIRT.get());
		tag(ItemTags.FOOT_ARMOR).add(AAItems.WINDMAKER_BOOTS.get());

		tag(ItemTags.SWORDS).add(AAItems.WIND_SWORD.get());
		tag(Tags.Items.MELEE_WEAPON_TOOLS).add(AAItems.WIND_SWORD.get());
		
		tag(FLIGHT_LIGHT).add(
				AAItems.WINDMAKER_HEADPIECE.get(),
				AAItems.WINDMAKER_ROBES.get(),
				AAItems.WINDMAKER_SKIRT.get(),
				AAItems.WINDMAKER_BOOTS.get(),
				AAItems.AIR_STAFF.get(),
				AAItems.WIND_SWORD.get()
		);
		tag(FLIGHT_HEAVY).add(
				Items.IRON_HELMET,
				Items.IRON_CHESTPLATE,
				Items.IRON_LEGGINGS,
				Items.IRON_BOOTS,

				Items.GOLDEN_HELMET,
				Items.GOLDEN_CHESTPLATE,
				Items.GOLDEN_LEGGINGS,
				Items.GOLDEN_BOOTS,

				Items.DIAMOND_HELMET,
				Items.DIAMOND_CHESTPLATE,
				Items.DIAMOND_LEGGINGS,
				Items.DIAMOND_BOOTS,

				Items.NETHERITE_HELMET,
				Items.NETHERITE_CHESTPLATE,
				Items.NETHERITE_LEGGINGS,
				Items.NETHERITE_BOOTS);
	}
}
