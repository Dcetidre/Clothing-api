package com.software.ddk.clothing.example;

import com.software.ddk.clothing.ClothingApi;
import com.software.ddk.clothing.api.ICloth;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class DummyItem extends Item implements ICloth, DyeableItem {

    public DummyItem() {
        super(new Item.Settings().group(ItemGroup.MISC));
    }

    @Override
    public boolean customEquip() {
        return true;
    }

    @Override
    public boolean[][] equipLayers() {
        return new boolean[][]{
                {true, true, true, true},
                {true, true, true, true}
        };
    }

    @Override
    public boolean multiLayer() {
        return true;
    }

    @Override
    public int getColor(ItemStack stack) {
        return 0x0000ff;
    }

    @Override
    public int getColorOverlay(ItemStack stack) {
        return 0xff0000;
    }

    @Override
    public boolean applyOverlayLight() {
        return true;
    }

    @Override
    public int overlayLight() {
        return 255;
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
