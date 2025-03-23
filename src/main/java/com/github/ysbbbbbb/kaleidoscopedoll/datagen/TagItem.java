package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;


public class TagItem extends ItemTagsProvider {
    public static final TagKey<Item> DOLL_MACHINE_TOKENS = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation("doll_machine_tokens"));

    public TagItem(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(DOLL_MACHINE_TOKENS).add(Items.DIAMOND.asItem());
    }
}
