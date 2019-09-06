package com.comarn.camping.blocks;

import com.comarn.camping.CampingMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.Collection;

public class BonfireBlock extends CampingBlock {
    public static final IProperty<Boolean> MASTER = BooleanProperty.create("master");    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(MASTER);
    }


    public BonfireBlock() {
        super("blockbonfire", Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.0f).lightValue(15));

        this.setDefaultState(this.stateContainer.getBaseState().with(MASTER, false));
    }


    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if(state.get(MASTER)) {
            return new BonfireMasterTileEntity();
        } else {
            return new BonfireTileEntity();
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        BonfireTileEntity tile = (BonfireTileEntity)world.getTileEntity(pos);

        if(tile.hasMaster()) {
            return tile.getMaster().handleActivation(player.getHeldItem(hand));
        }

        return false;
    }




    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if(fromPos.equals(pos.up()) && worldIn.isAirBlock(fromPos)) {
            state = ((FireBlock) Blocks.FIRE).getStateForPlacement(worldIn, fromPos);
            worldIn.setBlockState(fromPos, state, 11);
        }
    }


    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        destroyBonfire(world, pos, true);
    }

    @Override
    public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion) {
        destroyBonfire(world, pos, false);
    }

    @Override
    public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT; }

    // Make this like netherrack w.r.t. fire spread -----------------------------------------------------------------
    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 0;
    }

    @Override
    public boolean isFlammable(BlockState state, IBlockReader world, BlockPos pos, Direction face)
    {
        return face == Direction.UP;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 0;
    }

     @Override
     public boolean isFireSource(BlockState state, IBlockReader world, BlockPos pos, Direction side) {
         return side == Direction.UP;
     }

    @Override
    public boolean isBurning(BlockState state, IBlockReader world, BlockPos pos) {
        return true;
    }




    // Placing and Removing Logic -----------------------------------------------------------------------------------

    /* Checks to see if the targeted block is the log at the top of a bonfire pile
       Which is a 3x3 block of logs with a plus shape of logs on the second layer and a single log on the third
       with air above each block */
    public static boolean validBonfireTarget(World world, BlockPos posHit) {
        // More efficient to verify this first so we don't waste our time for other blocks
        if(!isLog(world, posHit)) {
            return false;
        }

        for(int i = -1; i < 2; i++) {
            for(int j = -2; j < 1; j++) {
                for(int k = -1; k < 2; k++) {
                    BlockPos currentPos = posHit.add(i, j, k);
                    if(j == -2 || (j == -1 && (i == 0 || k == 0) || (j == 0 && i == 0 && k == 0))) {
                        if(!isLog(world, currentPos)) {
                            return false;
                        }
                    } else {
                        if((i == 0 || k == 0 || j == -1) && !world.isAirBlock(currentPos) && (world.getBlockState(currentPos).getBlock() != Blocks.FIRE)) {
                            return false;
                        }
                    }
                }
            }
        }

        return world.isAirBlock(posHit.up()) || (world.getBlockState(posHit.up()).getBlock() == Blocks.FIRE);   // check on top of center
    }


    private static final ResourceLocation logTagId = new ResourceLocation("minecraft", "logs");
    private static boolean isLog(World world, BlockPos pos) {
        return BlockTags.LOGS.contains(world.getBlockState(pos).getBlock());
    }

    public static void createBonfire(World world, BlockPos posHit) {
        BlockState bonfireState = ModBlocks.BONFIRE_BLOCK.getDefaultState();
        world.setBlockState(posHit, bonfireState.with(BonfireBlock.MASTER, true));
        BonfireMasterTileEntity master = (BonfireMasterTileEntity)world.getTileEntity(posHit);

        // Bottom two layers
        for(int i = -1; i < 2; i++) {
            for(int j = -2; j < 0; j++) {
                for(int k = -1; k < 2; k++) {
                    BlockPos currentPos = posHit.add(i, j, k);

                    if(isLog(world, currentPos)) {
                        world.setBlockState(currentPos, bonfireState);
                        BonfireTileEntity slave = ((BonfireTileEntity)world.getTileEntity(currentPos));

                        if(slave != null) {
                            slave.setMaster(master);
                        }
                    }
                }
            }
        }

        for(int i = -1; i < 2; i++) {
            for(int j = -2; j < 1; j++) {
                for(int k = -1; k < 2; k++) {
                    BlockPos currentPos = posHit.add(i, j, k);

                    if(world.getBlockState(currentPos).getBlock() == ModBlocks.BONFIRE_BLOCK && world.isAirBlock(currentPos.up())) {
                        world.setBlockState(currentPos.up(), ((FireBlock) Blocks.FIRE).getDefaultState());
                    }
                }
            }
        }
    }


//    private static final ResourceLocation charcoalTagId = new ResourceLocation("forge", "blockCharcoal");
    public static void destroyBonfire(World world, BlockPos pos, boolean placeCharcoal) {
        BonfireTileEntity te = (BonfireTileEntity)world.getTileEntity(pos);
        if(!te.hasMaster()) {
            CampingMod.LOGGER.error("Trying to destroy bonfire at " +
                    pos + " but no tile entity was found.");
            return;
        }
        BlockPos top = te.getMaster().getPos();


        // Start from the bottom so we don't trigger fire replacement updates on top
        for(int i = -1; i < 2; i++) {
            for(int j = -2; j < 1; j++) {
                for(int k = -1; k < 2; k++) {
                    BlockPos currentPos = top.add(i, j, k);
                    if(world.getBlockState(currentPos).getBlock() == ModBlocks.BONFIRE_BLOCK) {
                        world.removeBlock(currentPos, false);
                    }
                }
            }
        }

        if(placeCharcoal) {
            ResourceLocation charcoalTagId = new ResourceLocation("forge", "blockcharcoal");
            Collection<Block> charcoalBlocks = BlockTags.getCollection().getOrCreate(charcoalTagId).getAllElements();
            BlockState coalState = charcoalBlocks.isEmpty() ? Blocks.COAL_BLOCK.getDefaultState() : charcoalBlocks.iterator().next().getDefaultState();

            world.setBlockState(top.down(2), coalState);
        }
    }
}
