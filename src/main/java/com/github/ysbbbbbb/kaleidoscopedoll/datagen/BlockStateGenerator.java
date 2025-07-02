package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.resources.ResourceLocation;

public class BlockStateGenerator extends FabricModelProvider {
    public BlockStateGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators generator) {
        PropertyDispatch dispatch = BlockModelGenerators.createHorizontalFacingDispatch();
        ModRegisterEvent.DOLL_BLOCKS.values().forEach(block -> {
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
            ResourceLocation location = modLoc("block/doll/" + key.getPath());
            Variant variant = Variant.variant().with(VariantProperties.MODEL, location);
            generator.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block, variant).with(dispatch));
        });

        ModRegisterEvent.DOLL_ITEMS.forEach(item -> {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
            ResourceLocation blockModel = modLoc("block/doll/" + key.getPath());
            generator.delegateItemModel(item, blockModel);
        });
    }

    @Override
    public void generateItemModels(ItemModelGenerators generator) {
    }

    private ResourceLocation modLoc(String path) {
        return new ResourceLocation(KaleidoscopeDoll.MOD_ID, path);
    }
}
