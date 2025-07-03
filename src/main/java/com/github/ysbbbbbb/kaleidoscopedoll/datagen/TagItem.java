package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.SPECIAL_TOOLTIPS;

public class TagItem extends FabricTagProvider.ItemTagProvider {
    public static final TagKey<Item> DOLL_MACHINE_TOKENS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_machine_tokens"));
    public static final TagKey<Item> VANILLA_DOLLS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "vanilla_dolls"));
    public static final TagKey<Item> ALL_DOLLS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "all_dolls"));

    public TagItem(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(DOLL_MACHINE_TOKENS).add(reverseLookup(Items.DIAMOND.asItem()));

        TagAppender<Item> vanillaDolls = this.tag(VANILLA_DOLLS);
        ModRegisterEvent.DOLL_ITEMS.stream()
                .filter(item -> !SPECIAL_TOOLTIPS.containsKey(BuiltInRegistries.ITEM.getKey(item)))
                .map(this::reverseLookup)
                .forEach(vanillaDolls::add);

        TagAppender<Item> allDolls = this.tag(ALL_DOLLS);
        ModRegisterEvent.DOLL_ITEMS.stream()
                .map(this::reverseLookup)
                .forEach(allDolls::add);
    }
}
