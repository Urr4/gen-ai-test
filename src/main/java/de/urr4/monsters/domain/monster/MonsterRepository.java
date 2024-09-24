package de.urr4.monsters.domain.monster;

import de.urr4.monsters.domain.IntRange;

import java.util.List;
import java.util.UUID;

public interface MonsterRepository {

    UUID saveMonster(Monster monster);

    Monster getMonsterById(UUID uuid);

    List<Monster> getMonstersByFilter(MonsterFilter filter);
    int getNumberOfMatchingMonsters(MonsterFilter filter);

    void addImageUUIDToMonster(UUID monsterId, UUID imageId);

    void removeImageFromMonster(UUID monsterId, UUID imageId);

    List<Size> getAllSizes();

    List<String> getAllTypes();

    IntRange getCurrentArmorClassRange();

    IntRange getCurrentHitpointRange();

    void removeMonster(UUID monsterId);

}
