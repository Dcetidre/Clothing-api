package com.software.ddk.clothing.api;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public interface ICloth{

    EquipmentSlot slotType();

    String clothId();

    String modId();

    default ClothRenderData renderData(){
        return null;
    }

    default boolean customModel(){
        return false;
    }

    default boolean customEquip(){
        return false;
    }

    default boolean multiLayer() {
        return false;
    }

    default int getColorOverlay(ItemStack stack){
        CompoundTag compoundTag = stack.getSubTag("display");
        return compoundTag != null && compoundTag.contains("coloroverlay", 99) ? compoundTag.getInt("coloroverlay") : 0xffffff;
    }

    default void setColorOverlay(ItemStack stack, int colorOverlay){
        stack.getOrCreateSubTag("display").putInt("coloroverlay", colorOverlay);
    }

    default boolean applyGlint(){
        return false;
    }

    default boolean[][] equipLayers(){
        return new boolean[][]{
                {false, false, false, false},
                {false, false, false, false}
        };
    }

    default void render(BipedEntityModel model, ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity living, int light, int overlay, float headYaw, float headPitch){
    }
}
