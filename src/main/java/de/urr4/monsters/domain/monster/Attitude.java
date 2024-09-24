package de.urr4.monsters.domain.monster;

import lombok.Getter;

@Getter
public enum Attitude {
    GOOD("good"),
    NEUTRAL("neutral"),
    EVIL("evil");

    private final String value;

    Attitude(String value) {
        this.value = value;
    }

    public static Attitude of(String value) {
        return switch (value) {
            case "good" -> GOOD;
            case "neutral" -> NEUTRAL;
            case "evil" -> EVIL;
            default -> throw new IllegalArgumentException("Unknown attitude " + value);
        };
    }
}
