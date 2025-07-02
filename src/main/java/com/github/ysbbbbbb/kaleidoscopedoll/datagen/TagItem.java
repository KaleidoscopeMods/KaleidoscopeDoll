package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;

public class TagItem extends ItemTagsProvider {
    public static final TagKey<Item> DOLL_MACHINE_TOKENS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_machine_tokens"));
    public static final TagKey<Item> VANILLA_DOLLS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "vanilla_dolls"));
    public static final TagKey<Item> ALL_DOLLS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "all_dolls"));

    public TagItem(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, pBlockTags, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(DOLL_MACHINE_TOKENS).add(Items.DIAMOND.asItem());

        this.tag(VANILLA_DOLLS).add(ModRegisterEvent.DOLL_ITEMS.stream()
                .filter(item -> !SPECIAL_TOOLTIPS.containsKey(BuiltInRegistries.ITEM.getKey(item)))
                .toArray(Item[]::new));

        this.tag(ALL_DOLLS).add(ModRegisterEvent.DOLL_ITEMS.toArray(Item[]::new));
    }
}
