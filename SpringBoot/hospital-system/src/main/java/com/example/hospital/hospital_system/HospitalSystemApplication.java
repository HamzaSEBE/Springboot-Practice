// src/main/java/com/example/hospital/hospital_system/HospitalSystemApplication.java
package com.example.hospital.hospital_system;

import com.example.hospital.hospital_system.entity.Role;
import com.example.hospital.hospital_system.entity.User;
import com.example.hospital.hospital_system.repository.RoleRepository;
import com.example.hospital.hospital_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.Optional;

@SpringBootApplication
@EntityScan(basePackages = "com.example.hospital.hospital_system.entity")
@EnableJpaRepositories(basePackages = "com.example.hospital.hospital_system.repository")
@ComponentScan(basePackages = "com.example.hospital.hospital_system") // تأكد من وجود هذا ومساره صحيح
public class HospitalSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder encoder) {
		return args -> {
			// تحقق من وجود الدور "ROLE_ADMIN" أو إنشائه إذا لم يكن موجودًا
			Optional<Role> existingAdminRole = roleRepo.findByName("ROLE_ADMIN");
			Role adminRole = existingAdminRole.orElseGet(() -> {
				Role newAdminRole = new Role();
				newAdminRole.setName("ROLE_ADMIN");
				return roleRepo.save(newAdminRole); // حفظ الدور الجديد
			});

			// تحقق من وجود الدور "ROLE_ASSISTANT" أو إنشائه إذا لم يكن موجودًا
			Optional<Role> existingAssistantRole = roleRepo.findByName("ROLE_ASSISTANT");
			Role assistantRole = existingAssistantRole.orElseGet(() -> {
				Role newAssistantRole = new Role();
				newAssistantRole.setName("ROLE_ASSISTANT");
				return roleRepo.save(newAssistantRole); // حفظ الدور الجديد
			});

			// إنشاء المستخدمين فقط إذا لم يكونوا موجودين
			if (userRepo.findByUsername("admin").isEmpty()) {
				User admin = new User();
				admin.setUsername("admin");
				admin.setPassword(encoder.encode("admin123"));
				admin.setRoles(Set.of(adminRole));
				userRepo.save(admin);
			}

			if (userRepo.findByUsername("assistant").isEmpty()) {
				User assistant = new User();
				assistant.setUsername("assistant");
				assistant.setPassword(encoder.encode("assistant123"));
				assistant.setRoles(Set.of(assistantRole));
				userRepo.save(assistant);
			}
		};
	}
}