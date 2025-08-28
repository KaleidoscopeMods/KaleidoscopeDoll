package com.github.ysbbbbbb.kaleidoscopedoll.network;

import com.github.ysbbbbbb.kaleidoscopedoll.network.message.DollTweakersMessage;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    private static final String VERSION = "1.0.0";

    public static void registerPacket(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION).optional();

        registrar.playToServer(DollTweakersMessage.TYPE, DollTweakersMessage.STREAM_CODEC, DollTweakersMessage::handle);
    }
}
