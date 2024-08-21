package org.kgromov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {

    @Value("classpath:/prompts/rag-prompt-template.st")
    private Resource systemPrompt;

    @Profile("!openai")
    @Qualifier("chatClient")
    @Bean
    ChatClient ollamaChatClient(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem(systemPrompt)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    @Profile("!openai")
    @Qualifier("vectorStore")
    @Bean
    VectorStore ollamaModelVectorStore(@Qualifier("ollamaEmbeddingModel") EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }

    @Profile("openai")
    @Qualifier("chatClient")
    @Bean
    ChatClient openaiChatClient(@Qualifier("openAiChatModel") ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem(systemPrompt)
                .defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
                .build();
    }

    @Profile("openai")
    @Qualifier("vectorStore")
    @Bean
    VectorStore openAiModelVectorStore(@Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        return new SimpleVectorStore(embeddingModel);
    }
}
