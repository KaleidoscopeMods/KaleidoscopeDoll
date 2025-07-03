package com.github.ysbbbbbb.kaleidoscopedoll.init;


import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.item.crafting.DollEntityCraftingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipes {
    public static RecipeSerializer<DollEntityCraftingRecipe> DOLL_ENTITY_CRAFTING;

    public static void registerRecipe() {
        DOLL_ENTITY_CRAFTING = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER,
                ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_entity_crafting"),
                new SimpleCraftingRecipeSerializer<>(DollEntityCraftingRecipe::new));
    }
}
