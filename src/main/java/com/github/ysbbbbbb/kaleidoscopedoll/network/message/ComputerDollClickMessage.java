package com.github.ysbbbbbb.kaleidoscopedoll.network.message;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.inventory.ComputerMenu;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ComputerDollClickMessage(ItemStack doll) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ComputerDollClickMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "computer_doll_click")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ComputerDollClickMessage> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, msg -> msg.doll,
            ComputerDollClickMessage::new
    );

    public static void handle(ComputerDollClickMessage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> onHandle(message, context));
        }
    }

    private static void onHandle(ComputerDollClickMessage message, IPayloadContext context) {
        Player player = context.player();
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        serverPlayer.resetLastActionTime();
        if (player.containerMenu instanceof ComputerMenu computerMenu
            && !player.isSpectator()
            && computerMenu.clickDollButton(message.doll)) {
            player.containerMenu.broadcastChanges();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
