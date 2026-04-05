package com.ai.controller;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class Embed2VectorController {


    private final EmbeddingModel embeddingModel;
    private final RedisVectorStore vectorStore;

    /**
     * 获取文本的向量表示
     *
     * @param msg
     * @return
     */
    @GetMapping("/text2embed")
    public EmbeddingResponse hello(@RequestParam("msg") String msg) {
        EmbeddingResponse embeddingResponse = embeddingModel.call(new EmbeddingRequest(List.of(msg),
                DashScopeEmbeddingOptions.builder().model(DashScopeModel.EmbeddingModel.EMBEDDING_V3.getValue()).build()));
        System.out.println(embeddingResponse.getResult().getOutput());
        return embeddingResponse;
    }

    @GetMapping("/text2embed/add")
    public void add() {
       List<Document> documents=List.of(new Document("i study LLM"),
               new Document("i study NLP"));

       vectorStore.add(documents);

    }

    @GetMapping("/text2embed/query")
    public List<Document> query(@RequestParam("msg") String msg) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(msg)
                //设置向量相似度搜索返回的最相似文档数量为2个。topK参数控制召回结果条数,K值越小返回结果越精确但数量越少,这里只获取最匹配的2条文档。
                .topK(2)
                .build();
        return vectorStore.doSimilaritySearch(searchRequest);
    }
}
