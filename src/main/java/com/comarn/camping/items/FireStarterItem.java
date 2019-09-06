package com.comarn.camping.items;

import com.comarn.camping.blocks.BonfireBlock;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireStarterItem extends CampingItem {
    public FireStarterItem() {
        super("itemfirestarter");
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        BlockPos posHit = context.getPos();
        BlockPos posFacing = posHit.offset(context.getFace());
        BlockState state;

        // Measures whether the firestarter successfully lit anything, either producing a fire block or lighting a campfire or bonfire
        boolean successFlag = false;

        if(BonfireBlock.validBonfireTarget(world, posHit)) {
            BonfireBlock.createBonfire(world, posHit);
            successFlag = true;
        } else {
            // The following code was copied from FlintAndSteelItem, with some modification and general deobfuscation

            if(FlintAndSteelItem.func_219996_a(world.getBlockState(posFacing), world, posFacing)) {
                world.playSound(player, posFacing, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                state = ((FireBlock) Blocks.FIRE).getStateForPlacement(world, posFacing);
                world.setBlockState(posFacing, state, 11);
                ItemStack itemStack = context.getItem();
                if(player instanceof ServerPlayerEntity) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, posFacing, itemStack);
                }

                successFlag = true;
            } else {
                state = world.getBlockState(posHit);
                if(FlintAndSteelItem.isUnlitCampfire(state)) {
                    world.playSound(player, posHit, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, random.nextFloat() * 0.4F + 0.8F);
                    world.setBlockState(posHit, (BlockState) state.with(BlockStateProperties.LIT, true), 11);

                    successFlag = true;
                }
            }
        }


        if(successFlag) {
            ItemStack stack = context.getItem();
            if (!player.abilities.isCreativeMode) {
                stack.shrink(1);
            }
        }
        return successFlag ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    }


}
