package de.urr4.monsters.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class IntRange {
    private Integer start;
    private Integer end;
}
