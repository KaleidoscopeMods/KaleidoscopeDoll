package com.github.ysbbbbbb.kaleidoscopedoll.data.custom;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipFile;

public class ServerCustomDollLoader {
    private static final Path ROOT = FMLPaths.CONFIGDIR.get().resolve(KaleidoscopeDoll.MOD_ID).resolve("custom");
    private static final String DIRECTORY_MODELS_NAME = "models";
    /**
     * zip 文件有两种情况，一直是直接下面就是 models 和 lang 文件夹
     * 另一种是有一个顶层文件夹，下面才是 models 和 lang 文件夹
     * 我们需要同时考虑这两种情况
     */
    private static final Pattern ZIP_MODELS_PATTERN = Pattern.compile("^(?:[^/]+/)?models/[^/]+\\.json$");

    private static final String JSON = ".json";
    private static final String ZIP = ".zip";

    /**
     * 缓存数据
     */
    private static final Set<String> MODELS = Sets.newLinkedHashSet();

    public static void init() throws IOException {
        if (!Files.isDirectory(ROOT)) {
            Files.createDirectories(ROOT);
        }

        MODELS.clear();

        try (Stream<Path> pathStream = Files.walk(ROOT, 1)) {
            pathStream.filter(p -> !p.equals(ROOT)).forEach(path -> {
                // 文件夹
                if (Files.isDirectory(path)) {
                    ServerCustomDollLoader.loadFromDirectory(path);
                    return;
                }

                // zip 文件
                if (Files.isRegularFile(path) && path.toString().endsWith(ZIP)) {
                    ServerCustomDollLoader.loadFromZip(path);
                }
            });
        }

        // 最后把内部数据包读取的数据存入
        ServerCustomDollResourceLoader.putAll(MODELS);
    }

    private static void loadFromDirectory(Path directory) {
        Path modelsDir = directory.resolve(DIRECTORY_MODELS_NAME);
        if (Files.isDirectory(modelsDir)) {
            try (Stream<Path> pathStream = Files.walk(modelsDir, 1)) {
                pathStream.filter(p -> !p.equals(modelsDir)).forEach(path -> {
                    if (Files.isRegularFile(path) && path.toString().endsWith(JSON)) {
                        try (InputStream stream = Files.newInputStream(path)) {
                            readModel(stream);
                        } catch (IOException e) {
                            KaleidoscopeDoll.LOGGER.error("Failed to read model file: {}", path, e);
                        }
                    }
                });
            } catch (IOException e) {
                KaleidoscopeDoll.LOGGER.error("Failed to load models from directory: {}", directory, e);
            }
        }
    }

    private static void loadFromZip(Path zipFile) {
        try (ZipFile zip = new ZipFile(zipFile.toFile())) {
            zip.stream().forEach(entry -> {
                String entryName = entry.getName().replace('\\', '/');
                if (entryName.startsWith("/")) {
                    entryName = entryName.substring(1);
                }

                if (ZIP_MODELS_PATTERN.matcher(entryName).matches()) {
                    try (InputStream stream = zip.getInputStream(entry)) {
                        readModel(stream);
                    } catch (IOException e) {
                        KaleidoscopeDoll.LOGGER.error("Failed to read model file from zip: {} in {}", entryName, zipFile, e);
                    }
                }
            });
        } catch (IOException e) {
            KaleidoscopeDoll.LOGGER.error("Failed to load models from zip file: {}", zipFile, e);
        }
    }

    private static void readModel(InputStream stream) {
        try {
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            JsonObject object = GsonHelper.parse(reader);
            JsonArray array = GsonHelper.getAsJsonArray(object, "minecraft:geometry");
            if (array.isEmpty() || !array.get(0).isJsonObject()) {
                KaleidoscopeDoll.LOGGER.error("Invalid model format: missing or invalid 'minecraft:geometry' array");
                return;
            }
            JsonObject geometry = array.get(0).getAsJsonObject();
            JsonObject description = GsonHelper.getAsJsonObject(geometry, "description");
            String identifier = GsonHelper.getAsString(description, "identifier");
            MODELS.add(identifier);
        } catch (Exception e) {
            KaleidoscopeDoll.LOGGER.error("Failed to read model", e);
        }
    }

    public static Set<String> getModels() {
        return MODELS;
    }
}
