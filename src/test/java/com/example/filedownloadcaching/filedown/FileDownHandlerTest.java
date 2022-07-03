package com.example.filedownloadcaching.filedown;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class FileDownHandlerTest {
  @Autowired
  WebTestClient client;

  @Autowired
  ResourceLoader resourceLoader;

  @Value("${file.location}")
  String location;

  @Value("${auth.key}")
  String authKey;
  @Value("${auth.value}")
  String authValue;

  @Test
  void notFound() {
    client.get().uri("/file-down/not_found.zip").header(authKey, authValue).exchange()
        .expectStatus().isNotFound();
  }

  @Test
  void ok() throws IOException {
    final String fileName = "test.txt";
    Resource resource = resourceLoader.getResource(location + fileName);
    try (InputStream inputStream = resource.getInputStream()){
      byte[] bytes = inputStream.readAllBytes();
      client.get().uri("/file-down/" + fileName)
          .header(authKey, authValue)
          .exchange()
          .expectStatus().isOk()
          .expectBody(byte[].class)
          .consumeWith(exchangeResult -> {
            assertThat(exchangeResult.getResponseBody()).isEqualTo(bytes);
          });
    }
  }
}