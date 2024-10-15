package de.urr4.pnp.adapter.neo4j.creature;

import de.urr4.pnp.domain.creature.Action;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@Node(labels = "Action")
public class ActionEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String rechargeOn;
    private String kindOfAttack;
    private String modifier;
    private String reach;
    private String numberOfTargets;
    private String description;
    private String damage;

    public static ActionEntity from(Action action) {

        String damageString = action.getDamages().stream()
                .map(damage -> damage.getDiceRoll() + " " + damage.getDamageType() + " damage")
                .collect(Collectors.joining(" plus "));

        return ActionEntity.builder()
                .name(action.getName())
                .rechargeOn(action.getRechargeOn())
                .kindOfAttack(action.getKindOfAttack())
                .modifier(action.getModifierToHit())
                .reach(action.getReach())
                .numberOfTargets(action.getNumberOfTargets())
                .description(action.getDescription())
                .damage(damageString)
                .build();
    }
}
