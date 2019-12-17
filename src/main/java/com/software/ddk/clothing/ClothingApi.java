package com.software.ddk.clothing;

import net.fabricmc.api.ModInitializer;

public class ClothingApi implements ModInitializer {
    public static final String MOD_ID = "clothing";
    //public static final Block GALERA = Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "galera"), new Block(FabricBlockSettings.of(Material.WOOL).strength(1.0f, 1.0f).build()));

    @Override
    public void onInitialize() {
        //Registry.register(Registry.ITEM, new Identifier(MOD_ID, "head"), new HeadItem());
        //Registry.register(Registry.ITEM, new Identifier(MOD_ID, "chest"), new ChestItem());
        //Registry.register(Registry.ITEM, new Identifier(MOD_ID, "legs"), new LegsItem());
        //Registry.register(Registry.ITEM, new Identifier(MOD_ID, "boots"), new BootsItem());
    }
}
