package com.comarn.camping.items;

import com.comarn.camping.CampingMod;
import com.comarn.camping.blocks.BonfireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MarshmallowItem extends CampingItem {
    public MarshmallowItem() {
        super("itemmarshmallow", (new Item.Properties()).food(CampingFoods.MARSHMALLOW));
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = context.getPlayer();


        if(canToastMarshmallows(state)) {
            world.playSound(player, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.7F, 1.5F);
            if (!world.isRemote) {
                ItemStack untoastedStack = context.getItem();

                if (player != null) {
                    ItemStack toastedStack = new ItemStack(ModItems.TOASTED_MARSHMALLOW_ITEM, untoastedStack.getCount());
                    CampingMod.LOGGER.error(toastedStack);
                    player.inventory.setInventorySlotContents(player.inventory.getSlotFor(untoastedStack), toastedStack);
                }
            }

            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    private boolean canToastMarshmallows(BlockState state) {
        Block block = state.getBlock();
        if((block instanceof CampfireBlock && state.get(CampfireBlock.LIT)) || block instanceof BonfireBlock) {
            return true;
        }

        return false;
    }
}
