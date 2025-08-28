package com.github.ysbbbbbb.kaleidoscopedoll.compat.jei;

import com.github.ysbbbbbb.kaleidoscopedoll.datagen.TagItem;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import com.google.common.collect.Lists;
import mezz.jei.api.constants.ModIds;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class EntityDollRecipeMaker {
    private static final String GROUP = "jei.doll.entity";

    public static List<RecipeHolder<CraftingRecipe>> createRecipes() {
        List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();
        ModRegisterEvent.DOLL_ITEMS.forEach(item -> {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
            if (!(item instanceof DollItem dollItem)) {
                return;
            }

            Ingredient inputDoll = Ingredient.of(item);
            Ingredient blockToEntityItem = Ingredient.of(TagItem.BLOCK_DOLLS_TO_ENTITY_ITEM);
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, inputDoll, blockToEntityItem);

            BlockState dollState = dollItem.getBlock().defaultBlockState();
            ItemStack output = DollEntityItem.createItemWithBlockState(dollState);

            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ModIds.MINECRAFT_ID, GROUP + "." + key.getPath());
            ShapelessRecipe recipe = new ShapelessRecipe(GROUP, CraftingBookCategory.MISC, output, inputs);

            recipes.add(new RecipeHolder<>(id, recipe));
        });
        return recipes;
    }

    private EntityDollRecipeMaker() {
    }
}
