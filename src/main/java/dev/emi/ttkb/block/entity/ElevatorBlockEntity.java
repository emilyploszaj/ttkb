package dev.emi.ttkb.block.entity;

import dev.emi.ttkb.TTKBMain;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;

public class ElevatorBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public BlockState cosmeticBlock = null;

    public ElevatorBlockEntity() {
        super(TTKBMain.ELEVATOR_BLOCK_ENTITY);
    }

    @Override
	public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        if (tag.contains("blockState")) {
			CompoundTag state = tag.getCompound("blockState");
			cosmeticBlock = NbtHelper.toBlockState(state);
        }
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
        tag = super.toTag(tag);
        if (cosmeticBlock != null) {
            tag.put("blockState", NbtHelper.fromBlockState(cosmeticBlock));
        }
		return tag;
	}

    @Override
    public void fromClientTag(CompoundTag tag) {
        if (tag.contains("blockState")) {
			CompoundTag state = tag.getCompound("blockState");
			cosmeticBlock = NbtHelper.toBlockState(state);
        }
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        tag = super.toTag(tag);
        if (cosmeticBlock != null) {
            tag.put("blockState", NbtHelper.fromBlockState(cosmeticBlock));
        }
        return tag;
    }
}