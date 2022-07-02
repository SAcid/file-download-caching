package com.example.filedownloadcaching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FileDownloadCachingApplication {

  public static void main(String[] args) {
    SpringApplication.run(FileDownloadCachingApplication.class, args);
  }

}
