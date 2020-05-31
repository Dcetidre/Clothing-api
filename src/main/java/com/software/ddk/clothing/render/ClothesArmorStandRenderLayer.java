package com.software.ddk.clothing.render;

import com.software.ddk.clothing.api.ICloth;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ClothesArmorStandRenderLayer <T extends LivingEntity, M extends BipedEntityModel<T>, A extends PlayerEntityModel<T>> extends FeatureRenderer<T, M> {
    private final A modelBody;
    private boolean[][] equipLayers = ClothesManager.DEFAULT_EQUIP;

    public ClothesArmorStandRenderLayer(FeatureRendererContext<T, M> context, A entityModel) {
        super(context);
        this.modelBody = entityModel;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        ItemStack head = entity.getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chest = entity.getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legs = entity.getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feet = entity.getEquippedStack(EquipmentSlot.FEET);

        if (ClothesManager.isCloth(head)){
            doRendering(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, head, ClothesManager.checkRender(0, head, equipLayers));
        }
        if (ClothesManager.isCloth(chest)){
            doRendering(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, chest, ClothesManager.checkRender(1, chest, equipLayers));
        }
        if (ClothesManager.isCloth(legs)){
            doRendering(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, legs, ClothesManager.checkRender(2, legs, equipLayers));
        }
        if (ClothesManager.isCloth(feet)){
            doRendering(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, feet, ClothesManager.checkRender(3, feet, equipLayers));
        }

    }

    private void doRendering(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T living, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch, ItemStack stack, boolean[][] equipLayers){
        matrices.push();
        matrices.scale(1.0f, 1.0f, 1.0f);
        boolean multiLayer = ((ICloth) stack.getItem()).multiLayer();
        boolean applyGlint = ((ICloth) stack.getItem()).applyGlint();
        float[][] colors = ClothesManager.getColors(stack);

        //todo this is ugly.
        A model = (A) ClothesManager.setPartsVisibility((A) modelBody, equipLayers);
        (this.getContextModel()).setAttributes(model);

        model.animateModel(living, limbAngle, limbDistance, tickDelta);
        model.setAngles(living, limbAngle, limbDistance, customAngle, headYaw, headPitch);
        model.rightArm.pitch = 0.0f;
        model.rightArm.roll = 0.0f;
        model.rightSleeve.pitch = 0.0f;
        model.rightSleeve.roll = 0.0f;
        model.leftArm.pitch = 0.0f;
        model.leftArm.roll = 0.0f;
        model.leftSleeve.pitch = 0.0f;
        model.leftSleeve.roll = 0.0f;

        ClothesManager.renderOnStand(model, matrices, vertexConsumers, light, stack, colors[0], colors[1], multiLayer, applyGlint, false);
        matrices.pop();
    }

}
