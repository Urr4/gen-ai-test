package de.urr4.monsters.application.usecases;

import de.urr4.monsters.domain.monster.MonsterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoveImageFromMonsterUsecase {

    private final MonsterRepository monsterRepository;

    public RemoveImageFromMonsterUsecase(MonsterRepository monsterRepository) {
        this.monsterRepository = monsterRepository;
    }

    @Transactional
    public void removeImageFromMonster(UUID monsterId, UUID imageId) {
        monsterRepository.removeImageFromMonster(monsterId, imageId);
    }
}
