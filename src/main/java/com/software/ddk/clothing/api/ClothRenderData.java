package com.software.ddk.clothing.api;

import net.fabricmc.loader.util.sat4j.core.Vec;
import net.minecraft.block.Block;
import net.minecraft.client.util.math.Vector3d;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;

public class ClothRenderData {
    //todo simplify/clean everything.
    private Block block;
    private EquipmentSlot slot;
    private float scaleX, scaleY, scaleZ;
    private float tX, tY, tZ;
    private boolean rotate = false;
    private Vector3f rotation;
    public static final Vector3f DEFAULT_SIZE = new Vector3f(0.5f, 0.5f, 0.5f);
    public static final Vector3f DEFAULT_POS = new Vector3f(-0.5f, -0.5f, -0.5f);

    public ClothRenderData(Block block, EquipmentSlot slot, float scaleX, float scaleY, float scaleZ, float tX, float tY, float tZ){
        this.block = block;
        this.slot = slot;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.tX = tX;
        this.tY = tY;
        this.tZ = tZ;
    }

    public ClothRenderData(Block block, EquipmentSlot slot, Vector3f scale, Vector3f translate){
        this.block = block;
        this.slot = slot;
        this.scaleX = scale.getX();
        this.scaleY = scale.getY();
        this.scaleZ = scale.getZ();
        this.tX = translate.getX();
        this.tY = translate.getY();
        this.tZ = translate.getZ();
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

    public void setRotationVector(Vector3f rotation){
        this.rotation = rotation;
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

    public Vector3f getScaleVector(){
        return new Vector3f(this.scaleX, this.scaleY, this.scaleZ);
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

    public Vector3f getTranslateVector(){
        return new Vector3f(this.tX, this.tY, this.tZ);
    }
}
