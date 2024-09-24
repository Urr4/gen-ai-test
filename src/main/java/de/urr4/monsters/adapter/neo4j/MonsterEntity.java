package de.urr4.monsters.adapter.neo4j;

import de.urr4.monsters.domain.monster.Monster;
import de.urr4.monsters.domain.monster.Size;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
@Node(labels = "Monster")
public class MonsterEntity {

    @Builder.Default
    private Status status = Status.ACTIVE;

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private Size size;
    private String type;
    private String alignment;

    private Integer armorClassValue;
    private String armorClassType;
    private Integer hitPoints;
    private Integer walkingSpeed;
    private Integer swimmingSpeed;
    private Integer flyingSpeed;

    private Integer strength;
    private Integer dexterity;
    private Integer constitution;
    private Integer intelligence;
    private Integer wisdom;
    private Integer charisma;

    private String savingThrowModifierStrength;
    private String savingThrowModifierDexterity;
    private String savingThrowModifierConstitution;
    private String savingThrowModifierIntelligence;
    private String savingThrowModifierWisdom;
    private String savingThrowModifierCharisma;

    @Builder.Default
    private List<String> proficiencies = new ArrayList<>();

    @Builder.Default
    private List<String> damageImmunities = new ArrayList<>();

    @Builder.Default
    private List<String> senses = new ArrayList<>();

    @Builder.Default
    private List<String> languages = new ArrayList<>();

    private Integer challengeRating;

    @Builder.Default
    @Relationship(type = "SKILL")
    private List<CharacteristicEntity> characteristics = new ArrayList<>();

    @Builder.Default
    @Relationship(type = "ACTION")
    private List<ActionEntity> actions = new ArrayList<>();

    private String description;

    @Builder.Default
    @Relationship(type = "IMAGES")
    private List<ImageStateEntity> imageStates = new ArrayList<>();

    public static MonsterEntity from(Monster monster) {
        return MonsterEntity.builder()
                .id(monster.getUuid())
                .name(monster.getName())
                .size(monster.getSize())
                .type(monster.getType())
                .alignment(monster.getAlignment().getAttribute().getValue() + " " + monster.getAlignment().getAttitude().getValue())
                .armorClassValue(monster.getArmorClass().getValue())
                .armorClassType(monster.getArmorClass().getKindOfArmor())
                .hitPoints(monster.getHitPoints())
                .walkingSpeed(monster.getMovementSpeed().getWalk())
                .swimmingSpeed(monster.getMovementSpeed().getSwim())
                .flyingSpeed(monster.getMovementSpeed().getFly())
                .strength(monster.getAttributes().getStrength())
                .dexterity(monster.getAttributes().getDexterity())
                .constitution(monster.getAttributes().getConstitution())
                .intelligence(monster.getAttributes().getIntelligence())
                .wisdom(monster.getAttributes().getWisdom())
                .charisma(monster.getAttributes().getCharisma())
                .savingThrowModifierStrength(monster.getSavingThrowModifiers().getStrength())
                .savingThrowModifierDexterity(monster.getSavingThrowModifiers().getDexterity())
                .savingThrowModifierConstitution(monster.getSavingThrowModifiers().getConstitution())
                .savingThrowModifierIntelligence(monster.getSavingThrowModifiers().getIntelligence())
                .savingThrowModifierWisdom(monster.getSavingThrowModifiers().getWisdom())
                .savingThrowModifierCharisma(monster.getSavingThrowModifiers().getCharisma())
                .proficiencies(monster.getSkills().stream()
                        .map(proficiency -> proficiency.getName() + "|" + proficiency.getModifier())
                        .toList())
                .damageImmunities(monster.getDamageImmunities())
                .senses(monster.getSenses())
                .languages(monster.getLanguages())
                .challengeRating(monster.getChallengeRating())
                .characteristics(monster.getCharacteristics().stream().map(CharacteristicEntity::from).toList())
                .actions(monster.getActions().stream().map(ActionEntity::from).toList())
                .description(monster.getMonsterDescription())
                .imageStates(monster.getImageIds().stream()
                        .map(imageId -> ImageStateEntity.builder()
                                .imageId(imageId)
                                .build())
                        .toList())
                .build();
    }
}
