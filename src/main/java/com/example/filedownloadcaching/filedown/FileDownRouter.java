package com.example.filedownloadcaching.filedown;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration(proxyBeanMethods = false)
public class FileDownRouter {

  @Bean
  public RouterFunction<ServerResponse> router(
      FileDownHandler fileDownHandler,
      @Value("${auth.key}") String authKey,
      @Value("${auth.value}") String authValue
  ) {

    return RouterFunctions
        .route(GET("/file-down/{fileName}")
            .and(accept(MediaType.APPLICATION_OCTET_STREAM)), fileDownHandler::fileDown)
        .filter(new FileDownloadFilterFunction(authKey, authValue));
  }
}
