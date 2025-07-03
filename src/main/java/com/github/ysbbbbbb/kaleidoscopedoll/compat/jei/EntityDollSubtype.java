package com.github.ysbbbbbb.kaleidoscopedoll.compat.jei;

import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;

public class EntityDollSubtype implements ISubtypeInterpreter<ItemStack> {
    @Override
    public String getSubtypeData(ItemStack itemStack, UidContext context) {
        if (context == UidContext.Ingredient) {
            return StringUtils.EMPTY;
        } else {
            Block block = DollEntityItem.getBlockFromItemStack(itemStack);
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
            return key.toString();
        }
    }

    @Override
    @SuppressWarnings("all")
    public String getLegacyStringSubtypeInfo(ItemStack ingredient, UidContext context) {
        return this.getSubtypeData(ingredient, context);
    }
}
