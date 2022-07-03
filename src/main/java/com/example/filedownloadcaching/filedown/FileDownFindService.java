package com.example.filedownloadcaching.filedown;

import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DefaultDataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 파일 다룬 로드 서비스
 */
@Slf4j
@Service
public class FileDownFindService {
  private final ResourceLoader resourceLoader;
  private final String location;


  public FileDownFindService(ResourceLoader resourceLoader,
      @Value("${file.location}") String location) {
    this.resourceLoader = resourceLoader;
    this.location = location;
  }

  /**
   * 퍼일 검색
   *
   * @param fileName 파일 이름
   * @return 파일 리소스
   */
  @Cacheable(value = "fileDown", key = "#fileName")
  public Mono<DefaultDataBuffer> find(String fileName) {
    log.info("search file {}", location + fileName);
     Resource resource = resourceLoader.getResource(
         location + StringUtils.cleanPath(fileName));

    if (resource.exists()) {
      return Mono.fromCallable(() ->
          new DefaultDataBufferFactory().wrap(byteArray(resource)))
          .subscribeOn(Schedulers.boundedElastic());
    }

    log.info("{} is not exists", fileName);
    return Mono.empty();
  }

  private byte[] byteArray(Resource resource) throws IOException {
    try (InputStream inputStream = resource.getInputStream()){
      return IOUtils.toByteArray(inputStream);
    }
  }
}
