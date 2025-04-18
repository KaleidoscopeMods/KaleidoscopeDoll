package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemModelGenerator extends ItemModelProvider {
    public ItemModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, KaleidoscopeDoll.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModRegisterEvent.DOLL_ITEMS.stream().forEach(item -> {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
            if (key == null) {
                return;
            }
            withExistingParent(key.getPath(), modLoc("block/doll/" + key.getPath()));
        });
    }
}
