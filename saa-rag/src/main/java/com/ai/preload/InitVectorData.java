package com.ai.preload;

import cn.hutool.crypto.SecureUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class InitVectorData implements ApplicationRunner {

    private final RedisVectorStore vectorStore;
    private final RedisTemplate<String, Object> redisTemplate;
    @Value("classpath:ops.txt")
    private Resource resource;



    @Override
    public void run(ApplicationArguments args) throws Exception {
        //加载resources下的ops.txt
        TextReader textReader = new TextReader(resource);
        textReader.setCharset(Charset.defaultCharset());

        //文件转换为向量
        List<Document> documentList = new TokenTextSplitter().transform(textReader.read());
        //每次加载都会重新加载代码，导致重复向量
        //vectorStore.add(documentList);
        String sourceMetadata = (String) textReader.getCustomMetadata().get("source");
        String textHash = SecureUtil.md5(sourceMetadata);
        String redisKey = "vector-xxx:" + textHash;
        //判断是否存入过
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(redisKey, "1");
        if (flag) {
            vectorStore.add(documentList);
        } else {
            System.out.println("已存在");
        }
    }
}
