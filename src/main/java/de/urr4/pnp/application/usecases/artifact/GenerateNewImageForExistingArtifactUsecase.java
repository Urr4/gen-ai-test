package de.urr4.pnp.application.usecases.artifact;

import de.urr4.pnp.domain.artifact.Artifact;
import de.urr4.pnp.domain.artifact.ArtifactRepository;
import de.urr4.pnp.domain.image.Image;
import de.urr4.pnp.domain.image.ImageGenerator;
import de.urr4.pnp.domain.image.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GenerateNewImageForExistingArtifactUsecase {

    private final ArtifactRepository artifactRepository;
    private final ImageGenerator imageGenerator;
    private final ImageRepository imageRepository;

    public GenerateNewImageForExistingArtifactUsecase(ArtifactRepository artifactRepository, ImageGenerator imageGenerator, ImageRepository imageRepository) {
        this.artifactRepository = artifactRepository;
        this.imageGenerator = imageGenerator;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public UUID generateNewImageForExistingArtifact(UUID id) {
        Artifact artifact = artifactRepository.getArtifactById(id);
        Image image = imageGenerator.generateImage(artifact.getDescription());
        imageRepository.saveImage(image);
        artifactRepository.addImageUUIDToArtifact(artifact.getUuid(), image.getImageId());
        return image.getImageId();
    }
}
