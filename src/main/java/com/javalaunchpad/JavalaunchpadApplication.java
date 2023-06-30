package com.javalaunchpad;

import com.javalaunchpad.entity.Category;
import com.javalaunchpad.entity.Tag;
import com.javalaunchpad.repository.CategoryRepository;
import com.javalaunchpad.repository.TagRepository;
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
	@Autowired
	private CategoryRepository categoryRepository ;
	@Autowired
	private TagRepository tagRepository ;

	public static void main(String[] args) {
		SpringApplication.run(JavalaunchpadApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Role admin = new Role(null ,"ADMIN");
		Role super_admin = new Role(null ,"SUPER_ADMIN");
		Set<Role> roleSet = new HashSet<>();
		roleSet.add(admin);
		roleSet.add(super_admin);
		Role user = new Role(null ,"USER");
		Set<Role> roleSet1 = new HashSet<>();
		roleSet1.add(user);
		roleRepository.save(admin);
		roleRepository.save(super_admin);
		roleRepository.save(user);
		userRepository.save(new User(null ,"hamza" , "nassour", "hamza.nassour13@gmail.com" , passwordEncoder.encode("Hnas2018") ,roleSet ,true));
		userRepository.save(new User(null ,"hamza" , "nassour", "hamza.nassour@gmail.com" , passwordEncoder.encode("Hnas2018") ,roleSet1 ,true));
		Category java = new Category(null , "JAVA");
		Category spring = new Category(null , "Spring");
		categoryRepository.save(java);
		categoryRepository.save(spring);
		Tag authorization = new Tag(null , "Authorization");
		Tag authentication = new Tag(null , "Authentication");
		tagRepository.save(authorization);
		tagRepository.save(authentication);

	}
}