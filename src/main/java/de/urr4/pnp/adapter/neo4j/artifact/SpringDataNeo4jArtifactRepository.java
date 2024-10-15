package de.urr4.pnp.adapter.neo4j.artifact;

import de.urr4.pnp.domain.artifact.Rarity;
import de.urr4.pnp.domain.artifact.Type;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataNeo4jArtifactRepository extends Neo4jRepository<ArtifactEntity, UUID> {

    @Query("MATCH (a:Artifact {status:'ACTIVE'}) " +
            "WHERE (toLower(m.name) CONTAINS $nameIncludes) " +
            "AND ($requiresAttunement IS NULL OR a.requiresAttunement = $requiresAttunement) " +
            "AND (type IS NULL OR toLower(a.type) = $type) " +
            "AND (rarity IS NULL OR toLower(a.rarity) = $rarity) " +
            "RETURN a " +
            "ORDER BY a.name ASC " +
            "SKIP $skip " +
            "LIMIT $limit")
    List<ArtifactEntity> getArtifactEntitiesByFilter(
            @Param("nameIncludes") String nameIncludes,
            @Param("requiresAttunement") boolean requiresAttunement,
            @Param("type") String type,
            @Param("rarity") String rarity,
            @Param("skip") Integer skip,
            @Param("limit") Integer limit);

    @Query("MATCH (a:Artifact {status:'ACTIVE'}) " +
            "WHERE (toLower(m.name) CONTAINS $nameIncludes) " +
            "AND ($requiresAttunement IS NULL OR a.requiresAttunement = $requiresAttunement) " +
            "AND (type IS NULL OR toLower(a.type) = $type) " +
            "AND (rarity IS NULL OR toLower(a.rarity) = $rarity) " +
            "RETURN count(a)")
    Integer getNumberOfArtifactsByFilter(
            @Param("nameIncludes") String nameIncludes,
            @Param("requiresAttunement") boolean requiresAttunement,
            @Param("type") String type,
            @Param("rarity") String rarity);

    @Query("MATCH (a:Artifact {status:'ACTIVE'}) " +
            "RETURN DISTINCT a.rarity " +
            "ORDER BY a.rarity ASC")
    List<Rarity> getAllRarities();

    @Query("MATCH (m:Artifact {status:'ACTIVE'}) " +
            "RETURN DISTINCT m.type " +
            "ORDER BY m.type ASC")
    List<Type> getAllTypes();
}
