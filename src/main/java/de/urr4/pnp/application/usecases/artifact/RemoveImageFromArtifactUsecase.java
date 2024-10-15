package de.urr4.pnp.application.usecases.artifact;

import de.urr4.pnp.domain.artifact.ArtifactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoveImageFromArtifactUsecase {

    private final ArtifactRepository artifactRepository;

    public RemoveImageFromArtifactUsecase(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    @Transactional
    public void removeImageFromArtifact(UUID artifactId, UUID imageId) {
        artifactRepository.removeImageFromArtifact(artifactId, imageId);
    }
}
