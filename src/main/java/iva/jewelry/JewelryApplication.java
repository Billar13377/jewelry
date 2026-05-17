package iva.jewelry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class JewelryApplication {

	public static void main(String[] args) {
		SpringApplication.run(JewelryApplication.class, args);
	}

}
