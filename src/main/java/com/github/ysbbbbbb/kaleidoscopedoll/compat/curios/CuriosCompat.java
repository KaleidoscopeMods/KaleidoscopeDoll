package com.github.ysbbbbbb.kaleidoscopedoll.compat.curios;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.ModList;

public class CuriosCompat {

    public static final String ID = "curios";
    private static final boolean IS_LOADED;

    static {
        IS_LOADED = ModList.get().isLoaded(ID);
    }

    @OnlyIn(Dist.CLIENT)
    public static void addEntityLayers(EntityRenderersEvent.AddLayers event) {
        if (IS_LOADED) {
            CuriosCompatInner.registerRenderer(event);
        }
    }

    public static void commonSetup() {
        if (IS_LOADED) {
            CuriosCompatInner.registerDollItemPredicate();
        }
    }

}
