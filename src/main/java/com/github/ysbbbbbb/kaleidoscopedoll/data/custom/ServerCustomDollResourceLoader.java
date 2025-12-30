package com.github.ysbbbbbb.kaleidoscopedoll.data.custom;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.util.Set;

public class ServerCustomDollResourceLoader {
    private static final String FILE_PATH = "custom_dolls.json";

    /**
     * 缓存数据
     */
    private static final Set<String> MODELS = Sets.newLinkedHashSet();

    public static void init(ResourceManager manager) throws IOException {
        MODELS.clear();

        for (String namespace : manager.getNamespaces()) {
            ResourceLocation location = new ResourceLocation(namespace, FILE_PATH);
            manager.getResource(location).ifPresent(resource -> {
                try (var reader = resource.openAsReader()) {
                    JsonArray array = GsonHelper.parseArray(reader);
                    for (JsonElement element : array) {
                        if (GsonHelper.isStringValue(element)) {
                            MODELS.add(element.getAsString());
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to load custom dolls from " + location, e);
                }
            });
        }
    }

    public static void putAll(Set<String> target) {
        target.addAll(MODELS);
    }
}
