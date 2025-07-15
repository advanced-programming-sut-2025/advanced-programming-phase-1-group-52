    package com.example.main.models.item;

    import com.example.main.enums.items.ToolType;

    public class WateringCan extends Tool {
        private int capacity;
        private int filledCapacity;

        public WateringCan(ToolType toolType, int number) {
            super(toolType, number);
            if(toolType.equals(ToolType.Watering_Can)){
                this.capacity = 40;
            }
            else if(toolType.equals(ToolType.Copper_Watering_Can)){
                this.capacity = 55;
            }
            else if(toolType.equals(ToolType.Iridium_Watering_Can)){
                this.capacity = 100;
            }
            else if(toolType.equals(ToolType.Gold_Watering_Can)){
                this.capacity = 85;
            }
            else if(toolType.equals(ToolType.Steel_Watering_Can)){
                this.capacity = 70;
            }
            this.filledCapacity = 0;
        }

        public void fill(){
            this.filledCapacity = capacity;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }

        public int getFilledCapacity() {
            return filledCapacity;
        }

        public void setFilledCapacity(int filledCapacity) {
            this.filledCapacity = filledCapacity;
        }

        public void useCan(){
            this.filledCapacity--;
        }

        @Override
        protected int calculateEnergyConsumption() {
            return 0;
        }
    }
