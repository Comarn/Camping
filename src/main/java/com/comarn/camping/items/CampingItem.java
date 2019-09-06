package com.comarn.camping.items;

import com.comarn.camping.CampingMod;

import net.minecraft.item.Item;

public class CampingItem extends Item {
	public CampingItem(String name) {
        this(name, new Properties());
    }

    public CampingItem(String name, Properties prop) {
        super(prop.group(CampingMod.itemGroup));
	    setRegistryName(name);
    }
	
//	@SideOnly(Side.CLIENT)
//    public void initModel() {
//        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//    }
}
