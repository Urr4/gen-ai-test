package de.urr4.monsters.adapter.openai;

import de.urr4.monsters.domain.monster.Monster;
import de.urr4.monsters.domain.monster.MonsterGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class OpenAiMonsterGenerator implements MonsterGenerator {

    private final BeanOutputConverter<Monster> monsterBeanOutputConverter;
    private final ChatClient chatClient;

    public OpenAiMonsterGenerator(ChatClient chatClient) {
        this.monsterBeanOutputConverter = new BeanOutputConverter<>(Monster.class);
        this.chatClient = chatClient;
    }

    @Override
    public Monster generateMonster(String prompt) {
        Monster monster = chatClient.prompt()
                .system("You are an experienced DnD master, able to homebrew new monsters on the fly. You are very good and making interesting monsters.")
                .user(u -> u.text("Generate a new monster for the following prompt: {prompt}. Generate the output only in format {format}, if you can't simply respond 'I can't do this'")
                        .param("format", monsterBeanOutputConverter.getFormat())
                        .param("prompt", prompt))
                .call()
                .entity(Monster.class);

        //Reset uuid and imageIds as we dont want this generated
        monster.setUuid(null);
        monster.setImageIds(new ArrayList<>());
        log.info("Generated new monster {}", monster.getName());
        return sanitizeMonster(monster);
    }

    private Monster sanitizeMonster(Monster monster) {
        monster.setUuid(null);
        monster.setImageIds(new ArrayList<>());
        String armorKindSanitized = monster.getArmorClass().getKindOfArmor().replace("armor", "");
        Monster sanitizedMonster = monster
                .withArmorClass(monster.getArmorClass().withKindOfArmor(armorKindSanitized))
                .withSavingThrowModifiers(monster.getSavingThrowModifiers()
                        .withStrength(monster.getSavingThrowModifiers().getStrength().contains("none") || monster.getSavingThrowModifiers().getStrength().contains("0") ? null : monster.getSavingThrowModifiers().getStrength())
                        .withDexterity(monster.getSavingThrowModifiers().getDexterity().contains("none") || monster.getSavingThrowModifiers().getDexterity().contains("0") ? null : monster.getSavingThrowModifiers().getDexterity())
                        .withConstitution(monster.getSavingThrowModifiers().getConstitution().contains("none") || monster.getSavingThrowModifiers().getConstitution().contains("0") ? null : monster.getSavingThrowModifiers().getConstitution())
                        .withIntelligence(monster.getSavingThrowModifiers().getIntelligence().contains("none") || monster.getSavingThrowModifiers().getIntelligence().contains("0") ? null : monster.getSavingThrowModifiers().getIntelligence())
                        .withWisdom(monster.getSavingThrowModifiers().getWisdom().contains("none") || monster.getSavingThrowModifiers().getWisdom().contains("0") ? null : monster.getSavingThrowModifiers().getWisdom())
                        .withCharisma(monster.getSavingThrowModifiers().getCharisma().contains("none") || monster.getSavingThrowModifiers().getCharisma().contains("0") ? null : monster.getSavingThrowModifiers().getCharisma())
                );
        return sanitizedMonster;
    }

}
