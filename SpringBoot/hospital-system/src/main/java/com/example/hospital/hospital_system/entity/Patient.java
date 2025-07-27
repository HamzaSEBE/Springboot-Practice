// src/main/java/com/example/hospital/hospital_system/entity/Patient.java
package com.example.hospital.hospital_system.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "patients") // تحديد اسم الجدول في قاعدة البيانات
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100) // حقل الاسم
    private String name;

    @Column(name = "phone", nullable = false, length = 255) // حقل رقم الهاتف المشفر (طول كافٍ)
    private String phone;

    // Constructors
    public Patient() {
    }

    public Patient(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}