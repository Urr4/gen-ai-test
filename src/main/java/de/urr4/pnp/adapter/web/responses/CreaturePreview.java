package de.urr4.pnp.adapter.web.responses;

import de.urr4.pnp.domain.creature.Creature;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreaturePreview {
    private String uuid;
    private String name;
    private String shortHand;
    private String description;
    private int hitPoints;
    private String armorClass;

    public static CreaturePreview from(Creature creature) {
        return new CreaturePreview(
                creature.getUuid().toString(),
                creature.getName(),
                getShortHand(creature),
                creature.getCreatureDescription(),
                creature.getHitPoints(),
                creature.getArmorClass().getValue()+" ("+ creature.getArmorClass().getKindOfArmor()+")");
    }

    private static String getShortHand(Creature creature) {
        // Like "huge chaotic evil fey"
        String sb = creature.getSize().getValue() +
                " " +
                creature.getAlignment().getAttribute().getValue() +
                " " +
                creature.getAlignment().getAttitude().getValue() +
                " " +
                creature.getType();
        return sb.toLowerCase();
    }
}
