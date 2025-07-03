package com.github.ysbbbbbb.kaleidoscopedoll.event;

import com.github.ysbbbbbb.kaleidoscopedoll.block.DollBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.config.GeneralConfig;
import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.init.ModEntities;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.Phantom;

import java.util.List;

public class PhantomSpawnEvent {
    public static void register() {
        // 注册实体加载事件
        ServerEntityEvents.ENTITY_LOAD.register((entity, serverLevel) -> {
            if (!(entity instanceof Phantom phantom)) {
                return;
            }
            if (!GeneralConfig.ENABLE_PHANTOM_DOLL_SPAWN.get()) {
                return;
            }
            if (phantom.getRandom().nextDouble() > GeneralConfig.PHANTOM_DOLL_SPAWN_CHANCE.get()) {
                return;
            }
            // 延迟 5 tick 执行，确保幻翼完全生成
            serverLevel.getServer().tell(new TickTask(5, () -> attachRandomDollToPhantom(phantom, serverLevel)));
        });
    }

    private static void attachRandomDollToPhantom(Phantom phantom, ServerLevel level) {
        // 随机选择一个玩偶外观
        DollBlock randomBlock = getRandomDollBlock(level.random);
        if (randomBlock == null) {
            return;
        }

        // 创建玩偶实体
        DollEntity doll = new DollEntity(ModEntities.DOLL, level);
        doll.setDisplayBlockState(randomBlock.defaultBlockState());
        doll.setPos(phantom.getX(), phantom.getY() + 0.35, phantom.getZ());
        doll.setYRot(phantom.getYRot());

        // 将玩偶附加到幻翼上
        level.addFreshEntity(doll);
        doll.startRiding(phantom);
    }

    private static DollBlock getRandomDollBlock(RandomSource random) {
        List<DollBlock> dollBlocks = Lists.newArrayList(ModRegisterEvent.DOLL_BLOCKS.values());
        if (dollBlocks.isEmpty()) {
            return null;
        }
        return dollBlocks.get(random.nextInt(dollBlocks.size()));
    }
}