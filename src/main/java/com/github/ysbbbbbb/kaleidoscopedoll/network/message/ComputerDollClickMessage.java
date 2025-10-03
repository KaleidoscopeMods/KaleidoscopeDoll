package com.github.ysbbbbbb.kaleidoscopedoll.network.message;

import com.github.ysbbbbbb.kaleidoscopedoll.inventory.ComputerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ComputerDollClickMessage {
    private final ItemStack doll;

    public ComputerDollClickMessage(ItemStack doll) {
        this.doll = doll;
    }

    public static void encode(ComputerDollClickMessage message, FriendlyByteBuf buf) {
        buf.writeItem(message.doll);
    }

    public static ComputerDollClickMessage decode(FriendlyByteBuf buf) {
        ItemStack doll = buf.readItem();
        return new ComputerDollClickMessage(doll);
    }

    public static void handle(ComputerDollClickMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> onHandle(message, context));
        }
        context.setPacketHandled(true);
    }

    private static void onHandle(ComputerDollClickMessage message, NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player == null) {
            return;
        }
        player.resetLastActionTime();
        if (player.containerMenu instanceof ComputerMenu computerMenu
            && !player.isSpectator()
            && computerMenu.clickDollButton(message.doll)) {
            player.containerMenu.broadcastChanges();
        }
    }
}
