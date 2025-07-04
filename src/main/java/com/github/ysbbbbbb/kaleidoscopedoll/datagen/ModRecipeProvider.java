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
        // 原版似乎有 bug，切石机配方数量超过一定值，会不能合成
        // 所以每 64 个配方分一组
        int count = 0;
        for (int i = 0; i < dollItems.size(); i++) {
            if (2 <= i && i <= 66) {
                continue;
            }
            Item doll = dollItems.get(i);
            Item wool = getWoolFromIndex(count / 64);
            stonecutterResultFromBase(consumer, RecipeCategory.DECORATIONS, doll, wool, 1);
            count++;
        }
    }

    private Item getWoolFromIndex(int number) {
        return switch (number) {
            case 0 -> Items.WHITE_WOOL;
            case 1 -> Items.ORANGE_WOOL;
            case 2 -> Items.MAGENTA_WOOL;
            case 3 -> Items.LIGHT_BLUE_WOOL;
            case 4 -> Items.YELLOW_WOOL;
            case 5 -> Items.LIME_WOOL;
            case 6 -> Items.PINK_WOOL;
            case 7 -> Items.GRAY_WOOL;
            case 8 -> Items.LIGHT_GRAY_WOOL;
            case 9 -> Items.CYAN_WOOL;
            case 10 -> Items.PURPLE_WOOL;
            case 11 -> Items.BLUE_WOOL;
            case 12 -> Items.BROWN_WOOL;
            case 13 -> Items.GREEN_WOOL;
            case 14 -> Items.RED_WOOL;
            case 15 -> Items.BLACK_WOOL;
            default -> throw new IllegalArgumentException("Invalid wool index: " + number);
        };
    }
}
