package de.urr4.pnp.application.usecases.creature;

import de.urr4.pnp.domain.creature.Creature;
import de.urr4.pnp.domain.image.Image;
import de.urr4.pnp.domain.image.ImageGenerator;
import de.urr4.pnp.domain.image.ImageRepository;
import de.urr4.pnp.domain.creature.CreatureGenerator;
import de.urr4.pnp.domain.creature.CreatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GenerateNewCreatureUsecase {

    private final CreatureGenerator creatureGenerator;
    private final CreatureRepository creatureRepository;
    private final ImageGenerator imageGenerator;
    private final ImageRepository imageRepository;

    public GenerateNewCreatureUsecase(CreatureGenerator creatureGenerator, CreatureRepository creatureRepository, ImageGenerator imageGenerator, ImageRepository imageRepository) {
        this.creatureGenerator = creatureGenerator;
        this.creatureRepository = creatureRepository;
        this.imageGenerator = imageGenerator;
        this.imageRepository = imageRepository;
    }


    @Transactional
    public UUID generateCreature(String prompt, boolean shouldGenerateImage) {
        Creature creature = creatureGenerator.generateCreature(prompt);
        UUID creatureId = creatureRepository.saveCreature(creature);
        if (shouldGenerateImage) {
            Image image = imageGenerator.generateImage(creature.getCreatureDescription());
            UUID imageId = imageRepository.saveImage(image);
            creatureRepository.addImageUUIDToCreature(creatureId, imageId);
        }
        return creatureId;
    }


}
