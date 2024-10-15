package de.urr4.pnp.adapter.neo4j.creature;

import de.urr4.pnp.adapter.neo4j.ImageStateEntity;
import de.urr4.pnp.adapter.neo4j.Status;
import de.urr4.pnp.domain.IntRange;
import de.urr4.pnp.domain.creature.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class Neo4JCreatureRepository implements CreatureRepository {

    private final SpringDataNeo4jCreatureRepository springDataNeo4JCreatureRepository;

    public Neo4JCreatureRepository(SpringDataNeo4jCreatureRepository springDataNeo4JCreatureRepository) {
        this.springDataNeo4JCreatureRepository = springDataNeo4JCreatureRepository;
    }

    @Override
    public UUID saveCreature(Creature creature) {
        CreatureEntity creatureEntity = springDataNeo4JCreatureRepository.save(CreatureEntity.from(creature));
        return creatureEntity.getId();
    }

    @Override
    public Creature getCreatureById(UUID id) {
        return from(springDataNeo4JCreatureRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Could not find Creature %s", id))));
    }

    @Override
    public void addImageUUIDToCreature(UUID creatureId, UUID imageId) {
        CreatureEntity creatureEntity = springDataNeo4JCreatureRepository.findById(creatureId).orElseThrow(() -> new RuntimeException(String.format("Could not find Creature %s", creatureId)));
        creatureEntity.getImageStates().add(ImageStateEntity.builder()
                .imageId(imageId)
                .build()
        );
        springDataNeo4JCreatureRepository.save(creatureEntity);
    }

    @Override
    public void removeImageFromCreature(UUID creatureId, UUID imageId) {
        CreatureEntity creatureEntity = springDataNeo4JCreatureRepository.findById(creatureId).orElseThrow(() -> new RuntimeException(String.format("Could not find Creature %s", creatureId)));
        creatureEntity.getImageStates().stream()
                .filter(imageWrapper -> imageWrapper.getImageId().equals(imageId))
                .forEach(imageWrapper -> imageWrapper.setStatus(Status.REMOVED));
        springDataNeo4JCreatureRepository.save(creatureEntity);
    }

    @Override
    public List<Creature> getCreatureByFilter(CreatureFilter creatureFilter) {
        List<CreatureEntity> creatureEntitiesByFilter = springDataNeo4JCreatureRepository.getCreaturesEntitiesByFilter(
                creatureFilter.getNameIncludes(),
                creatureFilter.getSize(),
                creatureFilter.getType(),
                creatureFilter.getArmorClassRange().getStart(),
                creatureFilter.getArmorClassRange().getEnd(),
                creatureFilter.getHitPointRange().getStart(),
                creatureFilter.getHitPointRange().getEnd(),
                creatureFilter.getPageNumber() * creatureFilter.getPageSize(),
                creatureFilter.getPageSize());
        return creatureEntitiesByFilter.stream().map(Neo4JCreatureRepository::from).toList();
    }

    @Override
    public int getNumberOfMatchingCreatures(CreatureFilter creatureFilter) {
        return springDataNeo4JCreatureRepository.getNumberOfCreaturesByFilter(
                creatureFilter.getNameIncludes(),
                creatureFilter.getSize(),
                creatureFilter.getType(),
                creatureFilter.getArmorClassRange().getStart(),
                creatureFilter.getArmorClassRange().getEnd(),
                creatureFilter.getHitPointRange().getStart(),
                creatureFilter.getHitPointRange().getEnd());
    }

    @Override
    public List<Size> getAllSizes() {
        return springDataNeo4JCreatureRepository.getAllSizes();
    }

    @Override
    public List<String> getAllTypes() {
        return springDataNeo4JCreatureRepository.getAllTypes();
    }

    @Override
    public IntRange getCurrentArmorClassRange() {
        return new IntRange(
                springDataNeo4JCreatureRepository.findArmorClassMin(),
                springDataNeo4JCreatureRepository.findArmorClassMax()
        );
    }

    @Override
    public IntRange getCurrentHitpointRange() {
        return new IntRange(
                springDataNeo4JCreatureRepository.findHitpointMin(),
                springDataNeo4JCreatureRepository.findHitpointMax()
        );
    }

    @Override
    public void removeCreature(UUID creatureId) {
        CreatureEntity creatureEntity = springDataNeo4JCreatureRepository.findById(creatureId).orElseThrow(() -> new RuntimeException(String.format("Could not find Creature %s", creatureId)));
        creatureEntity.setStatus(Status.REMOVED);
        springDataNeo4JCreatureRepository.save(creatureEntity);
    }

    private static Creature from(CreatureEntity creatureEntity) {
        return new Creature(
                creatureEntity.getId(),
                creatureEntity.getName(),
                creatureEntity.getSize(),
                creatureEntity.getType(),
                alignmentFromString(creatureEntity.getAlignment()),
                new ArmorClass(creatureEntity.getArmorClassValue(), creatureEntity.getArmorClassType()),
                creatureEntity.getHitPoints(),
                new MovementSpeed(creatureEntity.getWalkingSpeed(), creatureEntity.getSwimmingSpeed(), creatureEntity.getFlyingSpeed()),
                new StatBlock<>(creatureEntity.getStrength(), creatureEntity.getDexterity(), creatureEntity.getConstitution(), creatureEntity.getIntelligence(), creatureEntity.getWisdom(), creatureEntity.getCharisma()),
                new StatBlock<>(creatureEntity.getSavingThrowModifierStrength(), creatureEntity.getSavingThrowModifierDexterity(), creatureEntity.getSavingThrowModifierConstitution(), creatureEntity.getSavingThrowModifierIntelligence(), creatureEntity.getSavingThrowModifierWisdom(), creatureEntity.getSavingThrowModifierCharisma()),
                creatureEntity.getProficiencies().stream().map(Neo4JCreatureRepository::proficiencyFromString).toList(),
                creatureEntity.getDamageImmunities(),
                creatureEntity.getSenses(),
                creatureEntity.getLanguages(),
                creatureEntity.getChallengeRating(),
                creatureEntity.getCharacteristics().stream().map(Neo4JCreatureRepository::from).toList(),
                creatureEntity.getActions().stream().map(Neo4JCreatureRepository::from).toList(),
                creatureEntity.getDescription(),
                creatureEntity.getImageStates().stream()
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
