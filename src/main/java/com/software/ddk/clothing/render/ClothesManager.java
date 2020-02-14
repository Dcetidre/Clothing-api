package com.software.ddk.clothing.render;

import com.software.ddk.clothing.api.ICloth;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ClothesManager {
    public static boolean[][] DEFAULT_EQUIP = new boolean[][]{{false, false, false, false},{false, false, false, false}};
    public static final float[] DEFAULT_COLOR = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

    public static boolean isCloth(ItemStack stack){
        return !stack.isEmpty() && stack.getItem() instanceof ICloth;
    }

    public static boolean[][] checkRender(int index, ItemStack stack, boolean[][] equipLayers){
        if (((ICloth) stack.getItem()).customEquip()){
            equipLayers= ((ICloth) stack.getItem()).equipLayers();
        } else {
            equipLayers[0][index] = true;
            equipLayers[1][index] = true;
        }
        return equipLayers;
    }

    public static float[] getColorFloat(int color){
        return new float[]{
                (float)(color >> 16 & 255) / 255.0F,
                (float)(color >> 8 & 255) / 255.0F,
                (float)(color & 255) / 255.0F,
                1.0f
        };
    }

    public static Identifier getTexture(ItemStack stack, int layer, boolean slim) {
        String cloth_id = ((ICloth) stack.getItem()).clothId();
        String cloth_modid = ((ICloth) stack.getItem()).modId();
        String modifier = "";
        if (slim){
            modifier = "_slim";
        }
        return new Identifier(cloth_modid, "textures/clothes/cloth_" + cloth_id + modifier + "_layer" + layer +".png");
    }

    public static float[][] getColors(ItemStack stack){
        if (stack.getItem() instanceof DyeableItem){
            int color = ((DyeableItem) stack.getItem()).getColor(stack);
            int colorOverlay = ((ICloth) stack.getItem()).getColorOverlay(stack);
            return new float[][]{getColorFloat(color), getColorFloat(colorOverlay)};
        } else {
            //reset or set tints to default values.
            return new float[][]{DEFAULT_COLOR.clone(), DEFAULT_COLOR.clone()};
        }
    }

    public static PlayerEntityModel setPartsVisibility(PlayerEntityModel model, boolean[][] equipLayers){
        model.head.visible = equipLayers[0][0];
        model.helmet.visible = equipLayers[1][0];
        //chest
        model.torso.visible = equipLayers[0][1];
        model.rightArm.visible = equipLayers[0][1];
        model.leftArm.visible = equipLayers[0][1];
        model.jacket.visible = equipLayers[1][1];
        model.rightSleeve.visible = equipLayers[1][1];
        model.leftSleeve.visible = equipLayers[1][1];
        //leggings
        model.leftLeg.visible = equipLayers[0][3];
        model.rightLeg.visible = equipLayers[0][3];
        //boots
        model.leftPantLeg.visible = equipLayers[0][2];
        model.rightPantLeg.visible = equipLayers[0][2];
        return model;
    }

    public static void renderOnStand(PlayerEntityModel model, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, float[] COLOR, float[] COLOROVERLAY, boolean multiLayer, boolean applyGlint, boolean slim){
        ICloth item = ((ICloth) stack.getItem());
        //base layer rendering
        matrices.push();
        VertexConsumer textureLayer0 = ItemRenderer.getArmorVertexConsumer(
                vertexConsumers, RenderLayer.getEntityCutoutNoCull(
                        ClothesManager.getTexture(stack, 0, slim)), false, applyGlint);

        model.render(matrices, textureLayer0, light, OverlayTexture.DEFAULT_UV, COLOR[0], COLOR[1], COLOR[2], COLOR[3]);
        //overlay layer rendering.
        if (multiLayer){
            VertexConsumer textureLayer1 = ItemRenderer.getArmorVertexConsumer(
                    vertexConsumers, RenderLayer.getEntityCutoutNoCull(
                            ClothesManager.getTexture(stack, 1, slim)), false, applyGlint);
            int customLight = item.applyOverlayLight() ? item.overlayLight() : light;
            model.render(matrices, textureLayer1, customLight, OverlayTexture.DEFAULT_UV, COLOROVERLAY[0], COLOROVERLAY[1], COLOROVERLAY[2], COLOROVERLAY[3]);
        }
        matrices.pop();
    }

}
