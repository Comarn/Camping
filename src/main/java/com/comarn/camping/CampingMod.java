package com.comarn.camping;

import com.comarn.camping.blocks.BonfireMasterTileEntity;
import com.comarn.camping.blocks.ModBlocks;
import com.comarn.camping.blocks.BonfireTileEntity;
import com.comarn.camping.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



@Mod("camping")
public class CampingMod {
    public static final String MODID = "camping";
    public static ItemGroup itemGroup = new ItemGroup("camping") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.TENT_ITEM);
        }
    };

    public static final Logger LOGGER = LogManager.getLogger();


    public CampingMod() {
        // Register the setup method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }



    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) { ModBlocks.registerBlocks(event); }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            ModItems.registerItems(event);
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(TileEntityType.Builder.create(BonfireTileEntity::new, ModBlocks.BONFIRE_BLOCK).build(null).setRegistryName("tilebonfire"));
            event.getRegistry().register(TileEntityType.Builder.create(BonfireMasterTileEntity::new, ModBlocks.BONFIRE_BLOCK).build(null).setRegistryName("tilebonfiremaster"));
        }
    }


}

// TODO render tents
// TODO tent sleeping logic