package com.snackpirate.aeromancy.data;

import com.snackpirate.aeromancy.Aeromancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AADamageTypeTags extends TagsProvider<DamageType> {


	public AADamageTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
		super(output, Registries.DAMAGE_TYPE, lookupProvider, Aeromancy.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.@NotNull Provider provider) {
//		this.tag(DamageTypeTags.NO_IMPACT).add(AADamageTypes.ASPHYXIATION);
	}
}
