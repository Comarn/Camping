package com.comarn.camping.blocks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;

import static com.comarn.camping.blocks.ModBlocks.BONFIRE_TILE;

public class BonfireTileEntity extends TileEntity implements ICapabilityProvider {
    private BlockPos masterPos;

    public BonfireTileEntity() {
        super(BONFIRE_TILE);
    }
    public BonfireTileEntity(TileEntityType<?> type) {     // Couldn't figure out how to construct subclass otherwise
        super(type);
    }


    public boolean isMaster() {
        return false;
    }


    public void setMaster(BonfireMasterTileEntity master) {
        this.setMaster(master.getPos());
    }

    public void setMaster(BlockPos pos) {
        if(!isMaster()) {
            masterPos = pos;
            markDirty();
        }
    }

    public BonfireMasterTileEntity getMaster() {
        if(isMaster()) {
            return (BonfireMasterTileEntity)this;
        }

        if(masterPos == null) { return null; }
        return (BonfireMasterTileEntity)world.getTileEntity(masterPos);
    }

    public boolean hasMaster() {
        return getMaster() != null;
    }


    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);

        if(tag.getBoolean("has_master")) {
            int x = tag.getInt("master_x");
            int y = tag.getInt("master_y");
            int z = tag.getInt("master_z");

            masterPos = new BlockPos(x, y, z);
        } else {
            masterPos = null;
        }
    }


    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putBoolean("has_master", masterPos != null);
        if(masterPos != null) {
            tag.putInt("master_x", masterPos.getX());
            tag.putInt("master_y", masterPos.getY());
            tag.putInt("master_z", masterPos.getZ());
        }

        return super.write(tag);
    }


    public String debugMessage() {
        String res = "I am a " + (isMaster() ? "master" : "slave") + " located at " + this.getPos().toString();
        if(!isMaster() && getMaster() != null) {
            res += "\nMy master says \"" + getMaster().debugMessage() + "\"";
        }
        return res;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.read(tag);
    }

}
