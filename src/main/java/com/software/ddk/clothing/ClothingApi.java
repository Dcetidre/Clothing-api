package com.software.ddk.clothing;

import com.software.ddk.clothing.example.DummyItem;
import com.software.ddk.clothing.example.DummyItem2;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClothingApi implements ModInitializer {
    public static final String MOD_ID = "clothing";

    //public static final Item DUMMY = new DummyItem();
    //public static final Item DUMMY2 = new DummyItem2();

    @Override
    public void onInitialize() {
        //Registry.register(Registry.ITEM, new Identifier(MOD_ID, "dummy"), DUMMY);
        //Registry.register(Registry.ITEM, new Identifier(MOD_ID, "dummy2"), DUMMY2);
    }
}
