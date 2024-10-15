package de.urr4.pnp.domain.artifact;

import java.util.List;
import java.util.UUID;

public interface ArtifactRepository {

    UUID saveArtifact(Artifact artifact);

    Artifact getArtifactById(UUID uuid);

    List<Artifact> getArtifactsByFilter(ArtifactFilter filter);

    int getNumberOfMatchingArtifacts(ArtifactFilter filter);

    void addImageUUIDToArtifact(UUID artifactId, UUID imageId);

    void removeImageFromArtifact(UUID artifactId, UUID imageId);

    List<Rarity> getAllRarities();

    List<Type> getAllTypes();

    void removeArtifact(UUID artifactId);
}
