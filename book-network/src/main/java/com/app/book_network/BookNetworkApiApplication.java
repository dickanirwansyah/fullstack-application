package com.app.book_network;

import com.app.book_network.roles.Roles;
import com.app.book_network.roles.RolesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
public class BookNetworkApiApplication {

	public static void main(String[] args) {
		log.info("starting book-network application..");
		SpringApplication.run(BookNetworkApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RolesRepository rolesRepository){
		return args -> {
			if (rolesRepository.findByRoleName("USER").isEmpty()){
				rolesRepository.save(Roles.builder().name("USER").build());
			}
		};
	}

}
