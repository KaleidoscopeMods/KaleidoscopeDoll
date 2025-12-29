package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, KaleidoscopeDoll.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModRegisterEvent.DOLL_BLOCKS.values().forEach(block -> {
            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
            if (key == null) {
                return;
            }
            ResourceLocation location = modLoc("block/doll/" + key.getPath());
            horizontalBlock(block, models().getExistingFile(location));
        });

        horizontalBlock(ModBlocks.COMPUTER.get(), models().getExistingFile(modLoc("block/computer")));
        horizontalBlock(ModBlocks.CUSTOM_DOLL.get(), models().getExistingFile(modLoc("block/custom_doll")));
    }
}
