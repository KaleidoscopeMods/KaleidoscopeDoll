package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.DOLL_ITEMS;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(generator.getPackOutput(), lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput consumer) {
        List<Item> dollItems = Lists.newArrayList(DOLL_ITEMS);
        for (int i = 0; i < dollItems.size(); i++) {
            if (2 <= i && i <= 66) {
                continue;
            }
            Item item = dollItems.get(i);
            stonecutterResultFromBase(consumer, RecipeCategory.DECORATIONS, item, Items.WHITE_WOOL, 1);
        }
    }
}
