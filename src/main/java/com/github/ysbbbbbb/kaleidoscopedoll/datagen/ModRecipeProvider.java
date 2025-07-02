package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.function.Consumer;

import static com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent.DOLL_ITEMS;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(Consumer<FinishedRecipe> consumer) {
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
