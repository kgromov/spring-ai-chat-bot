package org.kgromov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.autoconfigure.azure.openai.AzureOpenAiAutoConfiguration;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.azure.openai.AzureOpenAiEmbeddingModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

@Profile({"azure", "default"})
@Configuration
@Import(AzureOpenAiAutoConfiguration.class)
@RequiredArgsConstructor
public class AzureAiConfig {

    @Value("classpath:/prompts/rag-prompt-template.st")
    private Resource systemPrompt;


    @Qualifier("chatClient")
    @Bean
    ChatClient azureChatClient(AzureOpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultUser(systemPrompt)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    @Qualifier("vectorStore")
    @Bean
    VectorStore azureModelVectorStore(AzureOpenAiEmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }
}
