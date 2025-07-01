package com.github.ysbbbbbb.kaleidoscopedoll.init;


import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.item.crafting.DollEntityCraftingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, KaleidoscopeDoll.MOD_ID);

    public static final RegistryObject<RecipeSerializer<DollEntityCraftingRecipe>> DOLL_ENTITY_CRAFTING = RECIPE_SERIALIZERS.register("doll_entity_crafting",
            () -> new SimpleCraftingRecipeSerializer<>(DollEntityCraftingRecipe::new));
}
