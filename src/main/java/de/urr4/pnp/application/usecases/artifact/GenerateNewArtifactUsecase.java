package de.urr4.pnp.application.usecases.artifact;

import de.urr4.pnp.domain.artifact.Artifact;
import de.urr4.pnp.domain.artifact.ArtifactGenerator;
import de.urr4.pnp.domain.artifact.ArtifactRepository;
import de.urr4.pnp.domain.image.Image;
import de.urr4.pnp.domain.image.ImageGenerator;
import de.urr4.pnp.domain.image.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GenerateNewArtifactUsecase {

    private final ArtifactGenerator artifactGenerator;
    private final ArtifactRepository artifactRepository;
    private final ImageGenerator imageGenerator;
    private final ImageRepository imageRepository;

    public GenerateNewArtifactUsecase(ArtifactGenerator artifactGenerator, ArtifactRepository artifactRepository, ImageGenerator imageGenerator, ImageRepository imageRepository) {
        this.artifactGenerator = artifactGenerator;
        this.artifactRepository = artifactRepository;
        this.imageGenerator = imageGenerator;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public UUID generateArtifact(String prompt, boolean shouldGenerateImage) {
        Artifact artifact = artifactGenerator.generateArtifact(prompt);
        UUID artifactId = artifactRepository.saveArtifact(artifact);
        if (shouldGenerateImage) {
            Image image = imageGenerator.generateImage(artifact.getDescription());
            UUID imageId = imageRepository.saveImage(image);
            artifactRepository.addImageUUIDToArtifact(artifactId, imageId);
        }
        return artifactId;
    }
}
