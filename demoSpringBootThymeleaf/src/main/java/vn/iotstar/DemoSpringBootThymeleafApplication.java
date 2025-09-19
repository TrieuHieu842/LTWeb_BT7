package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import vn.iotstar.Config.StorageProperties;
import vn.iotstar.Service.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class DemoSpringBootThymeleafApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoSpringBootThymeleafApplication.class, args);
	}

	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args -> {
			storageService.init();
		});
	}
}
