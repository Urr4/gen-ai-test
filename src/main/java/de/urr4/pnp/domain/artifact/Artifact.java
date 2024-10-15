package de.urr4.pnp.domain.artifact;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class Artifact {
    private UUID uuid;
    private UUID userId;
    private String name;
    private boolean requiresAttunement;
    private Type type;
    private Rarity rarity;
    private String description;
    private List<UUID> imageIds;
}
