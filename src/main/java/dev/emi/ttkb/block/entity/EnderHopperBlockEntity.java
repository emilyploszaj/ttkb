package dev.emi.ttkb.block.entity;

import java.util.List;

import dev.emi.ttkb.TTKBMain;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class EnderHopperBlockEntity extends LootableContainerBlockEntity implements Tickable {
	private DefaultedList<ItemStack> inventory;
	private int ticks;

	public EnderHopperBlockEntity() {
		super(TTKBMain.ENDER_HOPPER_BLOCK_ENTITY);
		this.inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);
	}

	@Override
	public void tick() {
		if (world.isClient) {
			BlockPos pos = this.getPos();
			if (this.ticks++ % 20 * 4 == 0) {
				this.world.addSyncedBlockEvent(pos, Blocks.ENDER_CHEST, 1, 1);
			}
		} else {
			List<Entity> entities = world.getEntities(ItemEntity.class, new Box(pos.getX() - 2, pos.getY() - 2, pos.getZ() - 2, pos.getX() + 3, pos.getY() + 3, pos.getZ() + 3), null);
			for (int i = 0; i < entities.size(); i++) {
				ItemEntity e = (ItemEntity) entities.get(i);
				for (int slot = 0; slot < 9; slot++) {
					ItemStack slotStack = getStack(slot);
					if (slotStack.isEmpty()) {
						setStack(slot, e.getStack());
						e.kill();
						break;
					} else if (slotStack.isItemEqual(e.getStack()) && slotStack.getMaxCount() > slotStack.getCount()) {
						if (slotStack.getCount() + e.getStack().getCount() <= slotStack.getMaxCount()) {
							slotStack.setCount(slotStack.getCount() + e.getStack().getCount());
							e.kill();
						} else {
							int leftover = slotStack.getCount() + e.getStack().getCount() - slotStack.getMaxCount();
							slotStack.setCount(slotStack.getMaxCount());
							e.getStack().setCount(leftover);
						}
						break;
					}
				}
			}
			if (ticks++ % 8 == 0) {
				outer: for (Direction d: Direction.values()) {
					BlockEntity be = world.getBlockEntity(pos.offset(d));
					if (be != null && be instanceof Inventory) {
						Inventory inv = (Inventory) be;
						for(int i = 0; i < this.size(); i++) {
							if (!this.getStack(i).isEmpty()) {
                  				ItemStack copy = this.getStack(i).copy();
								ItemStack output = HopperBlockEntity.transfer(this, inv, removeStack(i, 1), d.getOpposite());
								if (output.isEmpty()) {
									inv.markDirty();
									break outer;
								}
								setStack(i, copy);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
		if (!this.deserializeLootTable(tag)) {
			Inventories.fromTag(tag, this.inventory);
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		Inventories.toTag(tag, this.inventory);
		return tag;
	}

	@Override
	public int size() {
		return 9;
	}

	@Override
	public DefaultedList<ItemStack> getInvStackList() {
		return inventory;
	}

	@Override
	public void setInvStackList(DefaultedList<ItemStack> list) {
		inventory = list;
	}

	@Override
	public Text getContainerName() {
		return new LiteralText("Ender Hopper");
	}

	@Override
	public ScreenHandler createScreenHandler(int i, PlayerInventory player) {
		return new Generic3x3ContainerScreenHandler(i, player, this);
	}
}