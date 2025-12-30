package com.github.ysbbbbbb.kaleidoscopedoll.client.resources;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.client.bedrock.BedrockModel;
import com.github.ysbbbbbb.kaleidoscopedoll.client.custom.CustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.client.custom.CustomDollResourceLoader;
import net.minecraft.client.model.Model;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.io.IOException;
import java.io.InputStream;

public class CustomDollReloadListener implements ResourceManagerReloadListener {
    public static Model DFAULT_DOLL_MODEL;
    public static final ResourceLocation DEFAULT_TEXTURE_ID = new ResourceLocation(KaleidoscopeDoll.MOD_ID, "textures/block/default_custom_doll.png");

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        try {
            if (DFAULT_DOLL_MODEL == null) {
                readDefaultModel(manager);
            }
            CustomDollResourceLoader.init(manager);
            CustomDollLoader.init();
        } catch (IOException e) {
            KaleidoscopeDoll.LOGGER.error("Failed to reload custom dolls", e);
        }
    }

    private static void readDefaultModel(ResourceManager manager) {
        manager.getResource(new ResourceLocation(KaleidoscopeDoll.MOD_ID, "models/bedrock/default_custom_doll.json")).ifPresent(res -> {
            try (InputStream stream = res.open()) {
                DFAULT_DOLL_MODEL = new BedrockModel(stream);
            } catch (Exception e) {
                KaleidoscopeDoll.LOGGER.error("Failed to load default custom doll model", e);
            }
        });
    }
}
