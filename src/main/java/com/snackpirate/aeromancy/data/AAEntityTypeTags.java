package com.snackpirate.aeromancy.data;

import com.snackpirate.aeromancy.Aeromancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AAEntityTypeTags extends EntityTypeTagsProvider {
	public static final TagKey<EntityType<?>> REFLECTION_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, Aeromancy.id("reflection_immune"));
	public static final TagKey<EntityType<?>> ASPHYXIATION_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, Aeromancy.id("asphyxiation_immune"));
	public AAEntityTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, provider, modId, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(REFLECTION_IMMUNE).add(EntityType.LLAMA_SPIT);
		tag(ASPHYXIATION_IMMUNE).add(EntityType.DROWNED);
	}
}
