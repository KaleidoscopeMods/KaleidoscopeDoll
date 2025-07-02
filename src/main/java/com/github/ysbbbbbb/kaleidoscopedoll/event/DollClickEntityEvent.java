package com.github.ysbbbbbb.kaleidoscopedoll.event;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopedoll.item.DollEntityItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = KaleidoscopeDoll.MOD_ID)
public class DollClickEntityEvent {
    private static final int MAX_RIDING_LAYERS = 5;

    @SubscribeEvent
    public static void onClickEntity(PlayerInteractEvent.EntityInteract event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        ItemStack mainHandItem = player.getMainHandItem();

        if (mainHandItem.is(ModItems.DOLL_ENTITY_ITEM.get()) && GeneralConfig.DOLL_CAN_MOUNT_ENTITIES.get()) {
            // 检查骑乘层数
            int ridingLayers = getRidingLayers(target);
            if (ridingLayers >= MAX_RIDING_LAYERS) {
                // 如果骑乘层数达到最大值，取消操作
                return;
            }

            DollEntity dollEntity = DollEntityItem.getDollEntity(player.level(), mainHandItem);
            dollEntity.setPos(target.getX(), target.getY() + 1.5, target.getZ());
            dollEntity.setYRot(player.getYRot() - 180);
            if (dollEntity.startRiding(target)) {
                if (!player.level().isClientSide) {
                    player.level().addFreshEntity(dollEntity);
                }
                target.playSound(SoundEvents.WOOL_PLACE, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    mainHandItem.shrink(1);
                }
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }

    /**
     * 计算实体的骑乘层数
     *
     * @param entity 要检查的实体
     * @return 骑乘层数（包括当前实体）
     */
    private static int getRidingLayers(Entity entity) {
        // 当前实体算作第1层
        int layers = 1;
        Entity current = entity;

        // 向上遍历所有乘坐的实体
        while (current.getVehicle() != null) {
            current = current.getVehicle();
            layers++;
        }

        return layers;
    }
}
