package com.example.hospital.admin; // تأكد من اسم الحزمة الصحيح

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAdminServer // أضف هذا
public class HospitalAdminServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalAdminServerApplication.class, args);
	}
}