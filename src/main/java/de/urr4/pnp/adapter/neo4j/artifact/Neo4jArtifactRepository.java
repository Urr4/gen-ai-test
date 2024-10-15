package de.urr4.pnp.adapter.neo4j.artifact;

import de.urr4.pnp.adapter.neo4j.ImageStateEntity;
import de.urr4.pnp.adapter.neo4j.Status;
import de.urr4.pnp.domain.artifact.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class Neo4jArtifactRepository implements ArtifactRepository {

    private final SpringDataNeo4jArtifactRepository springDataNeo4jArtifactRepository;

    public Neo4jArtifactRepository(SpringDataNeo4jArtifactRepository springDataNeo4jArtifactRepository) {
        this.springDataNeo4jArtifactRepository = springDataNeo4jArtifactRepository;
    }

    @Override
    public UUID saveArtifact(Artifact artifact) {
        ArtifactEntity artifactEntity = springDataNeo4jArtifactRepository.save(ArtifactEntity.from(artifact));
        return artifactEntity.getId();
    }

    @Override
    public Artifact getArtifactById(UUID uuid) {
        return from(springDataNeo4jArtifactRepository.findById(uuid).orElseThrow(() -> new RuntimeException(String.format("Could not find artifact %s", uuid))));
    }

    @Override
    public List<Artifact> getArtifactsByFilter(ArtifactFilter filter) {
        List<ArtifactEntity> artifactEntitiesByFilter = springDataNeo4jArtifactRepository.getArtifactEntitiesByFilter(
                filter.getNameIncludes(),
                filter.getRequiresAttunement(),
                filter.getType().getName(),
                filter.getRarity().getName(),
                filter.getPageNumber() * filter.getPageSize(),
                filter.getPageSize());
        return artifactEntitiesByFilter.stream().map(Neo4jArtifactRepository::from).toList();
    }

    @Override
    public int getNumberOfMatchingArtifacts(ArtifactFilter filter) {
        return springDataNeo4jArtifactRepository.getNumberOfArtifactsByFilter(
                filter.getNameIncludes(),
                filter.getRequiresAttunement(),
                filter.getType().getName(),
                filter.getRarity().getName());
    }

    @Override
    public void addImageUUIDToArtifact(UUID artifactId, UUID imageId) {
        ArtifactEntity artifactEntity = springDataNeo4jArtifactRepository.findById(artifactId).orElseThrow(() -> new RuntimeException(String.format("Could not find artifact %s", artifactId)));
        artifactEntity.getImageStates().add(ImageStateEntity.builder()
                .imageId(imageId)
                .build()
        );
        springDataNeo4jArtifactRepository.save(artifactEntity);
    }

    @Override
    public void removeImageFromArtifact(UUID artifactId, UUID imageId) {
        ArtifactEntity artifactEntity = springDataNeo4jArtifactRepository.findById(artifactId).orElseThrow(() -> new RuntimeException(String.format("Could not find artifact %s", artifactId)));
        artifactEntity.getImageStates().stream()
                .filter(imageWrapper -> imageWrapper.getImageId().equals(imageId))
                .forEach(imageWrapper -> imageWrapper.setStatus(Status.REMOVED));
        springDataNeo4jArtifactRepository.save(artifactEntity);
    }

    @Override
    public List<Rarity> getAllRarities() {
        return springDataNeo4jArtifactRepository.getAllRarities();
    }

    @Override
    public List<Type> getAllTypes() {
        return springDataNeo4jArtifactRepository.getAllTypes();
    }

    @Override
    public void removeArtifact(UUID artifactId) {
        ArtifactEntity artifactEntity = springDataNeo4jArtifactRepository.findById(artifactId).orElseThrow(() -> new RuntimeException(String.format("Could not find artifact %s", artifactId)));
        artifactEntity.setStatus(Status.REMOVED);
        springDataNeo4jArtifactRepository.save(artifactEntity);
    }

    private static Artifact from(ArtifactEntity artifactEntity) {
        return Artifact.builder()
                .uuid(artifactEntity.getId())
                .userId(artifactEntity.getUserId())
                .name(artifactEntity.getName())
                .requiresAttunement(artifactEntity.isRequiresAttunement())
                .type(artifactEntity.getType())
                .description(artifactEntity.getDescription())
                .imageIds(artifactEntity.getImageStates().stream()
                        .filter(imageState -> Status.ACTIVE.equals(imageState.getStatus()))
                        .map(ImageStateEntity::getImageId)
                        .toList())
                .build();
    }
}
