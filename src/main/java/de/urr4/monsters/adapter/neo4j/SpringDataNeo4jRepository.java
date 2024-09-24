package de.urr4.monsters.adapter.neo4j;

import de.urr4.monsters.domain.monster.Size;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataNeo4jRepository extends Neo4jRepository<MonsterEntity, UUID> {

    @Query("MATCH (m:Monster {status:'ACTIVE'}) " +
            "WHERE (toLower(m.name) CONTAINS $nameIncludes) " +
            "AND ($size IS NULL OR toLower(m.size) = $size) " +
            "AND ($type IS NULL OR toLower(m.type) = $type) " +
            "AND ($armorClassFrom IS NULL OR m.armorClassValue >= $armorClassFrom) " +
            "AND ($armorClassTo IS NULL OR m.armorClassValue <= $armorClassTo) " +
            "AND ($hitPointsFrom IS NULL OR m.hitPoints >= $hitPointsFrom) " +
            "AND ($hitPointsTo IS NULL OR m.hitPoints <= $hitPointsTo) " +
            "RETURN m " +
            "ORDER BY m.name ASC " +
            "SKIP $skip " +
            "LIMIT $limit")
    List<MonsterEntity> getMonsterEntitiesByFilter(
            @Param("nameIncludes") String nameIncludes,
            @Param("size") String size,
            @Param("type") String type,
            @Param("armorClassFrom") Integer armorClassFrom,
            @Param("armorClassTo") Integer armorClassTo,
            @Param("hitPointsFrom") Integer hitPointsFrom,
            @Param("hitPointsTo") Integer hitPointsTo,
            @Param("skip") Integer skip,
            @Param("limit") Integer limit);

    @Query("MATCH (m:Monster {status:'ACTIVE'}) " +
            "WHERE (toLower(m.name) CONTAINS $nameIncludes) " +
            "AND ($size IS NULL OR toLower(m.size) = $size) " +
            "AND ($type IS NULL OR toLower(m.type) = $type) " +
            "AND ($armorClassFrom IS NULL OR m.armorClassValue >= $armorClassFrom) " +
            "AND ($armorClassTo IS NULL OR m.armorClassValue <= $armorClassTo) " +
            "AND ($hitPointsFrom IS NULL OR m.hitPoints >= $hitPointsFrom) " +
            "AND ($hitPointsTo IS NULL OR m.hitPoints <= $hitPointsTo) " +
            "RETURN count(m)")
    Integer getNumberOfMonstersByFilter(
            @Param("nameIncludes") String nameIncludes,
            @Param("size") String size,
            @Param("type") String type,
            @Param("armorClassFrom") Integer armorClassFrom,
            @Param("armorClassTo") Integer armorClassTo,
            @Param("hitPointsFrom") Integer hitPointsFrom,
            @Param("hitPointsTo") Integer hitPointsTo);

    @Query("MATCH (m:Monster {status:'ACTIVE'}) " +
            "RETURN DISTINCT m.size " +
            "ORDER BY m.size ASC")
    List<Size> getAllSizes();

    @Query("MATCH (m:Monster {status:'ACTIVE'}) " +
            "RETURN DISTINCT m.type " +
            "ORDER BY m.type ASC")
    List<String> getAllTypes();

    @Query("MATCH (m:Monster {status:'ACTIVE'}) RETURN MIN(m.armorClassValue)")
    Integer findArmorClassMin();

    @Query("MATCH (m:Monster {status:'ACTIVE'}) RETURN MAX(m.armorClassValue)")
    Integer findArmorClassMax();

    @Query("MATCH (m:Monster) RETURN MIN(m.hitPoints)")
    Integer findHitpointMin();

    @Query("MATCH (m:Monster {status:'ACTIVE'}) RETURN MAX(m.hitPoints)")
    Integer findHitpointMax();
}
