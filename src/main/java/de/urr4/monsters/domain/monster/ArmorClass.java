package de.urr4.monsters.domain.monster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ArmorClass {
    private int value;
    @With
    private String kindOfArmor;
}
