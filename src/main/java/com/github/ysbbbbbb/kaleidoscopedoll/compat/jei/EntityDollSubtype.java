package com.github.ysbbbbbb.kaleidoscopedoll.compat.jei;

import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.StringUtils;

public class EntityDollSubtype implements IIngredientSubtypeInterpreter<ItemStack> {
    @Override
    public String apply(ItemStack itemStack, UidContext context) {
        if (context == UidContext.Ingredient) {
            return IIngredientSubtypeInterpreter.NONE;
        } else {
            // 自定义玩偶
            String dollId = DollEntityItem.getCustomDollIdFromItemStack(itemStack);
            if (StringUtils.isNotBlank(dollId)) {
                return dollId;
            }

            // 普通玩偶
            Block block = DollEntityItem.getBlockFromItemStack(itemStack);
            ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
            return key.toString();
        }
    }
}
