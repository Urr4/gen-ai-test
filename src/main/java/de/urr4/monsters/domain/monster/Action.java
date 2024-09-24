package de.urr4.monsters.domain.monster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Action {
    private String name;
    private String rechargeOn;
    private String kindOfAttack;
    private String modifierToHit;
    private String reach;
    private String numberOfTargets;
    private String description;
    private List<Damage> damages;
}
