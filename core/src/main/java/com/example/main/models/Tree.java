package com.example.main.models;

import com.example.main.enums.items.TreeType;

public class Tree {
    TreeType type;

    public Tree(TreeType type) {
        this.type = type;
    }

    public TreeType getType() {
        return type;
    }
}
