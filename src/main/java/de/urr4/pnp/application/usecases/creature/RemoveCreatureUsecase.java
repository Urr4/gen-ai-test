package de.urr4.pnp.application.usecases.creature;

import de.urr4.pnp.domain.creature.CreatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoveCreatureUsecase {

    private final CreatureRepository creatureRepository;

    public RemoveCreatureUsecase(CreatureRepository creatureRepository) {
        this.creatureRepository = creatureRepository;
    }

    @Transactional
    public void removeCreature(UUID id) {
        creatureRepository.removeCreature(id);
    }
}
