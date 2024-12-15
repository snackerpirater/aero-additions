package com.snackpirate.aeromancy.data;

import com.snackpirate.aeromancy.item.AAItems;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class AARecipes extends RecipeProvider {

	public AARecipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput recipeOutput) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AAItems.WINDMAKER_HEADPIECE.get(), 1)
				.pattern("CCC")
				.pattern("CRC")
				.define('C', ItemRegistry.MAGIC_CLOTH.get())
				.define('R', AAItems.WIND_RUNE.get())
				.unlockedBy("has_rune", has(AAItems.WIND_RUNE.get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AAItems.WINDMAKER_ROBES.get(), 1)
				.pattern("CRC")
				.pattern("CCC")
				.pattern("CCC")
				.define('C', ItemRegistry.MAGIC_CLOTH.get())
				.define('R', AAItems.WIND_RUNE.get())
				.unlockedBy("has_rune", has(AAItems.WIND_RUNE.get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AAItems.WINDMAKER_SKIRT.get(), 1)
				.pattern("CCC")
				.pattern("CRC")
				.pattern("C C")
				.define('C', ItemRegistry.MAGIC_CLOTH.get())
				.define('R', AAItems.WIND_RUNE.get())
				.unlockedBy("has_rune", has(AAItems.WIND_RUNE.get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AAItems.WINDMAKER_BOOTS.get(), 1)
				.pattern("CRC")
				.pattern("C C")
				.define('C', ItemRegistry.MAGIC_CLOTH.get())
				.define('R', AAItems.WIND_RUNE.get())
				.unlockedBy("has_rune", has(AAItems.WIND_RUNE.get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, AAItems.AIR_STAFF.get(), 1)
				.pattern("CCS")
				.pattern(" RC")
				.pattern("S C")
				.define('S', Items.STICK)
				.define('C', ItemRegistry.MAGIC_CLOTH.get())
				.define('R', AAItems.WIND_RUNE.get())
				.unlockedBy("has_rune", has(AAItems.WIND_RUNE.get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AAItems.WIND_RUNE.get(), 1)
				.pattern("BBB")
				.pattern("BRB")
				.pattern("BBB")
				.define('B', Items.BREEZE_ROD)
				.define('R', ItemRegistry.BLANK_RUNE.get())
				.unlockedBy("has_rune", has(ItemRegistry.BLANK_RUNE.get()))
				.save(recipeOutput);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AAItems.WIND_UPGRADE_ORB.get(), 1)
				.pattern("RRR")
				.pattern("ROR")
				.pattern("RRR")
				.define('R', AAItems.WIND_RUNE.get())
				.define('O', ItemRegistry.UPGRADE_ORB.get())
				.unlockedBy("has_rune", has(ItemRegistry.BLANK_RUNE.get()))
				.save(recipeOutput);
	}
}
