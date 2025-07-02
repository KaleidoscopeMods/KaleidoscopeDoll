package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, KaleidoscopeDoll.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModRegisterEvent.DOLL_BLOCKS.values().forEach(block -> {
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
            if (key == null) {
                return;
            }
            ResourceLocation location = modLoc("block/doll/" + key.getPath());
            horizontalBlock(block, models().getExistingFile(location));
        });
    }
}
