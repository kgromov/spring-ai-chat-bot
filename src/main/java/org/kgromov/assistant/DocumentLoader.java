package org.kgromov.assistant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;
import org.springframework.util.StopWatch;

import java.nio.file.Path;
import java.util.List;


public interface DocumentLoader {

    List<Document> loadDocument(Path filePath);

    List<Document> loadDocument(Resource resource);
}
