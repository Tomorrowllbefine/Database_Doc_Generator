package com.kk.databasedocgenerator;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableKnife4j
public class DatabaseDocGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatabaseDocGeneratorApplication.class, args);
    }
}
