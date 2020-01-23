package com.software.ddk.clothing.example;

import com.software.ddk.clothing.ClothingApi;
import com.software.ddk.clothing.api.ICloth;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class DummyItem2 extends Item implements ICloth {
    public DummyItem2() {
        super(new Item.Settings().group(ItemGroup.MISC));
    }

    @Override
    public EquipmentSlot slotType() {
        return EquipmentSlot.CHEST;
    }

    @Override
    public String clothId() {
        return "simple";
    }

    @Override
    public String modId() {
        return ClothingApi.MOD_ID;
    }
}
