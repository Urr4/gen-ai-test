package de.urr4.pnp.domain.artifact;

import lombok.Getter;

@Getter
public enum Rarity {
    COMMON("common"),
    UNCOMMON("uncommon"),
    RARE("rare"),
    VERY_RARE("very rare"),
    LEGENDARY("legendary");

    private final String name;

    Rarity(String name) {
        this.name = name;
    }

    public static Rarity of(String name) {
        return switch (name) {
            case "common" -> COMMON;
            case "uncommon" -> UNCOMMON;
            case "rare" -> RARE;
            case "very rare" -> VERY_RARE;
            case "legendary" -> LEGENDARY;
            default -> throw new IllegalArgumentException("Unknown rarity: " + name);
        };
    }
}
