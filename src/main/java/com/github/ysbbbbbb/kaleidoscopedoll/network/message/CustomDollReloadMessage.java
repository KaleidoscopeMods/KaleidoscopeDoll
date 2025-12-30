package com.github.ysbbbbbb.kaleidoscopedoll.network.message;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.client.custom.CustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.data.custom.ServerCustomDollLoader;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModCreativeTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public record CustomDollReloadMessage(Set<String> modelIds) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CustomDollReloadMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "custom_doll_reload")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CustomDollReloadMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(HashSet::new, ByteBufCodecs.STRING_UTF8),
            CustomDollReloadMessage::modelIds,
            CustomDollReloadMessage::new
    );

    public static void handle(CustomDollReloadMessage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> onReload(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void onReload(CustomDollReloadMessage message) {
        try {
            CustomDollLoader.init();

            // 如自己不是主机，则添加这些模型 ID
            Minecraft mc = Minecraft.getInstance();
            if (!mc.isLocalServer()) {
                ServerCustomDollLoader.getModels().clear();
                ServerCustomDollLoader.getModels().addAll(message.modelIds);
            }

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

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
