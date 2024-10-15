package de.urr4.pnp.domain.creature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Damage {
    private String diceRoll;
    private String damageType;
}
