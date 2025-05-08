package org.generator.resource;

import jakarta.servlet.http.HttpServletResponse;
import org.generator.dto.DocumentInputDataDTO;
import org.generator.services.WordService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


@RestController
@RequestMapping("/word")
public class WordResource {

//    private WordService wordService;
//
//    public WordResource(WordService wordService) {
//        this.wordService = wordService;
//    }

    @PostMapping("")
    public void getWord(HttpServletResponse response, @RequestBody DocumentInputDataDTO inputData) throws IOException {
//        ByteArrayOutputStream stream = wordService.getDocument(response, inputData);
//        try{
//            if (stream != null) {
//                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
//                response.getOutputStream().write(stream.toByteArray());
//            } else {
//                throw new IllegalArgumentException("No stream found");
//            }
//        }
//        catch (Exception e) {
//            throw new IOException(e.getMessage());
//        }
    }
}
