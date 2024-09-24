package de.urr4.monsters.domain.monster;

import lombok.Getter;

@Getter
public enum Attribute {
    LAWFUL("lawful"),
    NEUTRAL("neutral"),
    CHAOTIC("chaotic");

    private final String value;

    Attribute(String value) {
        this.value = value;
    }

    public static Attribute of(String value) {
        return switch (value) {
            case "lawful" -> LAWFUL;
            case "neutral" -> NEUTRAL;
            case "chaotic" -> CHAOTIC;
            default -> throw new IllegalArgumentException("Unknown attribute: " + value);
        };
    }

}
