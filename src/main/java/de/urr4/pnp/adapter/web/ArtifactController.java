package de.urr4.pnp.adapter.web;

import de.urr4.pnp.adapter.web.requests.GenerateArtifactRequest;
import de.urr4.pnp.adapter.web.responses.ArtifactFilterParamResponse;
import de.urr4.pnp.adapter.web.responses.ArtifactPreview;
import de.urr4.pnp.adapter.web.responses.ArtifactPreviewPageResponse;
import de.urr4.pnp.application.usecases.artifact.GenerateNewArtifactUsecase;
import de.urr4.pnp.application.usecases.artifact.GenerateNewImageForExistingArtifactUsecase;
import de.urr4.pnp.application.usecases.artifact.RemoveArtifactUsecase;
import de.urr4.pnp.application.usecases.artifact.RemoveImageFromArtifactUsecase;
import de.urr4.pnp.domain.artifact.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/artifacts")
@CrossOrigin(origins = "http://localhost:4200")
public class ArtifactController {

    private final ArtifactRepository artifactRepository;

    private final GenerateNewArtifactUsecase generateNewArtifactUsecase;
    private final GenerateNewImageForExistingArtifactUsecase generateNewImageForExistingArtifactUsecase;
    private final RemoveArtifactUsecase removeArtifactUsecase;
    private final RemoveImageFromArtifactUsecase removeImageFromArtifactUsecase;

    public ArtifactController(ArtifactRepository artifactRepository, GenerateNewArtifactUsecase generateNewArtifactUsecase, GenerateNewImageForExistingArtifactUsecase generateNewImageForExistingArtifactUsecase, RemoveArtifactUsecase removeArtifactUsecase, RemoveImageFromArtifactUsecase removeImageFromArtifactUsecase) {
        this.artifactRepository = artifactRepository;
        this.generateNewArtifactUsecase = generateNewArtifactUsecase;
        this.generateNewImageForExistingArtifactUsecase = generateNewImageForExistingArtifactUsecase;
        this.removeArtifactUsecase = removeArtifactUsecase;
        this.removeImageFromArtifactUsecase = removeImageFromArtifactUsecase;
    }

    @GetMapping(path = "/filterParams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArtifactFilterParamResponse getFilterParams() {
        List<String> rarities = artifactRepository.getAllRarities().stream().map(Rarity::getName).toList();
        List<String> types = artifactRepository.getAllTypes().stream().map(Type::getName).toList();
        return new ArtifactFilterParamResponse(
                rarities,
                types);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ArtifactPreviewPageResponse getArtifactsByFilter(
            @RequestParam(value = "nameIncludes", required = false, defaultValue = "") String nameIncludes,
            @RequestParam(value = "requiresAttunement", required = false) Boolean requiresAttunement,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "rarity", required = false) String rarity,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "50") int pageSize) {
        ArtifactFilter artifactFilter = ArtifactFilter.builder()
                .nameIncludes(nameIncludes)
                .requiresAttunement(requiresAttunement)
                .type(Type.of(type))
                .rarity(Rarity.of(rarity))
                .pageNumber(page)
                .pageSize(pageSize)
                .build();
        artifactFilter.normalize();
        log.info("Filtering for {}", artifactFilter);
        List<ArtifactPreview> artifactPreviews = artifactRepository.getArtifactsByFilter(artifactFilter).stream().map(ArtifactPreview::from).toList();
        int numberOfMatchingArtifacts = artifactRepository.getNumberOfMatchingArtifacts(artifactFilter);
        return new ArtifactPreviewPageResponse(artifactPreviews, numberOfMatchingArtifacts);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Artifact getArtifactById(@PathVariable("id") String id) {
        return artifactRepository.getArtifactById(UUID.fromString(id));
    }

    @Transactional
    @PutMapping(path = "/{id}/image")
    public UUID generateNewImageForArtifact(@PathVariable("id") String id) {
        log.info("Generating new image for artifact {}", id);
        return generateNewImageForExistingArtifactUsecase.generateNewImageForExistingArtifact(UUID.fromString(id));
    }

    @PostMapping(path = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ArtifactPreview generateNewArtifact(@RequestBody GenerateArtifactRequest generateArtifactRequest) {
        log.info("Got request {}", generateArtifactRequest);
        UUID artifactId = generateNewArtifactUsecase.generateArtifact(generateArtifactRequest.getPrompt(), generateArtifactRequest.isGenerateImage());
        Artifact artifact = artifactRepository.getArtifactById(artifactId);
        log.info("Generated artifact {}", artifact.getName());
        return ArtifactPreview.from(artifact);
    }

    @DeleteMapping(path = "/{artifactId}")
    public void removeArtifact(@PathVariable("artifactId") String artifactId) {
        removeArtifactUsecase.removeArtifact(UUID.fromString(artifactId));
    }

    @Transactional
    @DeleteMapping(path = "/{artifactId}/image/{imageId}")
    public void removeImageFromArtifact(
            @PathVariable("artifactId") String artifactId,
            @PathVariable("imageId") String imageId
    ) {
        log.info("Removing image {} from artifact {}", imageId, artifactId);
        removeImageFromArtifactUsecase.removeImageFromArtifact(UUID.fromString(artifactId), UUID.fromString(imageId));
    }
}
