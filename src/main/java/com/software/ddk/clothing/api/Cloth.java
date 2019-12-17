package com.software.ddk.clothing.api;

import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;


public class Cloth extends Item {
    private EquipmentSlot slot;
    private String id;
    private String modid = "minecraft";
    private boolean customEquip = false;
    private boolean customModel = false;
    private boolean[] equip = new boolean[]{false, false, false, false};
    private boolean[] overlay = new boolean[]{false, false, false, false};
    private ClothRenderData renderData;

    public Cloth(EquipmentSlot slot, String id, Settings settings) {
        super(settings);
        this.slot = slot;
        this.id = id;
    }

    public void CustomModelRender(Block block, EquipmentSlot slot, float scaleX, float scaleY, float scaleZ, float tX, float tY, float tZ){
        this.renderData = new ClothRenderData(block, slot, scaleX, scaleY, scaleZ, tX, tY, tZ);
    }

    public ClothRenderData CustomModelRender(ClothRenderData clothRenderData){
        this.renderData = clothRenderData;
        return this.renderData;
    }

    public ClothRenderData getRenderData(){
        return this.renderData;
    }

    public void setCustomModel(boolean customModel) {
        this.customModel = customModel;
    }

    public boolean isCustomModel(){
        return this.customModel;
    }

    public void setCustomEquip(boolean customEquip) {
        this.customEquip = customEquip;
    }

    public boolean isCustomEquip(){
        return this.customEquip;
    }

    public void setEquip(boolean head, boolean chest, boolean legs, boolean boots){
        this.equip = new boolean[]{head, chest, legs, boots};
    }

    public boolean[] getEquip(){
        return this.equip;
    }

    public void setOverlay(boolean helmet, boolean jacket, boolean pants, boolean boots){
        this.overlay = new boolean[]{helmet, jacket, pants, boots};
    }

    public boolean[] getOverlay(){
        return this.overlay;
    }

    protected void setModId(String modid){
        this.modid = modid;
    }

    public String getModId(){
        return this.modid;
    }

    public String getClothId(){
        return this.id;
    }

    public EquipmentSlot getSlotType() {
        return this.slot;
    }
}
