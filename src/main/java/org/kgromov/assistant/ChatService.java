package org.kgromov.assistant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public String answer(String question) {
        var searchRequest = SearchRequest.query(question).withTopK(4);
        List<Document> documents = vectorStore.similaritySearch(searchRequest);
        String documentsContent = documents.stream().map(Document::getContent).collect(joining("\n"));

        return chatClient.prompt()
                .user(userSpec -> userSpec.params(
                        Map.of(
                                "input", question,
                                "documents", documentsContent
                        )
                ))
                .call()
                .content();
    }
}
