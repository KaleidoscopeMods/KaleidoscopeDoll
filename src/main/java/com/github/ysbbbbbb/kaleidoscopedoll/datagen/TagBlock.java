package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class TagBlock extends BlockTagsProvider {
    public TagBlock(DataGenerator dataGenerator, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.DOLL_MACHINE.get());
    }
}
