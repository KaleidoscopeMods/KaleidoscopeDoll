package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, KaleidoscopeDoll.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModRegisterEvent.DOLL_ITEMS.forEach(item -> {
            ResourceLocation key = BuiltInRegistries.ITEM.getKey(item);
            withExistingParent(key.getPath(), modLoc("block/doll/" + key.getPath()));
        });

        withExistingParent("computer", modLoc("block/computer"));
    }
}
