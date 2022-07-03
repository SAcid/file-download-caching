package com.example.filedownloadcaching.filedown;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FileDownloadFilterFunction implements
    HandlerFilterFunction<ServerResponse, ServerResponse> {
  private static final int HEADER_AUTH_KEY_INDEX = 0;
  private final String authKey;
  private final String authValue;

  @Override
  public Mono<ServerResponse> filter(ServerRequest serverRequest,
      HandlerFunction<ServerResponse> handlerFunction) {

    List<String> header = serverRequest.headers().header(authKey);
    if (checkAuthentication(header)) {
      return handlerFunction.handle(serverRequest);
    }

    return ServerResponse.status(FORBIDDEN).build();
  }

  private boolean checkAuthentication(List<String> header) {
    return !header.isEmpty() && Objects.equals(authValue, header.get(HEADER_AUTH_KEY_INDEX));
  }
}
