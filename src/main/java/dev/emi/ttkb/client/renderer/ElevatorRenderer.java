package dev.emi.ttkb.client.renderer;

import java.util.Random;

import dev.emi.ttkb.block.entity.ElevatorBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
public class ElevatorRenderer extends BlockEntityRenderer<ElevatorBlockEntity> {

    public ElevatorRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(ElevatorBlockEntity elevator, float var2, MatrixStack stack, VertexConsumerProvider vertexConsumer, int i1, int i2) {
        if (elevator.cosmeticBlock != null) {
            BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();
            BlockState state = elevator.cosmeticBlock;
            MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
            stack.push();
            stack.scale(0.999f, 0.999f, 0.999f);
			stack.translate(0.0005f, 0.0005f, 0.0005f);
			BlockPos pos = elevator.getPos();
			Random random = new Random();
			manager.renderBlock(state, pos, (ClientWorld) (elevator.getWorld()), stack, vertexConsumer.getBuffer(RenderLayers.getBlockLayer(state)), true, random);
            stack.pop();
        }
    }

}