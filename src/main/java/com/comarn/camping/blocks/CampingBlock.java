package com.comarn.camping.blocks;

import com.comarn.camping.CampingMod;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class CampingBlock extends Block {
	public CampingBlock(String name, Properties prop) {
        super(prop);
        setRegistryName(name);
    }

    public CampingBlock(String name, Material mat, SoundType sound, float hardness) {
        this(name, Properties.create(mat).sound(sound).hardnessAndResistance(hardness));
    }
	
//	@SideOnly(Side.CLIENT)
//	public void initModel() {
//		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//	}
}
