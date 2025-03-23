package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, KaleidoscopeDoll.MOD_ID);

    public static RegistryObject<SoundEvent> DUCK_TOY = SOUND_EVENTS.register("block.duck_toy",
            () -> new SoundEvent(new ResourceLocation(KaleidoscopeDoll.MOD_ID, "block.duck_toy")));
}
