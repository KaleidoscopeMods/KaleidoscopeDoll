package com.github.ysbbbbbb.kaleidoscopedoll.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class GeneralConfig {
    public static ForgeConfigSpec.BooleanValue DOLL_CAN_BE_THROWN;
    public static ForgeConfigSpec.BooleanValue DOLL_AFFECTED_BY_WATER;
    public static ForgeConfigSpec.BooleanValue DOLL_AFFECTED_BY_GRAVITY;
    public static ForgeConfigSpec.BooleanValue DOLL_THROW_PARTICLE_EFFECT;
    public static ForgeConfigSpec.BooleanValue DOLL_CAN_KNOCKBACK_ENTITIES;
    public static ForgeConfigSpec.DoubleValue DOLL_KNOCKBACK_FORCE;
    public static ForgeConfigSpec.BooleanValue DOLL_CAN_MOUNT_ENTITIES;

    public static ForgeConfigSpec.BooleanValue ENABLE_PHANTOM_DOLL_SPAWN;
    public static ForgeConfigSpec.DoubleValue PHANTOM_DOLL_SPAWN_CHANCE;

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("doll");

        builder.comment("Whether dolls can be thrown by players");
        builder.comment("玩偶是否可以被玩家丢出");
        DOLL_CAN_BE_THROWN = builder.define("DollCanBeThrown", true);

        builder.comment("Whether dolls can be pushed by water flow");
        builder.comment("玩偶是否会被水流推动");
        DOLL_AFFECTED_BY_WATER = builder.define("DollAffectedByWater", true);

        builder.comment("Whether dolls are affected by gravity");
        builder.comment("玩偶是否受重力影响");
        DOLL_AFFECTED_BY_GRAVITY = builder.define("DollAffectedByGravity", true);

        builder.comment("Whether throwing dolls produces particle effects");
        builder.comment("丢出玩偶时是否产生粒子效果");
        DOLL_THROW_PARTICLE_EFFECT = builder.define("DollThrowParticleEffect", true);

        builder.comment("Whether thrown dolls can knockback other entities");
        builder.comment("被丢出的玩偶是否能击退其他实体");
        DOLL_CAN_KNOCKBACK_ENTITIES = builder.define("DollCanKnockbackEntities", true);

        builder.comment("The knockback force of thrown dolls, the larger the value, the stronger the knockback effect");
        builder.comment("被丢出的玩偶的击退效果强度，数值越大击退效果越强");
        DOLL_KNOCKBACK_FORCE = builder.defineInRange("DollKnockbackForce", 1.0, 0.0, 10.0);

        builder.comment("Whether dolls can be placed directly on other entities");
        builder.comment("玩偶是否可以直接放置在其他实体上");
        DOLL_CAN_MOUNT_ENTITIES = builder.define("DollCanMountEntities", true);

        builder.comment("Enable dolls spawning on phantoms", "启用幻翼附带玩偶生成");
        ENABLE_PHANTOM_DOLL_SPAWN = builder.define("EnablePhantomDollSpawn", true);

        builder.comment("Chance for phantom to spawn with a doll", "幻翼附带玩偶的生成概率");
        PHANTOM_DOLL_SPAWN_CHANCE = builder.defineInRange("PhantomDollSpawnChance", 0.03, 0.0, 1.0);

        builder.pop();
        return builder.build();
    }
}
