package com.example.main.models.item;

import com.example.main.enums.items.MaterialType;

public class Material extends Item{
    private MaterialType materialType;
    private String materialName;

    public Material(MaterialType material,int number) {
        super(material, number);
        this.materialType = material;
    }

    @Override
    protected int calculateEnergyConsumption() {
        return 0;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}
