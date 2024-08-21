package org.kgromov.assistant;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
@Slf4j
public class PdfDocumentLoader implements DocumentLoader {

    @SneakyThrows
    @Override
    public List<Document> loadDocument(Path filePath) {
        Resource pdfResource = new ByteArrayResource(Files.readAllBytes(filePath));
        return this.loadDocument(pdfResource);
    }

    @Override
    public List<Document> loadDocument(Resource pdfResource) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("%s: loadDocument".formatted(this.getClass().getSimpleName()));
        try {
            log.debug("Loading document from resource = {}", pdfResource.getFilename());
            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder()
                            .withNumberOfBottomTextLinesToDelete(0)
                            .withNumberOfTopPagesToSkipBeforeDelete(0)
                            .build())
                    .withPagesPerDocument(1)
                    .build();
            var pdfReader = new PagePdfDocumentReader(pdfResource, config);
            var textSplitter = new TokenTextSplitter();
            return textSplitter.apply(pdfReader.get());
        } finally {
            stopWatch.stop();
            var taskInfo = stopWatch.lastTaskInfo();
            log.info("Time to {} = {} ms", taskInfo.getTaskName(), taskInfo.getTimeMillis());
        }
    }
}
