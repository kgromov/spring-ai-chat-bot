package org.kgromov;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.ai.autoconfigure.azure.openai.AzureOpenAiAutoConfiguration;
import org.springframework.ai.autoconfigure.ollama.OllamaAutoConfiguration;
import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Push
@SpringBootApplication(exclude = {
        OpenAiAutoConfiguration.class,
        AzureOpenAiAutoConfiguration.class,
        OllamaAutoConfiguration.class
})
public class SpringAiChatBotApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiChatBotApplication.class, args);
    }

}
