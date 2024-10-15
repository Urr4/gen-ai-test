package de.urr4.pnp.adapter.openai;

import de.urr4.pnp.domain.creature.Creature;
import de.urr4.pnp.domain.creature.CreatureGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
public class OpenAiCreatureGenerator implements CreatureGenerator {

    private final BeanOutputConverter<Creature> creatureBeanOutputConverter;
    private final ChatClient chatClient;

    public OpenAiCreatureGenerator(ChatClient chatClient) {
        this.creatureBeanOutputConverter = new BeanOutputConverter<>(Creature.class);
        this.chatClient = chatClient;
    }

    @Override
    public Creature generateCreature(String prompt) {
        Creature creature = chatClient.prompt()
                .system("You are an experienced DnD master, able to homebrew new creatures on the fly. You are very good at making interesting creatures.")
                .user(u -> u.text("Generate a new creature for the following prompt: {prompt}. Generate the output only in format {format}, if you can't simply respond 'I can't do this'")
                        .param("format", creatureBeanOutputConverter.getFormat())
                        .param("prompt", prompt))
                .call()
                .entity(Creature.class);

        //Reset uuid and imageIds as we dont want this generated
        creature.setUuid(null);
        creature.setImageIds(new ArrayList<>());
        log.info("Generated new creature {}", creature.getName());
        return sanitizeCreature(creature);
    }

    private Creature sanitizeCreature(Creature creature) {
        creature.setUuid(null);
        creature.setImageIds(new ArrayList<>());
        String armorKindSanitized = creature.getArmorClass().getKindOfArmor().replace("armor", "");
        Creature sanitizedCreature = creature
                .withArmorClass(creature.getArmorClass().withKindOfArmor(armorKindSanitized))
                .withSavingThrowModifiers(creature.getSavingThrowModifiers()
                        .withStrength(creature.getSavingThrowModifiers().getStrength().contains("none") || creature.getSavingThrowModifiers().getStrength().contains("0") ? null : creature.getSavingThrowModifiers().getStrength())
                        .withDexterity(creature.getSavingThrowModifiers().getDexterity().contains("none") || creature.getSavingThrowModifiers().getDexterity().contains("0") ? null : creature.getSavingThrowModifiers().getDexterity())
                        .withConstitution(creature.getSavingThrowModifiers().getConstitution().contains("none") || creature.getSavingThrowModifiers().getConstitution().contains("0") ? null : creature.getSavingThrowModifiers().getConstitution())
                        .withIntelligence(creature.getSavingThrowModifiers().getIntelligence().contains("none") || creature.getSavingThrowModifiers().getIntelligence().contains("0") ? null : creature.getSavingThrowModifiers().getIntelligence())
                        .withWisdom(creature.getSavingThrowModifiers().getWisdom().contains("none") || creature.getSavingThrowModifiers().getWisdom().contains("0") ? null : creature.getSavingThrowModifiers().getWisdom())
                        .withCharisma(creature.getSavingThrowModifiers().getCharisma().contains("none") || creature.getSavingThrowModifiers().getCharisma().contains("0") ? null : creature.getSavingThrowModifiers().getCharisma())
                );
        return sanitizedCreature;
    }

}
