package com.software.ddk.clothing.api;

import net.minecraft.block.Block;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;

public class ClothRenderData {
    private Block block;
    private EquipmentSlot slot;
    private float scaleX, scaleY, scaleZ;
    private float tX, tY, tZ;
    private boolean rotate = false;
    private Vector3f rotation;
    public static final Vector3f DEFAULT_SIZE = new Vector3f(0.5f, 0.5f, 0.5f);
    public static final Vector3f DEFAULT_POS = new Vector3f(-0.5f, -0.5f, -0.5f);

    public ClothRenderData(Block block, EquipmentSlot slot, float scaleX, float scaleY, float scaleZ, float tX, float tY, float tZ){
        //todo cambiar variables a Vector3f;
        this.block = block;
        this.slot = slot;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.tX = tX;
        this.tY = tY;
        this.tZ = tZ;
    }

    public boolean isRotable(){
        return this.rotate;
    }

    public void setRotable(boolean enable){
        this.rotate = enable;
    }

    public void setRotation(float x, float y, float z){
        this.rotation = new Vector3f(x, y, z);
    }

    public Vector3f getRotation(){
        return this.rotation;
    }

    public Block getRenderBlock() {
        return this.block;
    }

    public EquipmentSlot getSlot() {
        return this.slot;
    }

    public float getScaleX() {
        return this.scaleX;
    }

    public float getScaleY() {
        return this.scaleY;
    }

    public float getScaleZ() {
        return this.scaleZ;
    }

    public float gettX() {
        return this.tX;
    }

    public float gettY() {
        return this.tY;
    }

    public float gettZ() {
        return this.tZ;
    }
}
