package dev.emi.ttkb;

import dev.emi.ttkb.client.renderer.ElevatorRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

public class TTKBClient extends TTKBMain implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		BlockEntityRendererRegistry.INSTANCE.register(TTKBMain.ELEVATOR_BLOCK_ENTITY, ElevatorRenderer::new);
		BlockRenderLayerMap.INSTANCE.putBlock(TTKBMain.ELEVATOR, RenderLayer.getCutout());
	}
}
