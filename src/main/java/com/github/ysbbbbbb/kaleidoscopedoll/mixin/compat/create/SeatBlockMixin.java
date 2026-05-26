package com.github.ysbbbbbb.kaleidoscopedoll.mixin.compat.create;

import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import com.github.ysbbbbbb.kaleidoscopedoll.mixin.handler.IMixinInterface;
import com.simibubi.create.content.contraptions.actors.seat.SeatBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Author: chengzi-sc
 */
@Mixin(SeatBlock.class)
public abstract class SeatBlockMixin implements IMixinInterface {
    @Inject(method = "updateEntityAfterFallOn", at = @At("HEAD"))
    private void kaleidoscopeDoll$updateEntityAfterFallOn(BlockGetter reader, Entity entity, CallbackInfo ci) {
        BlockPos pos = entity.blockPosition();
        if (entity instanceof DollEntity) {
            if (SeatBlock.isSeatOccupied(entity.level(), pos)) {
                Vec3 vec3 = entity.getDeltaMovement();
                if (vec3.y < 0.0D) {
                    entity.setDeltaMovement(vec3.x, -vec3.y * (double) 0.66F * 0.8D, vec3.z);
                }
            } else {
                if (reader.getBlockState(pos).getBlock() != ((SeatBlock) (Object) this)) {
                    return;
                }
                SeatBlock.sitDown(entity.level(), pos, entity);
            }
        }
    }
}