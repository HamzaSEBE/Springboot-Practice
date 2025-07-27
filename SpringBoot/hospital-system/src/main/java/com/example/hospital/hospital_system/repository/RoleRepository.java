// src/main/java/com/example/hospital/hospital_system/repository/RoleRepository.java
package com.example.hospital.hospital_system.repository;

import com.example.hospital.hospital_system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



/**
 * RoleRepository
 *
 * واجهة مستودع JPA لإدارة كيان "الدور" (Role).
 *
 * توفر عمليات CRUD كاملة على الكيان Role باستخدام Spring Data JPA.
 * كما تقدم دالة مخصصة للبحث عن الدور بناءً على اسمه.
 *
 * ملاحظة: تُعيد دالة findByName() Optional<Role> لتغليف احتمالية عدم وجود الدور المطلوب.
 */

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}