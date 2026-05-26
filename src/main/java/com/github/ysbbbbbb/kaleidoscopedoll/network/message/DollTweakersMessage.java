package com.github.ysbbbbbb.kaleidoscopedoll.network.message;

import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class DollTweakersMessage {
    private final int entityId;
    private final Vector3f scale;
    private final Vector3f translation;
    private final Vector2f rotation;
    private final CompoundTag itemDisplay;

    public DollTweakersMessage(int entityId, Vector3f scale, Vector3f translation, Vector2f rotation, CompoundTag itemDisplay) {
        this.entityId = entityId;
        this.scale = scale;
        this.translation = translation;
        this.rotation = rotation;
        this.itemDisplay = itemDisplay;
        limitValues(this);
    }

    public static void limitValues(DollTweakersMessage message) {
        limitVector3f(message.scale, 0.1f, 10f);
        limitVector3f(message.translation, -5f, 5f);
        message.rotation.x = Math.max(-90f, Math.min(90f, message.rotation.x));
        message.rotation.y = ((message.rotation.y % 360) + 360) % 360;
    }

    private static void limitVector3f(Vector3f vec, float min, float max) {
        vec.x = Math.max(min, Math.min(max, vec.x));
        vec.y = Math.max(min, Math.min(max, vec.y));
        vec.z = Math.max(min, Math.min(max, vec.z));
    }

    public static void encode(DollTweakersMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityId);
        buf.writeVector3f(message.scale);
        buf.writeVector3f(message.translation);
        buf.writeFloat(message.rotation.x);
        buf.writeFloat(message.rotation.y);
        buf.writeNbt(message.itemDisplay);
    }

    public static DollTweakersMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        Vector3f scale = buf.readVector3f();
        Vector3f translation = buf.readVector3f();
        float rotX = buf.readFloat();
        float rotY = buf.readFloat();
        Vector2f rotation = new Vector2f(rotX, rotY);
        CompoundTag itemDisplay = buf.readNbt();
        return new DollTweakersMessage(entityId, scale, translation, rotation, itemDisplay);
    }

    public static void handle(DollTweakersMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayer player = context.getSender();
                if (player != null && player.level().getEntity(message.entityId) instanceof DollEntity dollEntity) {
                    limitValues(message);
                    dollEntity.setDisplayScale(message.scale);
                    dollEntity.setDisplayTranslation(message.translation);
                    dollEntity.moveTo(dollEntity.getX(), dollEntity.getY(), dollEntity.getZ(),
                            message.rotation.y, message.rotation.x);
                    dollEntity.setItemScale(readVector3f(message.itemDisplay.getCompound("item_scale")));
                    dollEntity.setItemTranslation(readVector3f(message.itemDisplay.getCompound("item_translation")));
                    dollEntity.setItemRotation(readVector3f(message.itemDisplay.getCompound("item_rotation")));
                }
            });
        }
        context.setPacketHandled(true);
    }

    private static Vector3f readVector3f(CompoundTag tag) {
        return new Vector3f(tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"));
    }
}
