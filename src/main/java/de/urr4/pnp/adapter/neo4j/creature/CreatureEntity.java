package de.urr4.pnp.adapter.neo4j.creature;

import de.urr4.pnp.adapter.neo4j.ImageStateEntity;
import de.urr4.pnp.adapter.neo4j.Status;
import de.urr4.pnp.domain.creature.Creature;
import de.urr4.pnp.domain.creature.Size;
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
@Node(labels = "Creature")
public class CreatureEntity {

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

    public static CreatureEntity from(Creature creature) {
        return CreatureEntity.builder()
                .id(creature.getUuid())
                .name(creature.getName())
                .size(creature.getSize())
                .type(creature.getType())
                .alignment(creature.getAlignment().getAttribute().getValue() + " " + creature.getAlignment().getAttitude().getValue())
                .armorClassValue(creature.getArmorClass().getValue())
                .armorClassType(creature.getArmorClass().getKindOfArmor())
                .hitPoints(creature.getHitPoints())
                .walkingSpeed(creature.getMovementSpeed().getWalk())
                .swimmingSpeed(creature.getMovementSpeed().getSwim())
                .flyingSpeed(creature.getMovementSpeed().getFly())
                .strength(creature.getAttributes().getStrength())
                .dexterity(creature.getAttributes().getDexterity())
                .constitution(creature.getAttributes().getConstitution())
                .intelligence(creature.getAttributes().getIntelligence())
                .wisdom(creature.getAttributes().getWisdom())
                .charisma(creature.getAttributes().getCharisma())
                .savingThrowModifierStrength(creature.getSavingThrowModifiers().getStrength())
                .savingThrowModifierDexterity(creature.getSavingThrowModifiers().getDexterity())
                .savingThrowModifierConstitution(creature.getSavingThrowModifiers().getConstitution())
                .savingThrowModifierIntelligence(creature.getSavingThrowModifiers().getIntelligence())
                .savingThrowModifierWisdom(creature.getSavingThrowModifiers().getWisdom())
                .savingThrowModifierCharisma(creature.getSavingThrowModifiers().getCharisma())
                .proficiencies(creature.getSkills().stream()
                        .map(proficiency -> proficiency.getName() + "|" + proficiency.getModifier())
                        .toList())
                .damageImmunities(creature.getDamageImmunities())
                .senses(creature.getSenses())
                .languages(creature.getLanguages())
                .challengeRating(creature.getChallengeRating())
                .characteristics(creature.getCharacteristics().stream().map(CharacteristicEntity::from).toList())
                .actions(creature.getActions().stream().map(ActionEntity::from).toList())
                .description(creature.getCreatureDescription())
                .imageStates(creature.getImageIds().stream()
                        .map(imageId -> ImageStateEntity.builder()
                                .imageId(imageId)
                                .build())
                        .toList())
                .build();
    }
}
