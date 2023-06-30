package com.javalaunchpad;

import com.javalaunchpad.security.Role;
import com.javalaunchpad.security.RoleRepository;
import com.javalaunchpad.security.User;
import com.javalaunchpad.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class JavalaunchpadApplication implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository ;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PasswordEncoder passwordEncoder ;

	public static void main(String[] args) {
		SpringApplication.run(JavalaunchpadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role role = new Role(null ,"ADMIN");
		Set<Role> roleSet = new HashSet<>();
		roleSet.add(role);
		roleRepository.save(role);
		userRepository.save(new User(null , "hamza.nassour13@gmail.com" , passwordEncoder.encode("Hnas2018") ,roleSet ,true));
	}
}