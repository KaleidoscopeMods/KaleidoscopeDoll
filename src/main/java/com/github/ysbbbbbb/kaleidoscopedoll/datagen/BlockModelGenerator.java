package com.github.ysbbbbbb.kaleidoscopedoll.datagen;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.event.ModRegisterEvent;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.util.TransformationHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockModelGenerator extends BlockModelProvider {
    public BlockModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, KaleidoscopeDoll.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModRegisterEvent.DOLL_BLOCKS.values().forEach(block -> {
            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
            if (key == null) {
                return;
            }
            doll(key.getPath());
        });
    }

    private void doll(String name) {
        ResourceLocation parent = modLoc("block/doll/%s".formatted(name));
        int max = RotationSegment.getMaxSegmentIndex();

        for (int i = 0; i <= max; i++) {
            ResourceLocation file = modLoc("rot_doll/%s/rot_%d".formatted(name, i));
            withExistingParent(file.toString(), parent)
                    .rootTransforms()
                    .origin(TransformationHelper.TransformOrigin.CENTER)
                    .rotation(0, -i * 22.5f, 0, true)
                    .end();
        }
    }
}
