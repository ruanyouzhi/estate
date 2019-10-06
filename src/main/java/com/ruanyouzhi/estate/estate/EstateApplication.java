package com.ruanyouzhi.estate.estate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.*.estate.estate.Mapper")
public class EstateApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstateApplication.class, args);
    }

}
