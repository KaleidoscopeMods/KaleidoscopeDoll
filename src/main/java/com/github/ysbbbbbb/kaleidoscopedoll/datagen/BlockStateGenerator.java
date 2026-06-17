package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
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
            this.getVariantBuilder(block).forAllStates(blockState -> {
                int rotation = blockState.getValue(BlockStateProperties.ROTATION_16);
                ResourceLocation file = modLoc("rot_doll/%s/rot_%d".formatted(key.getPath(), rotation));
                ModelFile.UncheckedModelFile modelFile = new ModelFile.UncheckedModelFile(file);
                return ConfiguredModel.builder().modelFile(modelFile).build();
            });
        });

        horizontalBlock(ModBlocks.COMPUTER.get(), models().getExistingFile(modLoc("block/computer")));
        simpleBlock(ModBlocks.CUSTOM_DOLL.get(), models().getExistingFile(modLoc("block/custom_doll")));
    }
}
