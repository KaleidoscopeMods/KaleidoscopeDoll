package com.github.ysbbbbbb.kaleidoscopedoll.compat.jade;

import com.github.ysbbbbbb.kaleidoscopedoll.block.DollBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ModPlugin implements IWailaPlugin {
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(DollBlockComponentProvider.INSTANCE, DollBlock.class);
        registration.registerEntityComponent(DollEntityComponentProvider.INSTANCE, DollEntity.class);
    }
}
