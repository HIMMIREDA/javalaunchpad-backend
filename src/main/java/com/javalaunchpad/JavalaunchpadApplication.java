package com.javalaunchpad;

import com.javalaunchpad.security.entity.Role;
import com.javalaunchpad.security.entity.User;
import com.javalaunchpad.security.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JavalaunchpadApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository ;

	public static void main(String[] args) {
		SpringApplication.run(JavalaunchpadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userRepository.save(new User("hamza", "nassour" ,"hamza.nassour13@gmail.com" ,"hamza123" , Role.ADMIN));
	}
}