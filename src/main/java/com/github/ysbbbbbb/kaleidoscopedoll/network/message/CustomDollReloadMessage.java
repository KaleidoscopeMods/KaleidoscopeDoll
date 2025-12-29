package com.github.ysbbbbbb.kaleidoscopedoll.network.message;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.client.custom.CustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModCreativeTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.io.IOException;
import java.util.Optional;
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

            Minecraft minecraft = Minecraft.getInstance();
            FeatureFlagSet features = Optional.ofNullable(minecraft.player)
                    .map(p -> p.connection)
                    .map(ClientPacketListener::enabledFeatures)
                    .orElse(FeatureFlagSet.of());

            final boolean hasOperatorItemsTabPermissions =
                    minecraft.options.operatorItemsTab().get() ||
                    Optional.of(minecraft)
                            .map(m -> m.player)
                            .map(Player::canUseGameMasterBlocks)
                            .orElse(false);

            ClientLevel level = minecraft.level;
            if (level != null) {
                RegistryAccess registryAccess = level.registryAccess();
                final CreativeModeTab.ItemDisplayParameters parameters =
                        new CreativeModeTab.ItemDisplayParameters(features, hasOperatorItemsTabPermissions, registryAccess);

                ModCreativeTabs.CUSTOM_DOLL_TAB.get().buildContents(parameters);
                ModCreativeTabs.ENTITY_DOLL_TAB.get().buildContents(parameters);
            }
        } catch (IOException e) {
            KaleidoscopeDoll.LOGGER.error("Failed to reload custom dolls", e);
        }
    }
}
