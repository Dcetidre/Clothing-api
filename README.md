# Clothing-api
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
        
        /assets/mod-id/textures/clothes/cloth_myclothid.png
        /assets/mod-id/textures/clothes/cloth_myclothid_slim.png
        
textures are basically player skin textures.


* You can render custom texture parts overriding customEquip as well equip and overlay (will change later.):
    
        @Override
        public boolean customEquip() {
            return true;
        }

        @Override
        public boolean[] equip() {
            //head chest legs feet
            return new boolean[]{false, false, false, false};
        }

        @Override
        public boolean[] overlay() {
            //helmet jacket pants boots
            return new boolean[]{false, false, false, false};
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