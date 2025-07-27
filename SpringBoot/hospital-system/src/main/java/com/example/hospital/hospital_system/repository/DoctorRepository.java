package com.example.hospital.hospital_system.repository;

import com.example.hospital.hospital_system.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * DoctorRepository
 *
 * واجهة مستودع JPA لإدارة كيان "طبيب" (Doctor).
 * توفر وظائف CRUD (إنشاء، قراءة، تحديث، حذف) الأساسية دون الحاجة لتعريفها يدويًا.
 * يتم التعامل مع كيان Doctor باستخدام معرف من نوع Long.
 *
 * تُستخدم هذه الواجهة من قبل Spring Data JPA لتمكين عمليات الوصول للبيانات بطريقة سهلة وفعالة.
 */

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}