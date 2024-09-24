package de.urr4.monsters.application.usecases;

import de.urr4.monsters.domain.monster.MonsterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RemoveMonsterUsecase {

    private final MonsterRepository monsterRepository;

    public RemoveMonsterUsecase(MonsterRepository monsterRepository) {
        this.monsterRepository = monsterRepository;
    }

    @Transactional
    public void removeMonster(UUID id) {
        monsterRepository.removeMonster(id);
    }
}
