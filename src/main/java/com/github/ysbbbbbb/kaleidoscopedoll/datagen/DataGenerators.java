package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var registries = event.getLookupProvider();
        var vanillaPack = generator.getVanillaPack(true);
        var existingFileHelper = event.getExistingFileHelper();
        var pack = generator.getPackOutput();

        generator.addProvider(event.includeServer(), new LootTableProvider(pack, Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(LootTableGenerator.BlockLootTables::new, LootContextParamSets.BLOCK)),
                registries));
        var blockTagsProvider = vanillaPack.addProvider(packOutput ->
                new TagBlock(packOutput, registries, KaleidoscopeDoll.MOD_ID, existingFileHelper));
        vanillaPack.addProvider(packOutput ->
                new TagItem(packOutput, registries, blockTagsProvider.contentsGetter(), KaleidoscopeDoll.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeServer(), new ModRecipeProvider(generator, registries));

        generator.addProvider(true, new AdvancementProvider(
                pack, registries, existingFileHelper,
                Collections.singletonList(new AdvancementGenerator())
        ));
        generator.addProvider(event.includeClient(), new ItemModelGenerator(pack, existingFileHelper));
        generator.addProvider(event.includeClient(), new BlockStateGenerator(pack, existingFileHelper));
    }
}
