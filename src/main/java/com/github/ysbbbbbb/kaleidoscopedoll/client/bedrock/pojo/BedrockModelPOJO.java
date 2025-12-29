package com.github.ysbbbbbb.kaleidoscopedoll.client.bedrock.pojo;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;

public class BedrockModelPOJO {
    @SerializedName("format_version")
    private String formatVersion;

    @SerializedName("minecraft:geometry")
    @Nullable
    private GeometryModel[] geometryModels;

    public String getFormatVersion() {
        return formatVersion;
    }

    @Nullable
    public GeometryModel getFirstGeometryModel() {
        if (geometryModels == null || geometryModels.length == 0) {
            return null;
        }
        return geometryModels[0];
    }
}