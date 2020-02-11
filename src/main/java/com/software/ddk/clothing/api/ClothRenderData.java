package com.software.ddk.clothing.api;

import net.minecraft.block.Block;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;

public class ClothRenderData {
    //todo simplify/clean everything.
    private Block block;
    private EquipmentSlot slot;
    private float scaleX, scaleY, scaleZ;
    private float tX, tY, tZ;
    private Vector3f rotation;
    private boolean rotate = false;
    private int rendermode = 0;
    public static final Vector3f DEFAULT_SIZE = new Vector3f(0.5f, 0.5f, 0.5f);
    public static final Vector3f DEFAULT_POS = new Vector3f(-0.5f, -0.5f, -0.5f);
    public static final int RENDER_BLOCKMODEL = 0;
    public static final int RENDER_ITEMMODEL = 1;

    public ClothRenderData(Block block, EquipmentSlot slot, float scaleX, float scaleY, float scaleZ, float tX, float tY, float tZ){
        setRenderMode(RENDER_BLOCKMODEL);
        this.block = block;
        this.slot = slot;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.tX = tX;
        this.tY = tY;
        this.tZ = tZ;
    }

    public ClothRenderData(EquipmentSlot slot, float scaleX, float scaleY, float scaleZ, float tX, float tY, float tZ){
        setRenderMode(RENDER_ITEMMODEL);
        this.slot = slot;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
        this.tX = tX;
        this.tY = tY;
        this.tZ = tZ;
    }

    public ClothRenderData(Block block, EquipmentSlot slot, Vector3f scale, Vector3f translate){
        setRenderMode(RENDER_BLOCKMODEL);
        this.block = block;
        this.slot = slot;
        this.scaleX = scale.getX();
        this.scaleY = scale.getY();
        this.scaleZ = scale.getZ();
        this.tX = translate.getX();
        this.tY = translate.getY();
        this.tZ = translate.getZ();
    }

    public ClothRenderData(EquipmentSlot slot){
        setRenderMode(RENDER_ITEMMODEL);
        this.slot = slot;
        this.scaleX = DEFAULT_SIZE.getX();
        this.scaleY = DEFAULT_SIZE.getY();
        this.scaleZ = DEFAULT_SIZE.getZ();
        this.tX = DEFAULT_POS.getX();
        this.tY = DEFAULT_POS.getY();
        this.tZ = DEFAULT_POS.getZ();
    }

    public void setSize(float x, float y, float z){
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    public void setSize(Vector3f size){
        this.scaleX = size.getX();
        this.scaleY = size.getY();
        this.scaleZ = size.getZ();
    }

    public void setTranslate(float x, float y, float z){
        this.tX = x;
        this.tY = y;
        this.tZ = z;
    }

    public void setTranslate(Vector3f translate){
        this.tX = translate.getX();
        this.tY = translate.getY();
        this.tZ = translate.getZ();
    }

    public int getRenderMode(){
        return this.rendermode;
    }

    public void setRenderMode(int mode){
        this.rendermode = mode;
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
