package de.urr4.monsters.adapter.neo4j;

import de.urr4.monsters.domain.IntRange;
import de.urr4.monsters.domain.monster.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class Neo4jMonsterRepository implements MonsterRepository {

    private final SpringDataNeo4jRepository springDataNeo4jRepository;

    public Neo4jMonsterRepository(SpringDataNeo4jRepository springDataNeo4jRepository) {
        this.springDataNeo4jRepository = springDataNeo4jRepository;
    }

    @Override
    public UUID saveMonster(Monster monster) {
        MonsterEntity monsterEntity = springDataNeo4jRepository.save(MonsterEntity.from(monster));
        return monsterEntity.getId();
    }

    @Override
    public Monster getMonsterById(UUID id) {
        return from(springDataNeo4jRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Could not find Monster %s", id))));
    }

    @Override
    public void addImageUUIDToMonster(UUID monsterId, UUID imageId) {
        MonsterEntity monsterEntity = springDataNeo4jRepository.findById(monsterId).orElseThrow(() -> new RuntimeException(String.format("Could not find Monster %s", monsterId)));
        monsterEntity.getImageStates().add(ImageStateEntity.builder()
                .imageId(imageId)
                .build()
        );
        springDataNeo4jRepository.save(monsterEntity);
    }

    @Override
    public void removeImageFromMonster(UUID monsterId, UUID imageId) {
        MonsterEntity monsterEntity = springDataNeo4jRepository.findById(monsterId).orElseThrow(() -> new RuntimeException(String.format("Could not find Monster %s", monsterId)));
        monsterEntity.getImageStates().stream()
                .filter(imageWrapper -> imageWrapper.getImageId().equals(imageId))
                .forEach(imageWrapper -> imageWrapper.setStatus(Status.REMOVED));
        springDataNeo4jRepository.save(monsterEntity);
    }

    @Override
    public List<Monster> getMonstersByFilter(MonsterFilter monsterFilter) {
        List<MonsterEntity> monsterEntitiesByFilter = springDataNeo4jRepository.getMonsterEntitiesByFilter(
                monsterFilter.getNameIncludes(),
                monsterFilter.getSize(),
                monsterFilter.getType(),
                monsterFilter.getArmorClassRange().getStart(),
                monsterFilter.getArmorClassRange().getEnd(),
                monsterFilter.getHitPointRange().getStart(),
                monsterFilter.getHitPointRange().getEnd(),
                monsterFilter.getPageNumber() * monsterFilter.getPageSize(),
                monsterFilter.getPageSize());
        return monsterEntitiesByFilter.stream().map(Neo4jMonsterRepository::from).toList();
    }

    @Override
    public int getNumberOfMatchingMonsters(MonsterFilter monsterFilter) {
        return springDataNeo4jRepository.getNumberOfMonstersByFilter(
                monsterFilter.getNameIncludes(),
                monsterFilter.getSize(),
                monsterFilter.getType(),
                monsterFilter.getArmorClassRange().getStart(),
                monsterFilter.getArmorClassRange().getEnd(),
                monsterFilter.getHitPointRange().getStart(),
                monsterFilter.getHitPointRange().getEnd());
    }

    @Override
    public List<Size> getAllSizes() {
        return springDataNeo4jRepository.getAllSizes();
    }

    @Override
    public List<String> getAllTypes() {
        return springDataNeo4jRepository.getAllTypes();
    }

    @Override
    public IntRange getCurrentArmorClassRange() {
        return new IntRange(
                springDataNeo4jRepository.findArmorClassMin(),
                springDataNeo4jRepository.findArmorClassMax()
        );
    }

    @Override
    public IntRange getCurrentHitpointRange() {
        return new IntRange(
                springDataNeo4jRepository.findHitpointMin(),
                springDataNeo4jRepository.findHitpointMax()
        );
    }

    @Override
    public void removeMonster(UUID monsterId) {
        MonsterEntity monsterEntity = springDataNeo4jRepository.findById(monsterId).orElseThrow(() -> new RuntimeException(String.format("Could not find Monster %s", monsterId)));
        monsterEntity.setStatus(Status.REMOVED);
        springDataNeo4jRepository.save(monsterEntity);
    }

    private static Monster from(MonsterEntity monsterEntity) {
        return new Monster(
                monsterEntity.getId(),
                monsterEntity.getName(),
                monsterEntity.getSize(),
                monsterEntity.getType(),
                alignmentFromString(monsterEntity.getAlignment()),
                new ArmorClass(monsterEntity.getArmorClassValue(), monsterEntity.getArmorClassType()),
                monsterEntity.getHitPoints(),
                new MovementSpeed(monsterEntity.getWalkingSpeed(), monsterEntity.getSwimmingSpeed(), monsterEntity.getFlyingSpeed()),
                new StatBlock<>(monsterEntity.getStrength(), monsterEntity.getDexterity(), monsterEntity.getConstitution(), monsterEntity.getIntelligence(), monsterEntity.getWisdom(), monsterEntity.getCharisma()),
                new StatBlock<>(monsterEntity.getSavingThrowModifierStrength(), monsterEntity.getSavingThrowModifierDexterity(), monsterEntity.getSavingThrowModifierConstitution(), monsterEntity.getSavingThrowModifierIntelligence(), monsterEntity.getSavingThrowModifierWisdom(), monsterEntity.getSavingThrowModifierCharisma()),
                monsterEntity.getProficiencies().stream().map(Neo4jMonsterRepository::proficiencyFromString).toList(),
                monsterEntity.getDamageImmunities(),
                monsterEntity.getSenses(),
                monsterEntity.getLanguages(),
                monsterEntity.getChallengeRating(),
                monsterEntity.getCharacteristics().stream().map(Neo4jMonsterRepository::from).toList(),
                monsterEntity.getActions().stream().map(Neo4jMonsterRepository::from).toList(),
                monsterEntity.getDescription(),
                monsterEntity.getImageStates().stream()
                        .filter(imageState -> Status.ACTIVE.equals(imageState.getStatus()))
                        .map(ImageStateEntity::getImageId)
                        .toList()
        );
    }

    private static Skill proficiencyFromString(String proficiency) {
        String[] split = proficiency.split("\\|");
        return new Skill(split[0], split[1]);
    }

    private static Alignment alignmentFromString(String alignment) {
        String[] split = alignment.split(" ");
        Attribute attribute = Attribute.of(split[0]);
        Attitude attitude = Attitude.of(split[1]);
        return new Alignment(attribute, attitude);
    }

    private static Characteristic from(CharacteristicEntity characteristicEntity) {
        return new Characteristic(
                characteristicEntity.getName(),
                characteristicEntity.getDescription());
    }

    private static Action from(ActionEntity actionEntity) {
        return new Action(
                actionEntity.getName(),
                actionEntity.getRechargeOn(),
                actionEntity.getKindOfAttack(),
                actionEntity.getModifier(),
                actionEntity.getReach(),
                actionEntity.getNumberOfTargets(),
                actionEntity.getDescription(),
                damagesFromString(actionEntity.getDamage()));
    }

    private static List<Damage> damagesFromString(String damageString) {
        if (damageString.isEmpty()) {
            return new ArrayList<>();
        }
        String[] split = damageString.split(" plus ");
        return Arrays.stream(split)
                .map(n -> n.replace(" damage", ""))
                .map(n -> n.split(" "))
                .map(arr -> new Damage(arr[0], arr[1]))
                .toList();
    }
}
