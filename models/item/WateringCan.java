    package models.item;

    import enums.items.ToolType;

    public class WateringCan extends Tool {
        private int capacity;
        private int filledCapacity;
        public WateringCan(ToolType toolType, int number) {
            super(toolType, number);
            if(toolType.equals(ToolType.PrimitiveWateringCan)){
                this.capacity = 40;
            }
            else if(toolType.equals(ToolType.CopperWateringCan)){
                this.capacity = 55;
            }
            else if(toolType.equals(ToolType.IridiumWateringCan)){
                this.capacity = 100;
            }
            else if(toolType.equals(ToolType.GoldenWateringCan)){
                this.capacity = 85;
            }
            else if(toolType.equals(ToolType.IronicWateringCan)){
                this.capacity = 70;
            }
            this.filledCapacity = 0;
        }

        public void fill(){
            this.filledCapacity = capacity;
        }

        @Override
        protected int calculateEnergyConsumption() {
            return 0;
        }
    }
