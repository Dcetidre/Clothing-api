# Clothing Api
Library for adding skin layer textures as dressable items.

basic use sample:

    public class myItem extends Item implements ICloth {
        public myItem() {
            super(new Item.Settings()
            .maxCount(1)
            .group(ItemGroup.MISC));
        }

        @Override
        public EquipmentSlot slotType() {
            return EquipmentSlot.HEAD;
        }
    
        @Override
        public String clothId() {
            return "myclothid";
        }
    
        @Override
        public String modId() {
            return MyMod.MODID;
        }
    }
 
* textures need to be located at:
        
        /assets/mod-id/textures/clothes/cloth_myclothid_layer0.png
        /assets/mod-id/textures/clothes/cloth_myclothid_slim_layer0.png
        
note: when multiLayer() is enabled, the respective "_layer1" files needs to be located too. Textures are basically player skin textures.


* You can render custom texture parts overriding customEquip() and equipLayers():
    
        @Override
        public boolean customEquip() {
            return true;
        }

        @Override
        public boolean[][] equipLayers() {
            return new boolean[][]{
                       //head chest legs boots
                       {false, false, false, false},
                       //helmet jacket pants boots_over
                       {false, false, false, false}
            };
        }


* You can render a custom blockmodel overriding customModel and renderData:
        
            @Override
            public boolean customModel() {
                return true;
            }
        
            @Override
            public ClothRenderData renderData() {
                //create a ClothRenderData object and feed size and position of block model
                ClothRenderData clothRenderData = new ClothRenderData(Blocks.GLASS, EquipmentSlot.HEAD, //the block to render and equipment slot
                        1.1f, 1.1f, 1.1f, //size x y z
                        -0.5f, -0.5f, -0.5f); //position x y z
                clothRenderData.setRotable(true); //enable rotation
                clothRenderData.setRotation(0.0f, 0.0f, 0.0f); //rotation x y z
                return clothRenderData; //return object.
            }