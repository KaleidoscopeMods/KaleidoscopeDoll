package com.github.ysbbbbbb.kaleidoscopedoll.compat.jei;

import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import com.google.common.collect.Lists;
import mezz.jei.api.constants.ModIds;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class EntityDollRecipeMaker {
    private static final String GROUP = "jei.doll.entity";

    public static List<CraftingRecipe> createRecipes() {
        List<CraftingRecipe> recipes = Lists.newArrayList();
        ModRegisterEvent.DOLL_ITEMS.forEach(item -> {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
            if (key == null || !(item instanceof DollItem dollItem)) {
                return;
            }

            Ingredient inputDoll = Ingredient.of(item);
            Ingredient slimeBall = Ingredient.of(Tags.Items.SLIMEBALLS);
            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY, inputDoll, slimeBall);

            BlockState dollState = dollItem.getBlock().defaultBlockState();
            ItemStack output = DollEntityItem.createItemWithBlockState(dollState);

            ResourceLocation id = new ResourceLocation(ModIds.MINECRAFT_ID, GROUP + "." + key.getPath());
            ShapelessRecipe recipe = new ShapelessRecipe(id, GROUP, CraftingBookCategory.MISC, output, inputs);

            recipes.add(recipe);
        });
        return recipes;
    }

    private EntityDollRecipeMaker() {
    }
}
