package com.github.ysbbbbbb.kaleidoscopedoll.config;


import net.neoforged.neoforge.common.ModConfigSpec;

public class GeneralConfig {
    public static ModConfigSpec.IntValue YELLOW_DOLL_GIFT_BOX_WEIGHT;
    public static ModConfigSpec.IntValue GREEN_DOLL_GIFT_BOX_WEIGHT;
    public static ModConfigSpec.IntValue PURPLE_DOLL_GIFT_BOX_WEIGHT;

    public static ModConfigSpec.BooleanValue DOLL_CAN_BE_THROWN;
    public static ModConfigSpec.BooleanValue DOLL_AFFECTED_BY_WATER;
    public static ModConfigSpec.BooleanValue DOLL_AFFECTED_BY_GRAVITY;
    public static ModConfigSpec.BooleanValue DOLL_THROW_PARTICLE_EFFECT;
    public static ModConfigSpec.BooleanValue DOLL_CAN_KNOCKBACK_ENTITIES;
    public static ModConfigSpec.DoubleValue DOLL_KNOCKBACK_FORCE;
    public static ModConfigSpec.BooleanValue DOLL_CAN_MOUNT_ENTITIES;

    public static ModConfigSpec init() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        builder.push("doll");

        builder.comment("Yellow gift box weights, yellow gift boxes can draw out vanilla humanoids doll");
        builder.comment("黄色礼品盒权重，黄色礼品盒可以抽出原版人形生物玩偶");
        YELLOW_DOLL_GIFT_BOX_WEIGHT = builder.defineInRange("YellowDollGiftBoxWeight", 10, 0, 100);

        builder.comment("Green gift box weights, green gift boxes can draw out vanilla neutral friendly mobs doll");
        builder.comment("绿色礼品盒权重，绿色礼品盒可以抽出原版中立友好生物玩偶");
        GREEN_DOLL_GIFT_BOX_WEIGHT = builder.defineInRange("GreenDollGiftBoxWeight", 10, 0, 100);

        builder.comment("Purple gift box weights, purple gift boxes can draw out vanilla hostile mobs doll");
        builder.comment("紫色礼品盒权重，紫色礼品盒可以抽出原版敌对生物玩偶");
        PURPLE_DOLL_GIFT_BOX_WEIGHT = builder.defineInRange("PurpleDollGiftBoxWeight", 80, 0, 100);

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

        builder.pop();
        return builder.build();
    }
}
