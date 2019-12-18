package com.software.ddk.clothing;

import com.software.ddk.clothing.example.DummyItem;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClothingApi implements ModInitializer {
    public static final String MOD_ID = "clothing";

    @Override
    public void onInitialize() {
        //Registry.register(Registry.ITEM, new Identifier(MOD_ID, "dummy"), new DummyItem());
    }
}
