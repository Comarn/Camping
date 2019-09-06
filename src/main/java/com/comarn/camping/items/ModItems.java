package com.comarn.camping.items;


import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {

	@ObjectHolder("camping:itemtent")
	public static TentItem TENT_ITEM;
    @ObjectHolder("camping:itemmarshmallow")
    public static MarshmallowItem ITEM_MARSHMALLOW_ITEM;
	@ObjectHolder("camping:itemtoastedmarshmallow")
	public static CampingItem TOASTED_MARSHMALLOW_ITEM;
	@ObjectHolder("camping:itemfirestarter")
	public static FireStarterItem FIRE_STARTER_ITEM;

	public static void registerItems(Register<Item> event) {
		event.getRegistry().register(new TentItem());
		event.getRegistry().register(new MarshmallowItem());
		event.getRegistry().register(new CampingItem("itemtoastedmarshmallow",
				(new Item.Properties()).food(CampingFoods.TOASTED_MARSHMALLOW)));
		event.getRegistry().register(new FireStarterItem());

		registerBlockItems(event);
	}

	public static void registerBlockItems(Register<Item> event) {
//            event.getRegistry().register(new BlockItem(ModBlocks.FIRSTBLOCK, properties).setRegistryName("firstblock"));
	}
}
