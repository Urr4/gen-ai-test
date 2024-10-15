package de.urr4.pnp.adapter.web.responses;

import de.urr4.pnp.domain.artifact.Artifact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtifactPreview {
    private String uuid;
    private String name;
    private boolean requiresAttunement;
    private String type;
    private String rarity;
    private String description;

    public static ArtifactPreview from(Artifact artifact) {
        return new ArtifactPreview(
                artifact.getUuid().toString(),
                artifact.getName(),
                artifact.isRequiresAttunement(),
                artifact.getType().getName(),
                artifact.getRarity().getName(),
                artifact.getDescription()
        );
    }
}
