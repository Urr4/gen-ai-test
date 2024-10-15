package de.urr4.pnp.domain.artifact;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Builder
@ToString
public class ArtifactFilter {

    private String nameIncludes;
    private Boolean requiresAttunement;
    private Type type;
    private Rarity rarity;
    private int pageNumber;
    private int pageSize;

    public void normalize() {
        if (!Objects.isNull(nameIncludes)) {
            this.nameIncludes = this.nameIncludes.toLowerCase();
        }
    }
}
