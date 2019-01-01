package com.xiongyi.contentsales;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.xiongyi.contentsales")
@MapperScan(basePackages = "com.xiongyi.contentsales.dao")
public class ContentsalesApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentsalesApplication.class, args);
	}

}

