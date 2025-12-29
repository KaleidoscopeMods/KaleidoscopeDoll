package com.github.ysbbbbbb.kaleidoscopedoll.network.message;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.client.custom.CustomDollLoader;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.io.IOException;

public class CustomDollReloadMessage implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CustomDollReloadMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "custom_doll_reload")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CustomDollReloadMessage> STREAM_CODEC = StreamCodec.unit(new CustomDollReloadMessage());

    public static void handle(CustomDollReloadMessage message, IPayloadContext context) {
        context.enqueueWork(CustomDollReloadMessage::onReload);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onReload() {
        try {
            CustomDollLoader.init();
        } catch (IOException e) {
            KaleidoscopeDoll.LOGGER.error("Failed to reload custom dolls", e);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
