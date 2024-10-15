package de.urr4.pnp.application.usecases.artifact;

import de.urr4.pnp.domain.artifact.ArtifactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoveArtifactUsecase {

    private final ArtifactRepository artifactRepository;

    public RemoveArtifactUsecase(ArtifactRepository artifactRepository) {
        this.artifactRepository = artifactRepository;
    }

    @Transactional
    public void removeArtifact(UUID id) {
        artifactRepository.removeArtifact(id);
    }
}
