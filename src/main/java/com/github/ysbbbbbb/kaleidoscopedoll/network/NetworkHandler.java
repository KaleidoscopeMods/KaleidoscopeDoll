package com.github.ysbbbbbb.kaleidoscopedoll.network;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.network.message.ComputerDollClickMessage;
import com.github.ysbbbbbb.kaleidoscopedoll.network.message.DollTweakersMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class NetworkHandler {
    private static final String VERSION = "1.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(KaleidoscopeDoll.MOD_ID, "network"),
            () -> VERSION, it -> it.equals(VERSION), it -> it.equals(VERSION));

    public static void init() {
        CHANNEL.registerMessage(0, DollTweakersMessage.class, DollTweakersMessage::encode, DollTweakersMessage::decode, DollTweakersMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(1, ComputerDollClickMessage.class, ComputerDollClickMessage::encode, ComputerDollClickMessage::decode, ComputerDollClickMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }
}
