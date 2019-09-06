package com.comarn.camping.blocks;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.event.RegistryEvent;

public class ModBlocks {

    @ObjectHolder("camping:blocktent")
    public static TentBlock TENT_BLOCK;
	@ObjectHolder("camping:blockbonfire")
	public static BonfireBlock BONFIRE_BLOCK;

	@ObjectHolder("camping:tilebonfire")
	public static TileEntityType<BonfireTileEntity> BONFIRE_TILE;
	@ObjectHolder("camping:tilebonfiremaster")
	public static TileEntityType<BonfireMasterTileEntity> BONFIRE_MASTER_TILE;
	
    
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().register(new TentBlock());
		event.getRegistry().register(new BonfireBlock());
	}
	
//	public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
//        event.getRegistry().register(new ItemBlock(tentBlock).setRegistryName(tentBlock.getRegistryName()));
//	}
	
	
//	@SideOnly(Side.CLIENT)
//	public static void initModels(ModelRegistryEvent event) {
//		tentBlock.initModel();
//	}
//
//	@SideOnly(Side.CLIENT)
//    public static void initItemModels() {
////        bakedModelBlock.initItemModel();
//    }
}
