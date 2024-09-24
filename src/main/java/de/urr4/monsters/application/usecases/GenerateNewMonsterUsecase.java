package de.urr4.monsters.application.usecases;

import de.urr4.monsters.domain.image.Image;
import de.urr4.monsters.domain.image.ImageGenerator;
import de.urr4.monsters.domain.image.ImageRepository;
import de.urr4.monsters.domain.monster.Monster;
import de.urr4.monsters.domain.monster.MonsterGenerator;
import de.urr4.monsters.domain.monster.MonsterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GenerateNewMonsterUsecase {

    private final MonsterGenerator monsterGenerator;
    private final MonsterRepository monsterRepository;
    private final ImageGenerator imageGenerator;
    private final ImageRepository imageRepository;

    public GenerateNewMonsterUsecase(MonsterGenerator monsterGenerator, MonsterRepository monsterRepository, ImageGenerator imageGenerator, ImageRepository imageRepository) {
        this.monsterGenerator = monsterGenerator;
        this.monsterRepository = monsterRepository;
        this.imageGenerator = imageGenerator;
        this.imageRepository = imageRepository;
    }


    @Transactional
    public UUID generateMonster(String prompt, boolean shouldGenerateImage) {
        Monster monster = monsterGenerator.generateMonster(prompt);
        UUID monsterId = monsterRepository.saveMonster(monster);
        if (shouldGenerateImage) {
            Image image = imageGenerator.generateImage(monster.getMonsterDescription());
            UUID imageId = imageRepository.saveImage(image);
            monsterRepository.addImageUUIDToMonster(monsterId, imageId);
        }
        return monsterId;
    }


}
