package com.example.hospital.hospital_system.repository;

import com.example.hospital.hospital_system.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * PatientRepository
 *
 * واجهة مستودع JPA لإدارة كيان "مريض" (Patient).
 * توفر عمليات CRUD كاملة بدون الحاجة لتعريفها يدويًا.
 * تعتمد على معرف الكيان من نوع Long.
 *
 * تُستخدم من قبل Spring Data JPA لتسهيل الوصول إلى بيانات المرضى.
 */

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
}