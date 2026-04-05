package com.ai.preload;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class InitVectorData implements ApplicationRunner {

    private final RedisVectorStore vectorStore;
    @Value("classpath:ops.txt")
    private Resource resource;

    public InitVectorData(RedisVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //加载resources下的ops.txt
        TextReader textReader = new TextReader(resource);
        textReader.setCharset(Charset.defaultCharset());

        //文件转换为向量
        List<Document> documentList = new TokenTextSplitter().transform(textReader.read());
        vectorStore.add(documentList);
    }
}
