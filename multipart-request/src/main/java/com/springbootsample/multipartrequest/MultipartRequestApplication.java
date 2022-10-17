package com.springbootsample.multipartrequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;

@SpringBootApplication
@EnableScheduling
@EnableAsync(proxyTargetClass = true)
public class MultipartRequestApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultipartRequestApplication.class, args);
    }

    @RestController
    public static class FileUploaderController {
        @Autowired
        PaymentService paymentService;

        @PostMapping("/upload-file")
        public void handle(@RequestPart("file") MultipartFile multipartFile, @RequestPart("sampleRequest") SampleRequest request) {
            System.out.println("File Size: " + multipartFile.getSize());
            System.out.println("Request: " + request);
        }

        @GetMapping("/test")
        public String handle() {
            return paymentService.payment();
        }
    }

    @Component
    public static class Converter extends AbstractJackson2HttpMessageConverter {
        /**
         * Converter for support http request with header Content-Type: multipart/form-data
         */
        public Converter(ObjectMapper objectMapper) {
            super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
        }

        @Override
        public boolean canWrite(Class<?> clazz, MediaType mediaType) {
            return false;
        }

        @Override
        public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
            return false;
        }

        @Override
        protected boolean canWrite(MediaType mediaType) {
            return false;
        }
    }

    @Data
    @ToString
    public static class SampleRequest {
        private String firstName;
        private String lastName;
    }
}
