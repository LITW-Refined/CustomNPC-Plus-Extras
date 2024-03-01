package de.pilz.customnpcsadvanced;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class ItemRegistry {

    public static final Item itemWandAdv = new Item().setUnlocalizedName("wandAdv")
        .setTextureName("wood_hoe")
        .setCreativeTab(CreativeTabs.tabAllSearch);

    public static void register() {
        GameRegistry.registerItem(itemWandAdv, "wandAdv");
    }
}
