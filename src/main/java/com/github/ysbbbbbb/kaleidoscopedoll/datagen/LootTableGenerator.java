package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;

public class LootTableGenerator {
    public static class BlockLootTables extends FabricBlockLootTableProvider {
        public final Set<Block> knownBlocks = new HashSet<>();

        public BlockLootTables(FabricDataOutput output) {
            super(output);
        }

        @Override
        public void generate() {
            ModRegisterEvent.DOLL_BLOCKS.values().forEach(this::dropSelf);

            LootTable.Builder table = createSinglePropConditionTable(ModBlocks.DOLL_MACHINE, BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
            this.add(ModBlocks.DOLL_MACHINE, table);
        }
    }
}
