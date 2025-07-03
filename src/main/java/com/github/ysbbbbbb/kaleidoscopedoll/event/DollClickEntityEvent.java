package com.github.ysbbbbbb.kaleidoscopedoll.event;

import com.github.ysbbbbbb.kaleidoscopedoll.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class DollClickEntityEvent {
    private static final int MAX_RIDING_LAYERS = 5;

    public static void register() {
        UseEntityCallback.EVENT.register(DollClickEntityEvent::onUseEntity);
    }

    private static InteractionResult onUseEntity(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        ItemStack handItem = player.getItemInHand(hand);

        if (handItem.is(ModItems.DOLL_ENTITY_ITEM) && GeneralConfig.DOLL_CAN_MOUNT_ENTITIES.get()) {
            // 检查骑乘层数
            int ridingLayers = getRidingLayers(entity);
            if (ridingLayers >= MAX_RIDING_LAYERS) {
                return InteractionResult.PASS;
            }

            DollEntity dollEntity = DollEntityItem.getDollEntity(level, handItem);
            dollEntity.setPos(entity.getX(), entity.getY() + 1.5, entity.getZ());
            dollEntity.setYRot(player.getYRot() - 180);

            if (dollEntity.startRiding(entity)) {
                if (!level.isClientSide) {
                    level.addFreshEntity(dollEntity);
                }
                entity.playSound(SoundEvents.WOOL_PLACE, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    handItem.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private static int getRidingLayers(Entity entity) {
        int layers = 1;
        Entity current = entity;

        while (current.getVehicle() != null) {
            current = current.getVehicle();
            layers++;
        }

        return layers;
    }
}
