package com.github.ysbbbbbb.kaleidoscopedoll.network.message;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.client.custom.CustomDollLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.io.IOException;
import java.util.function.Supplier;

public class CustomDollReloadMessage {
    public static void encode(CustomDollReloadMessage message, FriendlyByteBuf buf) {
    }

    public static CustomDollReloadMessage decode(FriendlyByteBuf buf) {
        return new CustomDollReloadMessage();
    }

    public static void handle(CustomDollReloadMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(CustomDollReloadMessage::onReload);
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onReload() {
        try {
            CustomDollLoader.init();
        } catch (IOException e) {
            KaleidoscopeDoll.LOGGER.error("Failed to reload custom dolls", e);
        }
    }
}
