package com.github.ysbbbbbb.kaleidoscopedoll.compat.emi;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.datagen.TagItem;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollItem;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

@EmiEntrypoint
public class ModEMIPlugins implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry) {
        hideDolls(emiRegistry);
    }

    private void hideDolls(EmiRegistry registry) {
        registry.removeEmiStacks(emiStack -> {
            ItemStack itemStack = emiStack.getItemStack();
            if (itemStack.getItem() instanceof DollItem && !itemStack.is(TagItem.VANILLA_DOLLS)) {
                return true;
            }
            return itemStack.getItem() instanceof DollEntityItem;
        });

        // 只添加一个 doll_0
        Item dollItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_0"));
        Block dollBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(KaleidoscopeDoll.MOD_ID, "doll_0"));

        if (dollItem != null) {
            registry.addEmiStackAfter(EmiStack.of(dollItem), emiStack -> emiStack.getItemStack().is(ModItems.YELLOW_DOLL_GIFT_BOX.get()));
        }
        if (dollBlock != null) {
            ItemStack dollEntityItem = DollEntityItem.createItemWithBlockState(dollBlock.defaultBlockState());
            registry.addEmiStackAfter(EmiStack.of(dollEntityItem), emiStack -> emiStack.getItemStack().is(ModItems.YELLOW_DOLL_GIFT_BOX.get()));
        }
    }
}
