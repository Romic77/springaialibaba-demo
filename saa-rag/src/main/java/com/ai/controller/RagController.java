package com.ai.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class RagController {
    private final VectorStore vectorStore;
    private final ChatClient chatClient;

    @GetMapping("/rag4aiops")
    public Flux<String> rag4aiops(@RequestParam("msg") String msg) {
        String systemInfo = "你是一个运维工程师，按照给出的编码给出对应的故障信息，否则回复找不到信息";
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder().
                documentRetriever(VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).build()).build();


        return chatClient.prompt().system(systemInfo).user(msg).advisors(retrievalAugmentationAdvisor).stream().content();
    }
}
