package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.block.DollMachineBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class ModModelGenerator extends ModelProvider {
    public ModModelGenerator(PackOutput output) {
        super(output, KaleidoscopeDoll.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        ModRegisterEvent.DOLL_BLOCKS.values().forEach(block -> {
            ResourceLocation location = ModModelGenerator.getDollLocation(block);
            blockModels.blockStateOutput.accept(ModModelGenerator.createDoll(block, BlockModelGenerators.plainVariant(location)));
        });
        blockModels.blockStateOutput.accept(ModModelGenerator.dollMachine(ModBlocks.DOLL_MACHINE.get()));
        blockModels.blockStateOutput.accept(ModModelGenerator.simpleBlock(ModBlocks.GREEN_DOLL_GIFT_BOX.get()));
        blockModels.blockStateOutput.accept(ModModelGenerator.simpleBlock(ModBlocks.YELLOW_DOLL_GIFT_BOX.get()));
        blockModels.blockStateOutput.accept(ModModelGenerator.simpleBlock(ModBlocks.PURPLE_DOLL_GIFT_BOX.get()));
        ModRegisterEvent.DOLL_ITEMS.forEach(item -> {
            if (!(item instanceof BlockItem blockItem)) return;
            ResourceLocation location = ModModelGenerator.getDollLocation(blockItem.getBlock());
            itemModels.itemModelOutput.accept(item, ItemModelUtils.plainModel(location));
        });
        itemModels.itemModelOutput.accept(
            ModItems.DOLL_ICON.get(),
            ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(ModItems.DOLL_ICON.get()))
        );
    }

    public static ResourceLocation getDollLocation(Block block) {
        ResourceLocation resourcelocation = BuiltInRegistries.BLOCK.getKey(block);
        return resourcelocation.withPrefix("block/doll/");
    }

    public static BlockModelDefinitionGenerator dollMachine(DollMachineBlock block) {
        return MultiPartGenerator.multiPart(block)
            .with(
                BlockModelGenerators.condition().term(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER),
                BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block))
            );
    }

    public static BlockModelDefinitionGenerator simpleBlock(Block block) {
        return MultiPartGenerator.multiPart(block).with(BlockModelGenerators.plainVariant(ModelLocationUtils.getModelLocation(block)));
    }

    public static BlockModelDefinitionGenerator createDoll(Block block, MultiVariant post) {
        return MultiPartGenerator.multiPart(block)
            .with(
                BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH),
                post.with(BlockModelGenerators.UV_LOCK)
            )
            .with(
                BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST),
                post.with(BlockModelGenerators.Y_ROT_90).with(BlockModelGenerators.UV_LOCK)
            )
            .with(
                BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH),
                post.with(BlockModelGenerators.Y_ROT_180).with(BlockModelGenerators.UV_LOCK)
            )
            .with(
                BlockModelGenerators.condition().term(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST),
                post.with(BlockModelGenerators.Y_ROT_270).with(BlockModelGenerators.UV_LOCK)
            );
    }
}
