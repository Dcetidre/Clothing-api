package com.software.ddk.clothing.render;

import com.software.ddk.clothing.api.ClothRenderData;
import com.software.ddk.clothing.api.ICloth;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
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
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ClothesRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private boolean slim;
	//0 head 1 chest 2 legs 3 feet
	private boolean[][] equipLayers = new boolean[][]{{false, false, false, false},{false, false, false, false}};
	private final float[] DEFAULT_COLOR = new float[]{1.0f, 1.0f, 1.0f, 1.0f};

	public ClothesRenderLayer(FeatureRendererContext<T, M> render, boolean slim) {
		super(render);
		this.slim = slim;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T living, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
		ItemStack head = living.getEquippedStack(EquipmentSlot.HEAD);
		ItemStack chest = living.getEquippedStack(EquipmentSlot.CHEST);
		ItemStack legs = living.getEquippedStack(EquipmentSlot.LEGS);
		ItemStack feet = living.getEquippedStack(EquipmentSlot.FEET);

		if (!head.isEmpty() && head.getItem() instanceof ICloth){
			boolean[][] render = checkRender(0, head, equipLayers);
			doRendering(matrices, vertexConsumers, light, living, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, head, render);
		}
		if (!chest.isEmpty() && chest.getItem() instanceof ICloth){
			boolean[][] render = checkRender(1, chest, equipLayers);
			doRendering(matrices, vertexConsumers, light, living, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, chest, render);
		}
		if (!legs.isEmpty() && legs.getItem() instanceof ICloth){
			boolean[][] render = checkRender(2, legs, equipLayers);
			doRendering(matrices, vertexConsumers, light, living, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, legs, render);
		}
		if (!feet.isEmpty() && feet.getItem() instanceof ICloth){
			boolean[][] render = checkRender(3, feet, equipLayers);
			doRendering(matrices, vertexConsumers, light, living, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, feet, render);
		}
	}

	private boolean[][] checkRender(int index, ItemStack stack, boolean[][] equipLayers){
		if (((ICloth) stack.getItem()).customEquip()){
			equipLayers= ((ICloth) stack.getItem()).equipLayers();
		} else {
			equipLayers[0][index] = true;
			equipLayers[1][index] = true;
		}
		return equipLayers;
	}

	private void doRendering(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T living, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch, ItemStack stack, boolean[][] equipLayers){
		matrices.push();
		matrices.scale(1.0f, 1.0f, 1.0f);
		boolean multiLayer = ((ICloth) stack.getItem()).multiLayer();
		boolean applyGlint = ((ICloth) stack.getItem()).applyGlint();
		float[] COLOR, COLOROVERLAY;

		VertexConsumer textureLayer0 = ItemRenderer.getArmorVertexConsumer(
				vertexConsumers, RenderLayer.getEntityCutoutNoCull(
				this.getTexture(stack, 0)), false, applyGlint);

		if (stack.getItem() instanceof DyeableItem){
			int color = ((DyeableItem) stack.getItem()).getColor(stack);
			int colorOverlay = ((ICloth) stack.getItem()).getColorOverlay(stack);
			COLOR = getColorFloat(color);
			COLOROVERLAY = getColorFloat(colorOverlay);
		} else {
			//reset or set tints to default values.
			COLOR = DEFAULT_COLOR.clone();
			COLOROVERLAY = DEFAULT_COLOR.clone();
		}

		PlayerEntityModel biped = ((PlayerEntityModel) this.getContextModel());
		//head
		biped.head.visible = equipLayers[0][0];
		biped.helmet.visible = equipLayers[1][0];
		//chest
		biped.torso.visible = equipLayers[0][1];
		biped.rightArm.visible = equipLayers[0][1];
		biped.leftArm.visible = equipLayers[0][1];
		biped.jacket.visible = equipLayers[1][1];
		biped.rightSleeve.visible = equipLayers[1][1];
		biped.leftSleeve.visible = equipLayers[1][1];
		//leggings
		biped.leftLeg.visible = equipLayers[0][3];
		biped.rightLeg.visible = equipLayers[0][3];
		//boots
		biped.leftPantLeg.visible = equipLayers[0][2];
		biped.rightPantLeg.visible = equipLayers[0][2];

		biped.render(matrices, textureLayer0, light, OverlayTexture.DEFAULT_UV, COLOR[0], COLOR[1], COLOR[2], COLOR[3]);

		//overlay layer rendering.
		if (multiLayer){
			VertexConsumer textureLayer1 = ItemRenderer.getArmorVertexConsumer(
					vertexConsumers, RenderLayer.getEntityCutoutNoCull(
					this.getTexture(stack, 1)), false, applyGlint);
			biped.render(matrices, textureLayer1, light, OverlayTexture.DEFAULT_UV, COLOROVERLAY[0], COLOROVERLAY[1], COLOROVERLAY[2], COLOROVERLAY[3]);
		}

		((ICloth) stack.getItem()).render(biped, stack, matrices, vertexConsumers, living, light, OverlayTexture.DEFAULT_UV, headYaw, headPitch);
		renderBlockModel(biped, stack, matrices, vertexConsumers, living, light, OverlayTexture.DEFAULT_UV, headYaw, headPitch);
		matrices.pop();
	}

	private void renderBlockModel(PlayerEntityModel biped, ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumer,T living, int light, int overlay, float headYaw, float headPitch){
		if (((ICloth)stack.getItem()).customModel()){
            ClothRenderData renderData = ((ICloth)stack.getItem()).renderData();
            switch (renderData.getRenderMode()){
                case 0:
                    //block render
                    Block block = renderData.getRenderBlock();
                    renderBySlot(renderData, matrices, ((ICloth) stack.getItem()).slotType(), living, biped, headYaw, headPitch);
                    MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(block.getDefaultState(), matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
                    break;
                case 1:
                    //item render
                    renderBySlot(renderData, matrices, ((ICloth) stack.getItem()).slotType(), living, biped, headYaw, headPitch);
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

    private float[] getColorFloat(int color){
		return new float[]{
				(float)(color >> 16 & 255) / 255.0F,
				(float)(color >> 8 & 255) / 255.0F,
				(float)(color & 255) / 255.0F,
				1.0f
		};
	}

	private Identifier getTexture(ItemStack stack, int layer) {
		String cloth_id = ((ICloth) stack.getItem()).clothId();
		String cloth_modid = ((ICloth) stack.getItem()).modId();
		String modifier = "";
		if (slim){
			modifier = "_slim";
		}
		return new Identifier(cloth_modid, "textures/clothes/cloth_" + cloth_id + modifier + "_layer" + layer +".png");
	}
}