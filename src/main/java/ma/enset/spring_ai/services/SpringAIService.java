package ma.enset.spring_ai.services;


import ma.enset.spring_ai.models.GeneratedImage;
import org.springframework.ai.client.AiClient;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class SpringAIService {

    private AiClient client;


    public SpringAIService(AiClient client) {
        this.client = client;
    }

    @Value("${spring.ai.openai.apiKey}")
    private String apiKey;

    @Value("${spring.ai.openai.imageUrl}")
    private String imageUrl;

    public String getJoke(String topic){
        PromptTemplate promptTemplate=new PromptTemplate("""
                Crafting a compilation of programming jokes for my website. would you like to create a joke about {topic}?""");
        promptTemplate.add("topic",topic);
        return this.client.generate(promptTemplate.create()).getGeneration().getText();
    }


    public InputStreamResource getImage(@RequestParam(name = "topic") String topic) throws URISyntaxException {
        PromptTemplate promptTemplate = new PromptTemplate("""
                 I am really board from online memes. Can you create me a prompt about {topic}.
                 Elevate the given topic. Make it classy.
                 Make a resolution of 256x256, but ensure that it is presented in json it need to be string.
                 I desire only one creation. Give me as JSON format: prompt, n, size.
                """);
        promptTemplate.add("topic", topic);
        String imagePrompt = this.client.generate(promptTemplate.create()).getGeneration().getText();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + apiKey);
        headers.add("Content-Type", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<>(imagePrompt,headers);

        String imageUrl = restTemplate.exchange(this.imageUrl, HttpMethod.POST, httpEntity, GeneratedImage.class)
                .getBody().getData().get(0).getUrl();
        byte[] imageBytes = restTemplate.getForObject(new URI(imageUrl), byte[].class);
        assert imageBytes != null;
        return new InputStreamResource(new java.io.ByteArrayInputStream(imageBytes));
    }

}
