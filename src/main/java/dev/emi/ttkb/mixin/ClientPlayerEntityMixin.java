package dev.emi.ttkb.mixin;

import com.mojang.authlib.GameProfile;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.emi.ttkb.TTKBMain;
import dev.emi.ttkb.block.Elevator;
import io.netty.buffer.Unpooled;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;

/**
 * Captures movement of players and sends elevator packets when able
 */
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    @Shadow
    private boolean lastOnGround;
    @Shadow
    private boolean lastIsHoldingSneakKey;
    @Shadow
    public Input input;

    private boolean lastSpace;

    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(at = @At("TAIL"), method = "tickMovement")
    public void tickMovement(CallbackInfo info) {
        if (input.jumping && !lastSpace) {
            if (world.getBlockState(getBlockPos().down()).getBlock() instanceof Elevator) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeInt(1);
                CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(TTKBMain.ELEVATOR_PACKET, buf);
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
            }
        } else if (input.sneaking && !lastIsHoldingSneakKey && onGround) {
            if (world.getBlockState(getBlockPos().down()).getBlock() instanceof Elevator) {
                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeInt(-1);
                CustomPayloadC2SPacket packet = new CustomPayloadC2SPacket(TTKBMain.ELEVATOR_PACKET, buf);
                MinecraftClient.getInstance().getNetworkHandler().sendPacket(packet);
            }
        }
        lastSpace = input.jumping;
    }
}