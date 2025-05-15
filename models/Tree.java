package models;

import enums.items.Growable;
import enums.items.TreeType;

public class Tree {
    TreeType type;

    public Tree(TreeType type) {
        this.type = type;
    }

    public TreeType getType() {
        return type;
    }
}
