package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.block.ComputerBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.block.DollGiftBoxBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.block.DollMachineBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.block.entity.DollGiftBoxBlockEntiy;
import com.github.ysbbbbbb.kaleidoscopedoll.block.entity.DollMachineBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KaleidoscopeDoll.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, KaleidoscopeDoll.MOD_ID);

    public static DeferredBlock<Block> DOLL_MACHINE = BLOCKS.register("doll_machine", DollMachineBlock::new);
    public static DeferredBlock<Block> PURPLE_DOLL_GIFT_BOX = BLOCKS.register("purple_doll_gift_box", DollGiftBoxBlock::new);
    public static DeferredBlock<Block> GREEN_DOLL_GIFT_BOX = BLOCKS.register("green_doll_gift_box", DollGiftBoxBlock::new);
    public static DeferredBlock<Block> YELLOW_DOLL_GIFT_BOX = BLOCKS.register("yellow_doll_gift_box", DollGiftBoxBlock::new);
    public static DeferredBlock<Block> COMPUTER = BLOCKS.register("computer", ComputerBlock::new);

    public static Supplier<BlockEntityType<DollGiftBoxBlockEntiy>> DOLL_GIFT_BOX_BE = BLOCK_ENTITIES.register("doll_gift_box", () ->
            BlockEntityType.Builder.of(DollGiftBoxBlockEntiy::new,
                    PURPLE_DOLL_GIFT_BOX.get(),
                    GREEN_DOLL_GIFT_BOX.get(),
                    YELLOW_DOLL_GIFT_BOX.get()
            ).build(null)
    );
    public static Supplier<BlockEntityType<DollMachineBlockEntity>> DOLL_MACHINE_BE = BLOCK_ENTITIES.register("doll_machine", () ->
            BlockEntityType.Builder.of(DollMachineBlockEntity::new, DOLL_MACHINE.get()).build(null));

}
