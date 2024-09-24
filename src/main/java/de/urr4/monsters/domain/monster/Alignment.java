package de.urr4.monsters.domain.monster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Alignment {
    private Attribute attribute;
    private Attitude attitude;
}
