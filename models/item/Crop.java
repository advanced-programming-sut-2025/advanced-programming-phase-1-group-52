package models.item;

import enums.items.CropType;

public class Crop extends GrowableItem {
    private CropType cropType;

    public Crop(CropType cropType, int number) {
        super(cropType, number);
        this.cropType = cropType;
    }

    public CropType getCropType() {
        return cropType;
    }

    public void setCropType(CropType cropType) {
        this.cropType = cropType;
    }

    @Override
    public int getTotalHarvestTime() {
        return cropType.getTotalHarvestTime();
    }

    @Override
    public String getGrowableName() {
        return cropType.toString();
    }
}