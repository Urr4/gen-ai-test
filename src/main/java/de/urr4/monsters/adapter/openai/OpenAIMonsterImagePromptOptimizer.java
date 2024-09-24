package de.urr4.monsters.adapter.openai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class OpenAIMonsterImagePromptOptimizer {

    private final ChatClient chatClient;

    public OpenAIMonsterImagePromptOptimizer(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String optimizePrompt(String prompt) {
        return chatClient.prompt()
                .system("You are an experienced prompt engineer who know exactly how to rewrite prompts, so that they generate the best images.")
                .user(u -> u.text("Optimize the following prompt to generate an image of a fantasy monster as if taken from a fantasy roleplaying book. Remove all reference to written text from the prompt. The prompt is: {prompt}. Return only the optimized prompt.")
                        .param("prompt", prompt))
                .call()
                .content();
    }

}
