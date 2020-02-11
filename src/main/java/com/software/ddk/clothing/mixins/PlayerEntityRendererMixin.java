package com.software.ddk.clothing.mixins;

import com.software.ddk.clothing.api.ICloth;
import com.software.ddk.clothing.render.ClothesManager;
import com.software.ddk.clothing.render.ClothesRenderLayer;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	private boolean slim;
	public PlayerEntityRendererMixin(EntityRenderDispatcher entityRenderDispatcher, PlayerEntityModel<AbstractClientPlayerEntity> entityModel, float f) {
		super(entityRenderDispatcher, entityModel, f);
	}

	@Inject(method = "<init>(Lnet/minecraft/client/render/entity/EntityRenderDispatcher;Z)V", at = @At("RETURN"))
	public void onInit(EntityRenderDispatcher entityRenderDispatcher, boolean slim, CallbackInfo ci) {
		this.addFeature(new ClothesRenderLayer<>(this, slim));
		this.slim = slim;
	}

	@Inject(method = "renderArm", at = @At("RETURN"))
	private void onRenderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci){
		ItemStack chest = player.getEquippedStack(EquipmentSlot.CHEST);

		if (ClothesManager.isCloth(chest)){
			float[][] colors = ClothesManager.getColors(chest);
			//base layer rendering
			VertexConsumer textureLayer0 = ItemRenderer.getArmorVertexConsumer(
					vertexConsumers, RenderLayer.getEntityCutoutNoCull(
							ClothesManager.getTexture(chest, 0, slim)), false, ((ICloth) chest.getItem()).applyGlint());

			arm.render(matrices, textureLayer0, light, OverlayTexture.DEFAULT_UV, colors[0][0], colors[0][1], colors[0][2], colors[0][3]);
			//overlay layer rendering.
			if (((ICloth) chest.getItem()).multiLayer()){
				VertexConsumer textureLayer1 = ItemRenderer.getArmorVertexConsumer(
						vertexConsumers, RenderLayer.getEntityCutoutNoCull(
								ClothesManager.getTexture(chest, 1, slim)), false, ((ICloth) chest.getItem()).applyGlint());
				arm.render(matrices, textureLayer1, light, OverlayTexture.DEFAULT_UV, colors[1][0], colors[1][1], colors[1][2], colors[1][3]);
			}
		}
	}

}
