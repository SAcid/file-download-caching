package com.example.filedownloadcaching.filedown;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class FileDownHandlerServerErrorTest {
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

  @MockBean
  FileDownFindService fileDownFindService;

  @BeforeEach
  void init() {
    when(fileDownFindService.find(anyString()))
        .thenThrow(new IllegalArgumentException("!!!!"));
  }

  @Test
  void error() {
    client.get().uri("/file-down/test.zip")
        .header(authKey, authValue)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
//        .expectBody(String.class)
//        .consumeWith(exchangeResult -> assertThat(exchangeResult).isEqualTo("SEVER ERROR"));

  }
}