package com.software.ddk.clothing.example;

import com.software.ddk.clothing.ClothingApi;
import com.software.ddk.clothing.api.ClothRenderData;
import com.software.ddk.clothing.api.ICloth;
import com.software.ddk.clothing.render.ClothesRenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
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
                {false, true, false, false},
                {false, false, false, false}
        };
    }

    @Override
    public boolean customModel() {
        return true;
    }

    @Override
    public ClothRenderData renderData() {
        //create model renderer and render as item model.
        ClothRenderData clothRenderData = new ClothRenderData(EquipmentSlot.HEAD,
                1.3f, 1.1f, 1.2f,
                0.0f, 0.5f, 0.08f);
        clothRenderData.setRenderMode(ClothRenderData.RENDER_ITEMMODEL);
        clothRenderData.setRotable(true);
        clothRenderData.setRotation(180.0f, 0.0f, 0.0f);
        return clothRenderData;
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
