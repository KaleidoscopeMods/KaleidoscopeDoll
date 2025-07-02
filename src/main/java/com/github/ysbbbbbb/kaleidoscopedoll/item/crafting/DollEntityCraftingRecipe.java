package com.github.ysbbbbbb.kaleidoscopedoll.item.crafting;

import com.github.ysbbbbbb.kaleidoscopedoll.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DollEntityCraftingRecipe extends CustomRecipe {
    public DollEntityCraftingRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean hasDollItem = false;
        boolean hasSlimeBall = false;
        int itemCount = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                itemCount++;
                if (stack.getItem() instanceof DollItem) {
                    hasDollItem = true;
                } else if (stack.is(Items.SLIME_BALL)) {
                    hasSlimeBall = true;
                } else {
                    return false;
                }
            }
        }

        return itemCount == 2 && hasDollItem && hasSlimeBall;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack dollItemStack = ItemStack.EMPTY;
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.getItem() instanceof DollItem) {
                dollItemStack = stack;
                break;
            }
        }
        if (dollItemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        DollItem dollItem = (DollItem) dollItemStack.getItem();
        return DollEntityItem.createItemWithBlockState(dollItem.getBlock().defaultBlockState());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.DOLL_ENTITY_CRAFTING;
    }
}
