
package com.ownProject.GINS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GinsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GinsApplication.class, args);
	}

}
