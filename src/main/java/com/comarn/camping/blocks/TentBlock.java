package com.comarn.camping.blocks;

import com.comarn.camping.CampingMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.World;
//import net.minecraft.block.properties.PropertyBool;
//import net.minecraft.block.properties.PropertyDirection;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EnumCreatureType;
//import net.minecraft.entity.item.EntityItem;
//import net.minecraft.entity.monster.EntityMob;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.text.TextComponentTranslation;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;

public class TentBlock extends CampingBlock {

	// Begin Blockstate Stuff ----------------------------------------------------------------------------------------------
	public static final IProperty<Direction> PROPERTY_FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
	public static final IProperty<Boolean> PROPERTY_TOP = BooleanProperty.create("top");
	public static final IProperty<Boolean> PROPERTY_FRONT = BooleanProperty.create("front");

	// This property is not used for anything, and only necessary for bed code to work properly.
	public static final IProperty<Boolean> PROPERTY_OCCUPIED = BooleanProperty.create("occupied");

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(PROPERTY_FACING);
		builder.add(PROPERTY_TOP);
		builder.add(PROPERTY_FRONT);
		builder.add(PROPERTY_OCCUPIED);
	}


	public TentBlock() {
		super("blocktent", Material.WOOL, SoundType.CLOTH, 1.0f);

		this.setDefaultState(this.stateContainer.getBaseState());
	}


	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		destroyTent(world, pos, !player.isCreative());
	}

	@Override
	public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
		destroyTent(world, pos, true);
	}

	@Override
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if(fromPos.equals(pos.down()) && !hasSolidTop(worldIn, fromPos)) {
			destroyTent(worldIn, pos, true);
		}
	}

	private boolean hasSolidTop(World world, BlockPos pos) {
		return Block.hasSolidSide(world.getBlockState(pos), world, pos, Direction.UP);
	}


	@Override
	public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.SOLID; }
//	public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT; }


//	@Override
//    public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player) {
//        return true;
//    }
//
	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
		CampingMod.LOGGER.error(state.isSolid());
//		if(worldIn.isRemote) {
//			return true;
//		}
//
//		// Picking up tent
//		ItemStack heldItem = playerIn.getHeldItem(hand);
//		if (heldItem.isEmpty() && playerIn.isSneaking()) {
//			ItemStack stack = new ItemStack(ModItems.tentItem);
//			EntityItem dropped = new EntityItem(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, stack);
//            worldIn.spawnEntity(dropped);
//
//            //Break tent blocks
//            removeTent(worldIn, pos, state);
//            return true;
//		}
//
//
//
//		if(worldIn.isDaytime()) {
//			playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
//			return true;
//		}
//
//		List<EntityMob> monsters = worldIn.<EntityMob>getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB((double)pos.getX() - 8.0D, (double)pos.getY() - 5.0D, (double)pos.getZ() - 8.0D, (double)pos.getX() + 8.0D, (double)pos.getY() + 5.0D, (double)pos.getZ() + 8.0D), isMonster);
//		if(!monsters.isEmpty()) {
//			boolean fire = false;
//			if(!fire) {
//				playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
//				return true;
//			}
//		}
//
//		// It's night and we can sleep
//		worldIn.setWorldTime(0);
//		CampingMod.LOGGER.info("Slept in tent. Seting time to 0.");
//
		return true;
	}


	public static void destroyTent(World world, BlockPos pos, boolean dropItem) {
		BlockState state = world.getBlockState(pos);
		BlockPos frontTop = state.get(PROPERTY_TOP) ? pos : pos.up();
		if(!state.get(PROPERTY_FRONT)) {
			frontTop = frontTop.offset(state.get(PROPERTY_FACING), -1);
		}

		// Make the bottom two have particle effects
		world.removeBlock(frontTop, false);
		world.destroyBlock(frontTop.down(), dropItem);
		world.removeBlock(frontTop.offset(state.get(PROPERTY_FACING)), false);
		world.destroyBlock(frontTop.down().offset(state.get(PROPERTY_FACING)), false);
	}

}
