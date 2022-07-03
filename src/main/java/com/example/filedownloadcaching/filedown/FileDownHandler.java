package com.example.filedownloadcaching.filedown;

import static org.springframework.web.reactive.function.BodyInserters.fromDataBuffers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class FileDownHandler {

  private final FileDownFindService fileDownFindService;
  private static final Mono<ServerResponse> NOT_FOUND = ServerResponse.notFound().build();

  public Mono<ServerResponse> fileDown(@NonNull ServerRequest request) {
    final String fileName = request.pathVariable("fileName");

    log.info("request file: {}", fileName);

    return fileDownFindService.find(fileName)
          .flatMap(dataBuffer -> ServerResponse.ok()
              .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s.zip\"", fileName))
              .body(fromDataBuffers(Mono.just(dataBuffer)))
          )
          .switchIfEmpty(NOT_FOUND);
  }
}
