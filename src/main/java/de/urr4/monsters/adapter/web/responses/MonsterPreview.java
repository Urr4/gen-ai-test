package de.urr4.monsters.adapter.web.responses;

import de.urr4.monsters.domain.monster.Monster;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MonsterPreview {
    private String uuid;
    private String name;
    private String shortHand;
    private String description;
    private int hitPoints;
    private String armorClass;

    public static MonsterPreview from(Monster monster) {
        return new MonsterPreview(
                monster.getUuid().toString(),
                monster.getName(),
                getShortHand(monster),
                monster.getMonsterDescription(),
                monster.getHitPoints(),
                monster.getArmorClass().getValue()+" ("+monster.getArmorClass().getKindOfArmor()+")");
    }

    private static String getShortHand(Monster monster) {
        // Like "huge chaotic evil fey"
        String sb = monster.getSize().getValue() +
                " " +
                monster.getAlignment().getAttribute().getValue() +
                " " +
                monster.getAlignment().getAttitude().getValue() +
                " " +
                monster.getType();
        return sb.toLowerCase();
    }
}
