package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator.getPackOutput());
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModItems.COMPUTER.get())
                .pattern("III")
                .pattern("IRI")
                .pattern("SSS")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('S', Items.SMOOTH_STONE)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(consumer);
    }
}
