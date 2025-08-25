package com.github.ysbbbbbb.kaleidoscopedoll.init;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.block.DollGiftBoxBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.block.DollMachineBlock;
import com.github.ysbbbbbb.kaleidoscopedoll.block.entity.DollGiftBoxBlockEntiy;
import com.github.ysbbbbbb.kaleidoscopedoll.block.entity.DollMachineBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, KaleidoscopeDoll.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, KaleidoscopeDoll.MOD_ID);

    public static RegistryObject<Block> DOLL_MACHINE = BLOCKS.register("doll_machine", DollMachineBlock::new);
    public static RegistryObject<Block> PURPLE_DOLL_GIFT_BOX = BLOCKS.register("purple_doll_gift_box", DollGiftBoxBlock::new);
    public static RegistryObject<Block> GREEN_DOLL_GIFT_BOX = BLOCKS.register("green_doll_gift_box", DollGiftBoxBlock::new);
    public static RegistryObject<Block> YELLOW_DOLL_GIFT_BOX = BLOCKS.register("yellow_doll_gift_box", DollGiftBoxBlock::new);

    public static RegistryObject<BlockEntityType<DollGiftBoxBlockEntiy>> DOLL_GIFT_BOX_BE = BLOCK_ENTITIES.register("doll_gift_box", () ->
            BlockEntityType.Builder.of(DollGiftBoxBlockEntiy::new,
                    PURPLE_DOLL_GIFT_BOX.get(),
                    GREEN_DOLL_GIFT_BOX.get(),
                    YELLOW_DOLL_GIFT_BOX.get()
            ).build(null)
    );
    public static RegistryObject<BlockEntityType<DollMachineBlockEntity>> DOLL_MACHINE_BE = BLOCK_ENTITIES.register("doll_machine", () ->
            BlockEntityType.Builder.of(DollMachineBlockEntity::new, DOLL_MACHINE.get()).build(null));
}
