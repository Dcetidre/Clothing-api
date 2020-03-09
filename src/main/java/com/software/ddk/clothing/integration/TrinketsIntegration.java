package com.software.ddk.clothing.integration;

import com.software.ddk.clothing.api.ICloth;
import com.software.ddk.clothing.render.ClothesManager;
import dev.emi.trinkets.api.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class TrinketsIntegration {

    //DISABLED

    public static boolean isLoaded(){
        //check if trinkets is loaded.
        return FabricLoader.getInstance().isModLoaded("trinkets");
    }

    public static ItemStack[] getTrinketsEquip(PlayerEntity player){
        //if loaded check slots
        final TrinketComponent inv = TrinketsApi.getTrinketComponent(player);
        ItemStack mask = inv.getStack(SlotGroups.HEAD, Slots.MASK);
        ItemStack backpack = inv.getStack(SlotGroups.HEAD, Slots.MASK);
        ItemStack belt = inv.getStack(SlotGroups.HEAD, Slots.MASK);
        ItemStack aglet = inv.getStack(SlotGroups.HEAD, Slots.MASK);
        return new ItemStack[]{mask, backpack, belt, aglet};
    }

    public static void trinketsTryIntegration(){
        //initialize trinkets
        TrinketSlots.addSlot(SlotGroups.HEAD, Slots.MASK, new Identifier("trinkets", "textures/item/empty_trinket_slot_mask.png"));
        TrinketSlots.addSlot(SlotGroups.CHEST, Slots.BACKPACK, new Identifier("trinkets", "textures/item/empty_trinket_slot_backpack.png"));
        TrinketSlots.addSlot(SlotGroups.LEGS, Slots.BELT, new Identifier("trinkets", "textures/item/empty_trinket_slot_belt.png"));
        TrinketSlots.addSlot(SlotGroups.FEET, Slots.AGLET, new Identifier("trinkets", "textures/item/empty_trinket_slot_aglet.png"));
    }

    public static void renderClothes(PlayerEntityModel model, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, LivingEntity entity){
        boolean[][] equipLayers = ClothesManager.DEFAULT_EQUIP;

        if (TrinketsIntegration.isLoaded()){
            ItemStack[] stacks = TrinketsIntegration.getTrinketsEquip((PlayerEntity) entity);
            if (ClothesManager.isCloth(stacks[0])){
                doRendering(model, matrices, vertexConsumers, light, stacks[0], ClothesManager.checkRender(0, stacks[0], equipLayers));
            }
            if (ClothesManager.isCloth(stacks[1])){
                doRendering(model, matrices, vertexConsumers, light, stacks[1], ClothesManager.checkRender(1, stacks[1], equipLayers));
            }
            if (ClothesManager.isCloth(stacks[2])){
                doRendering(model, matrices, vertexConsumers, light, stacks[2], ClothesManager.checkRender(2, stacks[2], equipLayers));
            }
            if (ClothesManager.isCloth(stacks[3])){
                doRendering(model, matrices, vertexConsumers, light, stacks[3], ClothesManager.checkRender(3, stacks[3], equipLayers));
            }
        }
    }

    private static void doRendering(PlayerEntityModel playerEntityModel, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ItemStack stack, boolean[][] equipLayers){
        matrices.push();
        matrices.scale(1.0f, 1.0f, 1.0f);
        boolean multiLayer = ((ICloth) stack.getItem()).multiLayer();
        boolean applyGlint = ((ICloth) stack.getItem()).applyGlint();
        float[][] colors = ClothesManager.getColors(stack);

        PlayerEntityModel model = ClothesManager.setPartsVisibility((PlayerEntityModel) playerEntityModel, equipLayers);
        ClothesManager.renderOnStand(model, matrices, vertexConsumers, light, stack, colors[0], colors[1], multiLayer, applyGlint, true);
        matrices.pop();
    }

}
