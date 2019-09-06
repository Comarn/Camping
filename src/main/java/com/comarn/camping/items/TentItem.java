package com.comarn.camping.items;

import com.comarn.camping.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class TentItem extends CampingItem {
	public TentItem() {
        super("itemtent", new Properties().maxStackSize(1));
    }


    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
	    World worldIn = context.getWorld();
	    BlockPos pos = context.getPos();
	    Direction facing = context.getFace();
        PlayerEntity player = context.getPlayer();

        if (worldIn.isRemote)
        {
            return ActionResultType.SUCCESS;
        }


        pos = pos.offset(facing);
        Direction direction = player.getHorizontalFacing();
        ItemStack itemstack = player.getHeldItem(context.getHand());

        if (!checkValidPlace(player, worldIn, pos, direction, itemstack, context)) {
            return ActionResultType.FAIL;
        }

        placeTentInWorld(worldIn, pos, direction);

        BlockState newstate = worldIn.getBlockState(pos);
        SoundType soundtype = newstate.getBlock().getSoundType(newstate, worldIn, pos, player);
        worldIn.playSound((PlayerEntity) null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

        itemstack.shrink(1);
        return ActionResultType.SUCCESS;
    }

    private boolean checkValidPlace(PlayerEntity player, World world, BlockPos pos, Direction facing, ItemStack itemstack, ItemUseContext context) {
        // Check ground is solid
        if(!hasSolidTop(world, pos.down()) || !hasSolidTop(world, pos.offset(facing).down())) {
            return false;
        }

        // Check spots where tent will go are empty
        for (BlockPos pos2 : new BlockPos[]{pos, pos.up(), pos.offset(facing), pos.offset(facing).up()}) {
            if (!(player.canPlayerEdit(pos2, facing, itemstack) && (world.getBlockState(pos2).isReplaceable(new BlockItemUseContext(context)) || world.isAirBlock(pos2))))
                return false;
        }

        return true;
    }

    private boolean hasSolidTop(World world, BlockPos pos) {
	    return Block.hasSolidSide(world.getBlockState(pos), world, pos, Direction.UP);
    }


    private void placeTentInWorld(World world, BlockPos pos, Direction facing) {
        BlockState blockstate = ModBlocks.TENT_BLOCK.getDefaultState().with(ModBlocks.TENT_BLOCK.PROPERTY_FACING, facing)
                                                   .with(ModBlocks.TENT_BLOCK.PROPERTY_FRONT, true).with(ModBlocks.TENT_BLOCK.PROPERTY_TOP, false);
        world.setBlockState(pos, blockstate);
        world.setBlockState(pos.up(), blockstate.with(ModBlocks.TENT_BLOCK.PROPERTY_TOP, true));
        world.setBlockState(pos.offset(facing), blockstate.with(ModBlocks.TENT_BLOCK.PROPERTY_FRONT, false));
        world.setBlockState(pos.offset(facing).up(), blockstate.with(ModBlocks.TENT_BLOCK.PROPERTY_TOP, true).with(ModBlocks.TENT_BLOCK.PROPERTY_FRONT, false));
    }
}
