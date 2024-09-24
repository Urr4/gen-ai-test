package de.urr4.monsters.domain.monster;

import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Monster {
    private UUID uuid;
    private String name;
    private Size size;
    private String type;
    private Alignment alignment;
    @With
    private ArmorClass armorClass;
    private int hitPoints;
    private MovementSpeed movementSpeed;
    private StatBlock<Integer> attributes;
    @With
    private StatBlock<String> savingThrowModifiers;
    private List<Skill> skills;
    private List<String> damageImmunities;
    private List<String> senses;
    private List<String> languages;
    private int challengeRating;
    private List<Characteristic> characteristics;
    private List<Action> actions;

    private String monsterDescription;

    private List<UUID> imageIds;
}
