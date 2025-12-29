package com.github.ysbbbbbb.kaleidoscopedoll;

import com.github.ysbbbbbb.kaleidoscopedoll.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopedoll.init.*;
import com.github.ysbbbbbb.kaleidoscopedoll.network.NetworkHandler;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(KaleidoscopeDoll.MOD_ID)
public final class KaleidoscopeDoll {
    public static final String MOD_ID = "kaleidoscope_doll";
    public static final Logger LOGGER = LogUtils.getLogger();

    public KaleidoscopeDoll(IEventBus modEventBus, ModContainer modContainer) {

        modContainer.registerConfig(ModConfig.Type.COMMON, GeneralConfig.init());

        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModSounds.SOUND_EVENTS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModDataComponent.DATA_COMPONENTS.register(modEventBus);
        ModMenuType.CONTAINER_TYPE.register(modEventBus);

        modEventBus.addListener(NetworkHandler::registerPacket);
    }
}
