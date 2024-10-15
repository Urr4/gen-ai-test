package de.urr4.pnp.application.usecases.creature;

import de.urr4.pnp.domain.creature.CreatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoveImageFromCreatureUsecase {

    private final CreatureRepository creatureRepository;

    public RemoveImageFromCreatureUsecase(CreatureRepository creatureRepository) {
        this.creatureRepository = creatureRepository;
    }

    @Transactional
    public void removeImageFromCreature(UUID creatureId, UUID imageId) {
        creatureRepository.removeImageFromCreature(creatureId, imageId);
    }
}
