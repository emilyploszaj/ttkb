package dev.emi.ttkb.block.entity.activator;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ActivatorPlayer extends PlayerEntity {

	public ActivatorPlayer(World world) {
		super(world, new GameProfile(UUID.randomUUID(), "ActivatorPlayer"));
	}

	public void setStatus(World world, BlockPos pos) {
		this.world = world;
		this.setPos(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public boolean isCreative() {
		return false;
	}

	@Override
	public boolean isSpectator() {
		return false;
	}
}