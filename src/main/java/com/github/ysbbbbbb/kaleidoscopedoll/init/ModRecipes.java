package com.github.ysbbbbbb.kaleidoscopedoll.init;


import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.item.crafting.DollEntityCraftingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, KaleidoscopeDoll.MOD_ID);

    public static final Supplier<RecipeSerializer<DollEntityCraftingRecipe>> DOLL_ENTITY_CRAFTING = RECIPE_SERIALIZERS.register("doll_entity_crafting",
            () -> new SimpleCraftingRecipeSerializer<>(DollEntityCraftingRecipe::new));
}
