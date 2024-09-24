package de.urr4.monsters.adapter.neo4j;

import de.urr4.monsters.domain.monster.Characteristic;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@Node(labels = "Skill")
public class CharacteristicEntity {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String description;

    public static CharacteristicEntity from(Characteristic characteristic) {
        return CharacteristicEntity.builder()
                .name(characteristic.getName())
                .description(characteristic.getDescription())
                .build();
    }
}
