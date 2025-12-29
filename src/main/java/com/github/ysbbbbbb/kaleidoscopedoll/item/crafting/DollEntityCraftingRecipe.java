package com.github.ysbbbbbb.kaleidoscopedoll.item.crafting;

import com.github.ysbbbbbb.kaleidoscopedoll.datagen.TagItem;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopedoll.item.CustomDollItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;

public class DollEntityCraftingRecipe extends CustomRecipe {
    public DollEntityCraftingRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        boolean hasDollItem = false;
        boolean hasBlockToEntityItem = false;
        int itemCount = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                itemCount++;
                if (stack.getItem() instanceof DollItem) {
                    hasDollItem = true;
                } else if (stack.is(ModItems.CUSTOM_DOLL.get())) {
                    String modelId = CustomDollItem.getModelId(stack);
                    hasDollItem = StringUtils.isNotBlank(modelId);
                } else if (stack.is(TagItem.BLOCK_DOLLS_TO_ENTITY_ITEM)) {
                    hasBlockToEntityItem = true;
                } else {
                    return false;
                }
            }
        }

        return itemCount == 2 && hasDollItem && hasBlockToEntityItem;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        ItemStack dollItemStack = ItemStack.EMPTY;
        boolean isCustomDoll = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (stack.getItem() instanceof DollItem) {
                dollItemStack = stack;
                break;
            } else if (stack.is(ModItems.CUSTOM_DOLL.get())) {
                String modelId = CustomDollItem.getModelId(stack);
                if (StringUtils.isNotBlank(modelId)) {
                    dollItemStack = stack;
                    isCustomDoll = true;
                    break;
                }
            }
        }

        if (dollItemStack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (isCustomDoll) {
            String modelId = CustomDollItem.getModelId(dollItemStack);
            return DollEntityItem.createItemWithCustomDollId(modelId);
        } else {
            DollItem dollItem = (DollItem) dollItemStack.getItem();
            return DollEntityItem.createItemWithBlockState(dollItem.getBlock().defaultBlockState());
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.DOLL_ENTITY_CRAFTING.get();
    }
}
