package de.urr4.pnp.domain.creature;

import lombok.Getter;

@Getter
public enum Size {
    SMALL("small"),
    MEDIUM("medium"),
    LARGE("large"),
    HUGE("huge");

    private final String value;

    Size(String value) {
        this.value = value;
    }

    public static Size of(String value) {
        return switch (value) {
            case "small" -> SMALL;
            case "medium" -> MEDIUM;
            case "large" -> LARGE;
            case "huge" -> HUGE;
            default -> throw new IllegalArgumentException("Unknown attribute: " + value);
        };
    }
}
