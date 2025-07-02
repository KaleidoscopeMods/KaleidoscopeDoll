package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerators implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        // 服务端数据生成器
        pack.addProvider(LootTableGenerator.BlockLootTables::new);
        pack.addProvider(TagBlock::new);
        pack.addProvider(TagItem::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(AdvancementGenerator::new);

        // 客户端数据生成器
        pack.addProvider(BlockStateGenerator::new);
    }
}
