package com.github.ysbbbbbb.kaleidoscopedoll.compat.jei;

import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityDollSubtype implements IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public String apply(ItemStack itemStack, UidContext context) {
        if (context == UidContext.Ingredient) {
            return IIngredientSubtypeInterpreter.NONE;
        } else {
            Block block = DollEntityItem.getBlockFromItemStack(itemStack);
            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
            if (key == null) {
                return IIngredientSubtypeInterpreter.NONE;
            }
            return key.toString();
        }
    }
}
