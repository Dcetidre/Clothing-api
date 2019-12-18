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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;

import java.util.Vector;

public class ClothesRenderLayer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {
	private boolean slim;
	//0 head 1 chest 2 legs 3 feet
	private boolean[] equip = new boolean[]{false, false, false, false};
	private boolean[] overlay = new boolean[]{false, false, false, false};
	float r = 1.0f;
	float g = 1.0f;
	float b = 1.0f;

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
			boolean[][] render = checkRender(0, head, equip, overlay);
			doRendering(matrices, vertexConsumers, light, living, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, head, render[0], render[1]);
		}
		if (!chest.isEmpty() && chest.getItem() instanceof ICloth){
			boolean[][] render = checkRender(1, chest, equip, overlay);
			doRendering(matrices, vertexConsumers, light, living, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, chest, render[0], render[1]);
		}
		if (!legs.isEmpty() && legs.getItem() instanceof ICloth){
			boolean[][] render = checkRender(2, legs, equip, overlay);
			doRendering(matrices, vertexConsumers, light, living, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, legs, render[0], render[1]);
		}
		if (!feet.isEmpty() && feet.getItem() instanceof ICloth){
			boolean[][] render = checkRender(3, feet, equip, overlay);
			doRendering(matrices, vertexConsumers, light, living, limbAngle, limbDistance, tickDelta, customAngle, headYaw, headPitch, feet, render[0], render[1]);
		}
	}

	private boolean[][] checkRender(int index, ItemStack stack, boolean[] equip, boolean[] overlay){
		if (((ICloth) stack.getItem()).customEquip()){
			equip = ((ICloth) stack.getItem()).equip();
			overlay = ((ICloth) stack.getItem()).overlay();
		} else {
			equip[index] = true;
			overlay[index] = true;
		}
		return new boolean[][]{equip, overlay};
	}

	private void doRendering(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T living, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch, ItemStack stack, boolean[] equip, boolean[] overlay){
		matrices.push();
		matrices.scale(1.0f, 1.0f, 1.0f);

		VertexConsumer myVertexConsumer = ItemRenderer
				.getArmorVertexConsumer(vertexConsumers,
						RenderLayer.getEntityCutoutNoCull(
								this.getTexture(stack)), false, false);

		if (stack.getItem() instanceof DyeableItem){
			int color = ((DyeableItem) stack.getItem()).getColor(stack);
			r = (float)(color >> 16 & 255) / 255.0F;
			g = (float)(color >> 8 & 255) / 255.0F;
			b = (float)(color & 255) / 255.0F;
		}

		PlayerEntityModel biped = ((PlayerEntityModel) this.getContextModel());

		//head
		biped.head.visible = equip[0];
		biped.helmet.visible = overlay[0];
		//chest
		biped.torso.visible = equip[1];
		biped.rightArm.visible = equip[1];
		biped.leftArm.visible = equip[1];
		biped.jacket.visible = overlay[1];
		biped.rightSleeve.visible = overlay[1];
		biped.leftSleeve.visible = overlay[1];

		//leggings
		biped.leftLeg.visible = equip[2];
		biped.rightLeg.visible = equip[2];

		//boots
		biped.leftPantLeg.visible = equip[3];
		biped.rightPantLeg.visible = equip[3];

		System.out.println("rgb: " + r + " " + g + " " + b);
		biped.render(matrices, myVertexConsumer, light, OverlayTexture.DEFAULT_UV, r, g, b, 1.0F);
		((ICloth) stack.getItem()).render(biped, stack, matrices, vertexConsumers, living, light, OverlayTexture.DEFAULT_UV, headYaw, headPitch);
		renderBlockModel(biped, stack, matrices, vertexConsumers, living, light, OverlayTexture.DEFAULT_UV, headYaw, headPitch);
		matrices.pop();
	}

	private void renderBlockModel(PlayerEntityModel biped, ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumer,T living, int light, int overlay, float headYaw, float headPitch){
		if (((ICloth)stack.getItem()).customModel()){
            ClothRenderData renderData = ((ICloth)stack.getItem()).renderData();
			Block block = renderData.getRenderBlock();

			renderBySlot(renderData, matrices, ((ICloth) stack.getItem()).slotType(), living, biped, headYaw, headPitch);
			MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(block.getDefaultState(), matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV);

		}
	}

	private void renderBySlot(ClothRenderData renderData, MatrixStack matrices, EquipmentSlot slot,T living, PlayerEntityModel model, float headYaw, float headPitch){
    	if (slot.equals(EquipmentSlot.HEAD)){
            if (living.isInSwimmingPose() || living.isFallFlying()){
                //matrices.multiply(new Quaternion(VECTOR_Z, model.head.roll, true));
                //matrices.multiply(new Quaternion(VECTOR_Y, headYaw, true));
                //matrices.multiply(new Quaternion(VECTOR_X, -45.0F, true));
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(model.head.roll));
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-45.0F));
			} else {
                if (living.isInSneakingPose() && !model.riding){
                    matrices.translate(0.0f, 0.25f, 0.0f);
                }
            }

            //matrices.multiply(new Quaternion(VECTOR_Y, headYaw, true));
            //matrices.multiply(new Quaternion(VECTOR_X, headPitch, true));
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(headYaw));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(headPitch));

			matrices.translate(0.0f, -0.25f, 0.0f);

        } else if (slot.equals(EquipmentSlot.CHEST)){
            if (living.isInSneakingPose() && !model.riding && !living.isSwimming()){
                matrices.translate(0.0f, 0.2f, 0.0f);
                //matrices.multiply(new Quaternion(VECTOR_X, model.torso.pitch * 57.5f, true));
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(model.torso.pitch * 57.5f));
            }
            //matrices.multiply(new Quaternion(VECTOR_Y, model.torso.yaw * 57.5f, true));
            matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(model.torso.yaw * 57.5f));
            matrices.translate(0.0f, 0.4f, -0.16f);
        }

        //custom
		if (renderData.isRotable()){
			//matrices.multiply(new Quaternion(VECTOR_X, renderData.getRotation().getX(), true));
			//matrices.multiply(new Quaternion(VECTOR_Y, renderData.getRotation().getY(), true));
			//matrices.multiply(new Quaternion(VECTOR_Z, renderData.getRotation().getZ(), true));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(renderData.getRotation().getX()));
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(renderData.getRotation().getY()));
			matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(renderData.getRotation().getZ()));
		}
		matrices.scale(renderData.getScaleX(), renderData.getScaleY(), renderData.getScaleZ());
        matrices.translate(renderData.gettX(), renderData.gettY(), renderData.gettZ());
    }

	private Identifier getTexture(ItemStack stack) {
		String cloth_id = ((ICloth) stack.getItem()).clothId();
		String cloth_modid = ((ICloth) stack.getItem()).modId();
		String modifier = "";
		if (slim){
			modifier = "_slim";
		}
		return new Identifier(cloth_modid, "textures/clothes/cloth_" + cloth_id + modifier + ".png");
	}
}