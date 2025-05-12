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

/**
 * REST controller that handles requests for generating Word documents.
 * Provides an endpoint to receive document input data and return a generated
 * .docx file as a response.
 */
@RestController
@RequestMapping("/word")
public class WordResource {

    private final WordService wordService;


    public WordResource(WordService wordService) {
        this.wordService = wordService;
    }

    /**
     * Receives document input data and generates a Word (.docx) file as a response.
     * This endpoint expects a POST request containing the document input data.
     * If the document is successfully generated, it sets the appropriate response headers and streams
     * the file back to the client. Throws an exception if the stream is null or an error occurs during processing.
     *
     * @param response the HTTP servlet response used to send the file
     * @param inputData the data required to generate the Word document
     * @throws IOException if an error occurs while writing the file to the response
     */
    @PostMapping("")
    public void getWord(HttpServletResponse response, @RequestBody DocumentInputDataDTO inputData) throws IOException {
        ByteArrayOutputStream stream = wordService.generateWorkSheet(response, inputData);
        try{
            if (stream != null) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                response.getOutputStream().write(stream.toByteArray());
            } else {
                throw new IllegalArgumentException("No stream found");
            }
        }
        catch (Exception e) {
            throw new IOException(e.getMessage());
        }
    }
}
