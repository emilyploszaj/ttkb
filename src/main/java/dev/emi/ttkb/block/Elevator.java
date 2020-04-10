package dev.emi.ttkb.block;

import dev.emi.ttkb.block.entity.ElevatorBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TransparentBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Elevator extends TransparentBlock implements BlockEntityProvider {
    public static final BooleanProperty FILLED = BooleanProperty.of("filled");

    public Elevator() {
        super(FabricBlockSettings.copy(Blocks.WHITE_WOOL).nonOpaque().build());
        setDefaultState(getStateManager().getDefaultState().with(FILLED, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem() instanceof BlockItem) {
            BlockItem item = (BlockItem) player.getStackInHand(hand).getItem();
			Block block = item.getBlock();
			BlockState placeState = block.getPlacementState(new ItemPlacementContext(new ItemUsageContext(player, hand, hit)));
            if (placeState.isSimpleFullBlock(world, pos)) {
                world.setBlockState(pos, getDefaultState().with(FILLED, true));
                ElevatorBlockEntity be = (ElevatorBlockEntity) world.getBlockEntity(pos);
                be.cosmeticBlock = placeState;
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> manager) {
        manager.add(FILLED);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new ElevatorBlockEntity();
    }

    public boolean isTranslucent(BlockState state, BlockView view, BlockPos pos) {
        return true;
    }
  
    public boolean isSimpleFullBlock(BlockState statee, BlockView view, BlockPos pos) {
        return false;
    }
}