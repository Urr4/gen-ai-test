package de.urr4.pnp.application.usecases.creature;

import de.urr4.pnp.domain.creature.Creature;
import de.urr4.pnp.domain.image.Image;
import de.urr4.pnp.domain.image.ImageGenerator;
import de.urr4.pnp.domain.image.ImageRepository;
import de.urr4.pnp.domain.creature.CreatureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GenerateNewImageForExistingCreatureUsecase {

    private final CreatureRepository creatureRepository;
    private final ImageGenerator imageGenerator;
    private final ImageRepository imageRepository;

    public GenerateNewImageForExistingCreatureUsecase(CreatureRepository creatureRepository, ImageGenerator imageGenerator, ImageRepository imageRepository) {
        this.creatureRepository = creatureRepository;
        this.imageGenerator = imageGenerator;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public UUID generateNewImageForExistingCreature(UUID id) {
        Creature creature = creatureRepository.getCreatureById(id);
        Image image = imageGenerator.generateImage(creature.getCreatureDescription());
        imageRepository.saveImage(image);
        creatureRepository.addImageUUIDToCreature(creature.getUuid(), image.getImageId());
        return image.getImageId();
    }
}
