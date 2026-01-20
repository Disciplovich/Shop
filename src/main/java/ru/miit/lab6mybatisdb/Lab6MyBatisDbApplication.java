package ru.miit.lab6mybatisdb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("ru.miit.lab6mybatisdb.mapper")
public class Lab6MyBatisDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab6MyBatisDbApplication.class, args);
    }

}
