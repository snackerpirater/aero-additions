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
	public AAItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
		super(output, lookupProvider, blockTags);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(WIND_FOCUS).add(Items.BREEZE_ROD);
		tag(ModTags.SCHOOL_FOCUS).add(Items.BREEZE_ROD);
		tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("curios", "spellbook"))).add(AAItems.UPDRAFT_TOME.get());
		tag(TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("irons_spellbooks", "staff"))).add(AAItems.AIR_STAFF.get());
		tag(Tags.Items.ENCHANTABLES).add(AAItems.AIR_STAFF.get());
		tag(ItemTags.SWORD_ENCHANTABLE).add(AAItems.AIR_STAFF.get());
		tag(ItemTags.SHARP_WEAPON_ENCHANTABLE).add(AAItems.AIR_STAFF.get());

	}
}
