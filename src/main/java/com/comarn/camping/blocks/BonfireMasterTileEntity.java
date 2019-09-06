package com.comarn.camping.blocks;

import com.comarn.camping.CampingMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;

import static com.comarn.camping.blocks.ModBlocks.BONFIRE_MASTER_TILE;

public class BonfireMasterTileEntity extends BonfireTileEntity implements ITickableTileEntity {
    private int fuel;

    public BonfireMasterTileEntity() {
        super(BONFIRE_MASTER_TILE);

        fuel = 8000;    // Most but not all of a night
    }

    @Override
    public boolean isMaster() {
        return true;
    }


    // I don't think I need to synchronize to client because a client doesn't care except that it's still lit, and it will
    // see the block update when it's not. But what ab0ut something like WAILA?

    //TODO inventory stuff (hoppers)

    @Override
    public void tick() {
        fuel--;
        if(fuel <= 0) {
            BonfireBlock.destroyBonfire(this.world, this.pos, true);
        }

        // This is an expensive operation so let's do it only four times a second
        if(fuel % 5 == 0) {
            repelHostileMobs();
        }

        markDirty();
    }


    // Partially derived from Project E's Interdiction Torch
    private void repelHostileMobs() {
        List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.add(-8, -8, -8), pos.add(8, 8, 8)));

        for (Entity ent : list) {
            if(ent instanceof MonsterEntity){
                Vec3d firePos = new Vec3d(this.pos.add(0.5, 0.5, 0.5));
                Vec3d monsterPos = new Vec3d(ent.posX, ent.posY, ent.posZ);
                double distance = firePos.distanceTo(monsterPos) + 0.1D;

                Vec3d r = monsterPos.subtract(firePos).scale(.67D / distance);

                ent.addVelocity(r.x, r.y, r.z);
            }
        }
    }



    public boolean handleActivation(ItemStack stack) {
        CampingMod.LOGGER.error(fuel);
        if(isWoodenFuel(stack)) {
            Item item = stack.getItem();
//            int burn = item.getBurnTime(stack);
            int burn = AbstractFurnaceTileEntity.getBurnTimes().get(item);

            fuel += burn * stack.getCount();
            stack.setCount(0);
            CampingMod.LOGGER.error(fuel);

            markDirty();

            return true;
        } else {
            return false;
        }
    }

    private static final ResourceLocation woodenTagId = new ResourceLocation("forge", "wooden");
    private boolean isWoodenFuel(ItemStack stack) {
        Item item = stack.getItem();
        if(!ItemTags.getCollection().getOrCreate(woodenTagId).contains(item)) {
            return false;
        }

        return AbstractFurnaceTileEntity.isFuel(stack);
    }


    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);

        fuel = tag.getInt("fuel");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt("fuel", fuel);

        return super.write(tag);
    }
}
