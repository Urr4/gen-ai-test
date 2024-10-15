package de.urr4.pnp.domain.artifact;

import lombok.Getter;

@Getter
public enum Type {
    ARMOR("armor"),
    RING("ring"),
    POTION("potion"),
    SCROLL("scoll"),
    WAND("wand"),
    SCEPTER("scepter"),
    WEAPON("weapon"),
    ROD("rod"),
    WOUNDROUS_ITEM("wondrous item");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    public static Type of(String name) {
        return switch (name) {
            case "armor" -> ARMOR;
            case "ring" -> RING;
            case "potion" -> POTION;
            case "scroll" -> SCROLL;
            case "wand" -> WAND;
            case "scepter" -> SCEPTER;
            case "weapon" -> WEAPON;
            case "rod" -> ROD;
            case "wondrous item" -> WOUNDROUS_ITEM;
            default -> throw new IllegalArgumentException("Unknown type: " + name);
        };
    }


}
