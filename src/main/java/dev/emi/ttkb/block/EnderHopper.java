package dev.emi.ttkb.block;

import java.util.Random;

import dev.emi.ttkb.block.entity.EnderHopperBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class EnderHopper extends TransparentBlock implements BlockEntityProvider, Waterloggable {
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);

	public EnderHopper() {
		super(Settings.copy(Blocks.ENDER_CHEST));
		this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false));
	}

	public BlockState getPlacementState(ItemPlacementContext context) {
		FluidState state = context.getWorld().getFluidState(context.getBlockPos());
		boolean b = state.isIn(FluidTags.WATER) && state.getLevel() == 8;
		return (BlockState) super.getPlacementState(context).with(WATERLOGGED, b);
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity be = world.getBlockEntity(pos);
			if (be instanceof EnderHopperBlockEntity) {
				player.openHandledScreen((EnderHopperBlockEntity) be);
			}

			return ActionResult.SUCCESS;
		}
	}

	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		for(int i = 0; i < 3; i++) {
			int r1 = random.nextInt(2) * 2 - 1;
			int r2 = random.nextInt(2) * 2 - 1;
			double d1 = (double) pos.getX() + 0.5D + 0.25D * (double) r1;
			double d2 = (double) ((float) pos.getY() + random.nextFloat());
			double d3 = (double) pos.getZ() + 0.5D + 0.25D * (double) r2;
			double d4 = (double) (random.nextFloat() * (float) r1);
			double d5 = ((double) random.nextFloat() - 0.5D) * 0.125D;
			double d6 = (double) (random.nextFloat() * (float) r2);
			world.addParticle(ParticleTypes.PORTAL, d1, d2, d3, d4, d5, d6);
		}
 
	}

	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean b) {
		if (state.getBlock() != newState.getBlock()) {
		   BlockEntity be = world.getBlockEntity(pos);
		   if (be instanceof EnderHopperBlockEntity) {
			  ItemScatterer.spawn(world, pos, (Inventory) be);
			  world.updateComparators(pos, this);
		   }
		   super.onStateReplaced(state, world, pos, newState, b);
		}
	}

	public FluidState getFluidState(BlockState state) {
		return (Boolean) state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView var1) {
		return new EnderHopperBlockEntity();
	}
}