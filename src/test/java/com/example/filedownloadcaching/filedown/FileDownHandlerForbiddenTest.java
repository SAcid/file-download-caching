package com.example.filedownloadcaching.filedown;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class FileDownHandlerForbiddenTest {
  @Autowired
  WebTestClient client;

  @Autowired
  ResourceLoader resourceLoader;

  @Value("${file.location}")
  String location;

  @Test
  void forbidden() {
    client.get().uri("/file-down/test.txt").exchange()
        .expectStatus().isForbidden();
  }
}