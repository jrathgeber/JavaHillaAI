package com.example.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Configuration
public class RagConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

    @Value("vectorstore.json")
    private String vectorStoreName;

    @Value("classpath:/docs/clo_stats.txt")
    private Resource faq;

    @Value("classpath:/docs/12585323.pdf")
    private Resource pdfResource;


    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingClient) throws IOException {

        var simpleVectorStore = new SimpleVectorStore(embeddingClient);
        var vectorStoreFile = getVectorStoreFile();

        if (vectorStoreFile.exists()) {
            log.info("Vector Store File Exists,");
            simpleVectorStore.load(vectorStoreFile);
        } else {

            TextSplitter textSplitter = new TokenTextSplitter();

            log.info("Loading CLO IMA PDFs into Vector Store");
            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(0)
                            .withNumberOfTopPagesToSkipBeforeDelete(0)
                            .build())
                    .withPagesPerDocument(1)
                    .build();

            var pdfReader = new PagePdfDocumentReader(pdfResource, config);
            simpleVectorStore.accept(textSplitter.apply(pdfReader.get()));

        }
        return simpleVectorStore;
    }


    private File getVectorStoreFile() {

        Path path = Paths.get("src", "main", "resources", "data");
        log.info("Path is [" + path + "]");
        String absolutePath = path.toFile().getAbsolutePath() + "\\" + vectorStoreName;
        log.info("Absolute path is [" + absolutePath + "]");
        return new File(absolutePath);

    }


}
