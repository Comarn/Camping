package com.comarn.camping.items;

import net.minecraft.item.Food;

public class CampingFoods {
    public static final Food MARSHMALLOW = (new Food.Builder()).hunger(1).saturation(0F).build();
    public static final Food TOASTED_MARSHMALLOW = (new Food.Builder()).hunger(1).saturation(1F).build();
}
