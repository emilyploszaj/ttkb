package dev.emi.ttkb.block;

import dev.emi.ttkb.block.entity.activator.ActivatorPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class Activator extends DispenserBlock {
	static ActivatorPlayer fakePlayer;

	public Activator() {
		super(Block.Settings.copy(Blocks.DISPENSER));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
		return ActionResult.PASS;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return null;
	}

	protected void dispense(World world, BlockPos pos) {
		try {
			BlockPos original = pos;
			Direction dir = world.getBlockState(pos).get(FACING);
			pos = pos.offset(dir);
			if (fakePlayer == null) {
				fakePlayer = new ActivatorPlayer(world);
			}
			fakePlayer.setStatus(world, pos);
			ActionResult result = world.getBlockState(pos).onUse(world, fakePlayer, fakePlayer.getActiveHand(),
				new BlockHitResult(new Vec3d(dir.getOffsetX(), dir.getOffsetY(), dir.getOffsetZ()), dir, pos, false));
			if (result != ActionResult.SUCCESS) {
				world.playLevelEvent(1001, original, 0);
			}
		} catch(Exception e) {
			// :)
		}
	}
}