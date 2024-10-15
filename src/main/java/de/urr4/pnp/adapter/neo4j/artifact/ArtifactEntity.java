package de.urr4.pnp.adapter.neo4j.artifact;

import de.urr4.pnp.adapter.neo4j.Status;
import de.urr4.pnp.adapter.neo4j.ImageStateEntity;
import de.urr4.pnp.domain.artifact.Artifact;
import de.urr4.pnp.domain.artifact.Rarity;
import de.urr4.pnp.domain.artifact.Type;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Node(labels = "Artifact")
public class ArtifactEntity {

    @Builder.Default
    private Status status = Status.ACTIVE;

    @Id
    @GeneratedValue
    private UUID id;

    private UUID userId;

    private String name;

    private boolean requiresAttunement;
    private Type type;
    private Rarity rarity;
    private String description;

    @Builder.Default
    @Relationship(type = "IMAGES")
    private List<ImageStateEntity> imageStates = new ArrayList<>();

    public static ArtifactEntity from(Artifact artifact) {
        return ArtifactEntity.builder()
                .id(artifact.getUuid())
                .userId(artifact.getUserId())
                .name(artifact.getName())
                .requiresAttunement(artifact.isRequiresAttunement())
                .type(artifact.getType())
                .description(artifact.getDescription())
                .imageStates(artifact.getImageIds().stream()
                        .map(imageId -> ImageStateEntity.builder()
                                .imageId(imageId)
                                .build())
                        .toList())
                .build();
    }
}
