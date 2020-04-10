package dev.emi.ttkb;

import com.google.common.base.Supplier;

import dev.emi.ttkb.block.Activator;
import dev.emi.ttkb.block.Elevator;
import dev.emi.ttkb.block.EnderHopper;
import dev.emi.ttkb.block.entity.ElevatorBlockEntity;
import dev.emi.ttkb.block.entity.EnderHopperBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class TTKBMain implements ModInitializer {
	public static final Block ELEVATOR = registerBlock(new Elevator(), new Identifier("ttkb", "elevator"));
	public static final Block ACTIVATOR = registerBlock(new Activator(), new Identifier("ttkb", "activator"));
	public static final Block ENDER_HOPPER = registerBlock(new EnderHopper(), new Identifier("ttkb", "ender_hopper"));

	public static final Identifier ELEVATOR_PACKET = new Identifier("ttkb:elevator");
	
	public static final BlockEntityType<ElevatorBlockEntity> ELEVATOR_BLOCK_ENTITY = registerBlockEntityType(new Identifier("ttkb", "elevator"), ElevatorBlockEntity::new, ELEVATOR);
	public static final BlockEntityType<EnderHopperBlockEntity> ENDER_HOPPER_BLOCK_ENTITY = registerBlockEntityType(new Identifier("ttkb", "ender_hoper"), EnderHopperBlockEntity::new, ENDER_HOPPER);
	
	@Override
	public void onInitialize() {
		ServerSidePacketRegistry.INSTANCE.register(ELEVATOR_PACKET, (context, buffer) -> {
			ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
			World world = player.world;
			int direction = buffer.readInt();
			BlockPos pos = player.getBlockPos().down();
			if (world.getBlockState(pos).getBlock() instanceof Elevator) {
				if (direction == 1) {
					for (int i = 1; i < 32; i++) {
						BlockPos newPos = pos.up(i);
						if (world.getBlockState(newPos).getBlock() instanceof Elevator &&
								!world.getBlockState(newPos.up(1)).getBlock().isOpaque(world.getBlockState(newPos.up(1))) &&
								!world.getBlockState(newPos.up(2)).getBlock().isOpaque(world.getBlockState(newPos.up(2)))) {
							player.teleport(player.getPos().x, player.getPos().y + i, player.getPos().z);
							break;
						}
					}
				} else if (direction == -1) {
					for (int i = 1; i < 32; i++) {
						BlockPos newPos = pos.down(i);
						if (world.getBlockState(newPos).getBlock() instanceof Elevator &&
								!world.getBlockState(newPos.up(1)).getBlock().isOpaque(world.getBlockState(newPos.up(1))) &&
								!world.getBlockState(newPos.up(2)).getBlock().isOpaque(world.getBlockState(newPos.up(2)))) {
							player.teleport(player.getPos().x, player.getPos().y - i, player.getPos().z);
							break;
						}
					}
				}
			}
		});
	}

	public static Block registerBlock(Block block, Identifier identifier) {
		Registry.register(Registry.BLOCK, identifier, block);
		Registry.register(Registry.ITEM,identifier, new BlockItem(block, new Item.Settings()));
		return block;
	}

	public static <B extends BlockEntity> BlockEntityType<B> registerBlockEntityType(Identifier identifier, Supplier<B> supplier, Block... blocks) {
		return Registry.register(Registry.BLOCK_ENTITY_TYPE, identifier, BlockEntityType.Builder.create(supplier, blocks).build(null));
	}
}