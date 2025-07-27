package com.example.hospital.hospital_system.repository;

import com.example.hospital.hospital_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


/**
 * UserRepository
 *
 * واجهة مستودع JPA لإدارة كيان المستخدم (User).
 * يوفر عمليات CRUD أساسية، بالإضافة إلى دالة مخصصة للبحث عن المستخدم بواسطة اسم المستخدم.
 *
 * @see User
 */

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
