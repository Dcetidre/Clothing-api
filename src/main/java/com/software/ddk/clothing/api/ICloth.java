package com.software.ddk.clothing.api;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface ICloth{

    public EquipmentSlot slotType();

    public String clothId();

    public String modId();

    public default ClothRenderData renderData(){
        return null;
    }

    public default boolean customModel(){
        return false;
    }

    public default boolean customEquip(){
        return false;
    }

    public default boolean[] overlay(){
        return new boolean[]{false, false, false, false};
    }

    public default boolean[] equip(){
        return new boolean[]{false, false, false, false};
    }

    public default void render(BipedEntityModel model, ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, LivingEntity living, int light, int overlay, float headYaw, float headPitch){

    }
}
