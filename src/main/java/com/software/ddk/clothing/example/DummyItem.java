package com.software.ddk.clothing.example;

import com.software.ddk.clothing.ClothingApi;
import com.software.ddk.clothing.api.ICloth;
import com.software.ddk.clothing.integration.TrinketsIntegration;
import com.software.ddk.clothing.render.ClothesManager;
import com.software.ddk.clothing.render.ClothesRenderLayer;
import dev.emi.trinkets.api.ITrinket;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class DummyItem extends Item implements ICloth, DyeableItem, ITrinket {

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

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return group.equals(SlotGroups.CHEST);
    }

    @Override
    public void render(String slot, MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, PlayerEntityModel<AbstractClientPlayerEntity> model, AbstractClientPlayerEntity player, float headYaw, float headPitch) {
        //trinket render
        //TrinketsIntegration.renderClothes(model, matrixStack, vertexConsumer, light, (LivingEntity) player);

    }
}
