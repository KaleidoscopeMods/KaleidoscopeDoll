package com.github.ysbbbbbb.kaleidoscopedoll.network.message;

import com.github.ysbbbbbb.kaleidoscopedoll.KaleidoscopeDoll;
import com.github.ysbbbbbb.kaleidoscopedoll.entity.DollEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.joml.Vector2f;
import org.joml.Vector3f;

public final class DollTweakersMessage implements CustomPacketPayload {
    public static final Type<DollTweakersMessage> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(KaleidoscopeDoll.MOD_ID, "doll_tweakers")
    );
    public static final StreamCodec<ByteBuf, DollTweakersMessage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, msg -> msg.entityId,
            ByteBufCodecs.VECTOR3F, msg -> msg.scale,
            ByteBufCodecs.VECTOR3F, msg -> msg.translation,
            ByteBufCodecs.FLOAT, msg -> msg.rotation.x,
            ByteBufCodecs.FLOAT, msg -> msg.rotation.y,
            DollTweakersMessage::new
    );
    private final int entityId;
    private final Vector3f scale;
    private final Vector3f translation;
    private final Vector2f rotation;

    public DollTweakersMessage(int entityId, Vector3f scale, Vector3f translation, Vector2f rotation) {
        this.entityId = entityId;
        this.scale = scale;
        this.translation = translation;
        this.rotation = rotation;
        limitValues(this);
    }

    public DollTweakersMessage(int entityId, Vector3f scale, Vector3f translation, float xRot, float yRot) {
        this(entityId, scale, translation, new Vector2f(xRot, yRot));
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

    public static void handle(DollTweakersMessage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player instanceof ServerPlayer && player.level().getEntity(message.entityId) instanceof DollEntity dollEntity) {
                    limitValues(message);
                    dollEntity.setDisplayScale(message.scale);
                    dollEntity.setDisplayTranslation(message.translation);
                    dollEntity.moveTo(dollEntity.getX(), dollEntity.getY(), dollEntity.getZ(),
                            message.rotation.y, message.rotation.x);
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
