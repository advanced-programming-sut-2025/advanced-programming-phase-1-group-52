package models;

import enums.items.Growable;
import enums.items.TreeType;

public class Tree implements Growable {
    TreeType type;

    public Tree(TreeType type) {
        this.type = type;
    }
}
