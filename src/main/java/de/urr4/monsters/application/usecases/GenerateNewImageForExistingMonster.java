package de.urr4.monsters.application.usecases;

import de.urr4.monsters.domain.image.Image;
import de.urr4.monsters.domain.image.ImageGenerator;
import de.urr4.monsters.domain.image.ImageRepository;
import de.urr4.monsters.domain.monster.Monster;
import de.urr4.monsters.domain.monster.MonsterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GenerateNewImageForExistingMonster {

    private final MonsterRepository monsterRepository;
    private final ImageGenerator imageGenerator;
    private final ImageRepository imageRepository;

    public GenerateNewImageForExistingMonster(MonsterRepository monsterRepository, ImageGenerator imageGenerator, ImageRepository imageRepository) {
        this.monsterRepository = monsterRepository;
        this.imageGenerator = imageGenerator;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public UUID generateNewImageForExistingMonster(UUID id) {
        Monster monster = monsterRepository.getMonsterById(id);
        Image image = imageGenerator.generateImage(monster.getMonsterDescription());
        imageRepository.saveImage(image);
        monsterRepository.addImageUUIDToMonster(monster.getUuid(), image.getImageId());
        return image.getImageId();
    }
}
