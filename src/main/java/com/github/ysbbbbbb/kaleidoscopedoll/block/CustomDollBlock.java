package com.github.ysbbbbbb.kaleidoscopedoll.block;

import com.github.ysbbbbbb.kaleidoscopedoll.block.entity.CustomDollBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CustomDollBlock extends DollBlock implements EntityBlock {
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CustomDollBlockEntity(pPos, pState);
    }
}
