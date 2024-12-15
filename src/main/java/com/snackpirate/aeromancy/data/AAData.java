package com.snackpirate.aeromancy.data;

import com.snackpirate.aeromancy.Aeromancy;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Aeromancy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class AAData extends DatapackBuiltinEntriesProvider {

	private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, AADamageTypes::bootstrap);

	public AAData(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Set.of("minecraft", Aeromancy.MOD_ID));
	}

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		PackOutput output = event.getGenerator().getPackOutput();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
		DatapackBuiltinEntriesProvider datapackProvider = new AAData(output, provider);
		DataGenerator generator = event.getGenerator();
		LanguageProvider lang;
		lang = new AALang(output);
		generator.addProvider(event.includeServer(), datapackProvider);
		generator.addProvider(event.includeClient(), lang);
		generator.addProvider(event.includeServer(), new AAItemTags(output, provider, CompletableFuture.completedFuture(TagsProvider.TagLookup.empty())));
		generator.addProvider(event.includeServer(), new AARecipes(output, provider));
	}
}
