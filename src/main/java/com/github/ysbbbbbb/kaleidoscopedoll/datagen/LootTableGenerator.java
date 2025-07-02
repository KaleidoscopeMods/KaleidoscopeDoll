package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.HashSet;
import java.util.Set;

public class LootTableGenerator {
    public static class BlockLootTables extends BlockLootSubProvider {
        public final Set<Block> knownBlocks = new HashSet<>();

        public BlockLootTables(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        public void generate() {
            ModRegisterEvent.DOLL_BLOCKS.values().forEach(this::dropSelf);

            LootTable.Builder table = createSinglePropConditionTable(ModBlocks.DOLL_MACHINE.get(), BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER);
            this.add(ModBlocks.DOLL_MACHINE.get(), table);
        }

        @Override
        public void add(Block block, LootTable.Builder builder) {
            this.knownBlocks.add(block);
            super.add(block, builder);
        }

        @Override
        public Iterable<Block> getKnownBlocks() {
            return this.knownBlocks;
        }
    }
}
