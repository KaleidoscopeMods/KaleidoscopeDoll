package com.github.ysbbbbbb.kaleidoscopedoll.client.custom;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.client.bedrock.BedrockModel;
import com.github.ysbbbbbb.kaleidoscopedoll.client.bedrock.BedrockModelUtil;
import com.github.ysbbbbbb.kaleidoscopedoll.utils.Md5Utils;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 和 CustomDollResourceManager 类似，但是是读取 Minecraft 的客户端资源包
 */
public class CustomDollResourceLoader {
    private static final String MODELS_PATH = "custom_dolls/models";
    private static final String LANGUAGES_PATH = "custom_dolls/lang";
    private static final String TEXTURES_PATH = "custom_dolls/textures";

    private static final String JSON = ".json";
    private static final String PNG = ".png";

    /**
     * 缓存数据
     */
    private static final Map<String, Model> MODELS = Maps.newHashMap();
    private static final Map<String, Map<String, String>> LANGUAGES = Maps.newHashMap();
    private static final Map<String, ResourceLocation> TEXTURES = Maps.newHashMap();

    public static void init(ResourceManager manager) throws IOException {
        MODELS.clear();
        LANGUAGES.clear();
        TEXTURES.clear();

        manager.listResources(MODELS_PATH, path -> path.getPath().endsWith(JSON)).forEach((name, resource) -> {
            try (InputStream stream = resource.open()) {
                readModel(stream);
            } catch (IOException e) {
                KaleidoscopeDoll.LOGGER.error("Failed to load model resource: {}", name, e);
            }
        });

        manager.listResources(LANGUAGES_PATH, path -> path.getPath().endsWith(JSON)).forEach((name, resource) -> {
            String langName = name.getPath().substring(LANGUAGES_PATH.length() + 1, name.getPath().length() - JSON.length());
            try (InputStream stream = resource.open()) {
                readLanguage(langName, stream);
            } catch (IOException e) {
                KaleidoscopeDoll.LOGGER.error("Failed to load language resource: {}", name, e);
            }
        });

        manager.listResources(TEXTURES_PATH, path -> path.getPath().endsWith(PNG)).forEach((name, resource) -> {
            String textureName = name.getPath().substring(TEXTURES_PATH.length() + 1, name.getPath().length() - PNG.length());
            try (InputStream stream = resource.open()) {
                readTexture(textureName, stream);
            } catch (IOException e) {
                KaleidoscopeDoll.LOGGER.error("Failed to load texture resource: {}", name, e);
            }
        });
    }

    public static void putAll(Map<String, Model> models, Map<String, Map<String, String>> languages, Map<String, ResourceLocation> textures) {
        models.putAll(MODELS);

        LANGUAGES.forEach((key, value) -> {
            languages.computeIfAbsent(key, k -> Maps.newHashMap()).putAll(value);
        });

        textures.putAll(TEXTURES);
    }

    private static void readModel(InputStream stream) {
        try {
            BedrockModel model = new BedrockModel(stream);
            String identifier = model.getIdentifier();
            if (identifier != null) {
                MODELS.put(identifier, model);
            } else {
                KaleidoscopeDoll.LOGGER.error("Model identifier is null");
            }
        } catch (Exception e) {
            KaleidoscopeDoll.LOGGER.error("Failed to read model", e);
        }
    }

    private static void readLanguage(String name, InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> languages = BedrockModelUtil.GSON.fromJson(reader, type);
        LANGUAGES.computeIfAbsent(name, k -> Maps.newHashMap()).putAll(languages);
    }

    private static void readTexture(String name, InputStream stream) {
        String md5Name = Md5Utils.md5Hex(name);
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "custom/" + md5Name);
        try {
            NativeImage image = NativeImage.read(stream);
            DynamicTexture texture = new DynamicTexture(image);
            Minecraft.getInstance().getTextureManager().register(id, texture);
            TEXTURES.put(name, id);
        } catch (Exception e) {
            KaleidoscopeDoll.LOGGER.error("Failed to read texture: {}", name, e);
        }
    }
}
