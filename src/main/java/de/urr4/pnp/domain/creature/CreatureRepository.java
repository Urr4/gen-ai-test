package de.urr4.pnp.domain.creature;

import de.urr4.pnp.domain.IntRange;

import java.util.List;
import java.util.UUID;

public interface CreatureRepository {

    UUID saveCreature(Creature creature);

    Creature getCreatureById(UUID uuid);

    List<Creature> getCreatureByFilter(CreatureFilter filter);
    int getNumberOfMatchingCreatures(CreatureFilter filter);

    void addImageUUIDToCreature(UUID creatureId, UUID imageId);

    void removeImageFromCreature(UUID creatureId, UUID imageId);

    List<Size> getAllSizes();

    List<String> getAllTypes();

    IntRange getCurrentArmorClassRange();

    IntRange getCurrentHitpointRange();

    void removeCreature(UUID creatureId);

}
