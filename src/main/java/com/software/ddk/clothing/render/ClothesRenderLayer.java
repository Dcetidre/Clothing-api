package com.software.ddk.clothing.render;

import com.software.ddk.clothing.api.ClothRenderData;
import com.software.ddk.clothing.api.ICloth;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class ClothesRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private boolean slim;
	private boolean[][] equipLayers = ClothesManager.DEFAULT_EQUIP;

	public ClothesRenderLayer(FeatureRendererContext<T, M> render, boolean slim) {
		super(render);
		this.slim = slim;
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

		PlayerEntityModel model = ClothesManager.setPartsVisibility((PlayerEntityModel) this.getContextModel(), equipLayers);
		renderOnPlayer(model, matrices, vertexConsumers, light, living, headYaw, headPitch, stack, colors[0], colors[1], multiLayer, applyGlint);

		matrices.pop();
	}

	private void renderOnPlayer(PlayerEntityModel model, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T living, float headYaw, float headPitch, ItemStack stack, float[] COLOR, float[] COLOROVERLAY, boolean multiLayer, boolean applyGlint){
		ICloth item = ((ICloth) stack.getItem());

		//base layer rendering
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


		item.render(model, stack, matrices, vertexConsumers, living, light, OverlayTexture.DEFAULT_UV, headYaw, headPitch);
		renderBlockModel(model, stack, matrices, vertexConsumers, living, light, OverlayTexture.DEFAULT_UV, headYaw, headPitch);
	}

	private void renderBlockModel(PlayerEntityModel model, ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumer,T living, int light, int overlay, float headYaw, float headPitch){
		if (((ICloth)stack.getItem()).customModel()){
            ClothRenderData renderData = ((ICloth)stack.getItem()).renderData();
            switch (renderData.getRenderMode()){
                case 0:
                    //block render
                    Block block = renderData.getRenderBlock();
                    renderBySlot(renderData, matrices, ((ICloth) stack.getItem()).slotType(), living, model, headYaw, headPitch);
                    MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(block.getDefaultState(), matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
                    break;
                case 1:
                    //item render
                    renderBySlot(renderData, matrices, ((ICloth) stack.getItem()).slotType(), living, model, headYaw, headPitch);
                    MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumer);
                    break;
            }
		}
	}

	private void renderBySlot(ClothRenderData renderData, MatrixStack matrices, EquipmentSlot slot,T living, PlayerEntityModel model, float headYaw, float headPitch){
    	//based on trinkets.
    	if (slot.equals(EquipmentSlot.HEAD)){
            if (living.isInSwimmingPose() || living.isFallFlying()){
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(model.head.roll));
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-45.0F));
			} else {
                if (living.isInSneakingPose() && !model.riding){
                    matrices.translate(0.0f, 0.25f, 0.0f);
                }
            }

            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(headPitch));
			matrices.translate(0.0f, -0.25f, 0.0f);

        } else if (slot.equals(EquipmentSlot.CHEST)){
            if (living.isInSneakingPose() && !model.riding && !living.isSwimming()){
                matrices.translate(0.0f, 0.2f, 0.0f);
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(model.torso.pitch * 57.5f));
            }
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(model.torso.yaw * 57.5f));
            matrices.translate(0.0f, 0.4f, -0.16f);
        }

        //custom
		if (renderData.isRotable()){
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(renderData.getRotation().getX()));
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(renderData.getRotation().getY()));
			matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(renderData.getRotation().getZ()));
		}
		matrices.scale(renderData.getScaleX(), renderData.getScaleY(), renderData.getScaleZ());
        matrices.translate(renderData.gettX(), renderData.gettY(), renderData.gettZ());
    }

}