package de.urr4.monsters.adapter.neo4j;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Node(labels = "ImageState")
public class ImageStateEntity {

    @Id
    @GeneratedValue(generatorClass = GeneratedValue.UUIDGenerator.class)
    private UUID id;

    @Builder.Default
    private Status status = Status.ACTIVE;

    private UUID imageId;
}
