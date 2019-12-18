package com.software.ddk.clothing.mixins;

import com.software.ddk.clothing.api.ICloth;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin{

    @Inject( at = @At("HEAD"), method = "getPreferredEquipmentSlot", cancellable = true)
    private static void onGetPreferredEquipmentSlot(ItemStack itemStack_1, CallbackInfoReturnable<EquipmentSlot> cir) {
        Item item = itemStack_1.getItem();
        if (item instanceof ICloth){
            cir.setReturnValue(((ICloth)item).slotType());
        }
    }

}
