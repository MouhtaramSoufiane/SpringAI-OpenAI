package ma.enset.spring_ai.web;

import ma.enset.spring_ai.services.SpringAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
public class SpringAIController {
    @Autowired
    private SpringAIService springAIService;

    @GetMapping("/joke")
    public String getJoke(@RequestParam String topic){
        return springAIService.getJoke(topic);
    }

    @GetMapping(value = "/image",produces = "image/jpeg")
    public ResponseEntity<InputStreamResource> getImage(@RequestParam String topic) throws URISyntaxException {

        return ResponseEntity.ok().body(springAIService.getImage(topic));
    }
}
