// src/main/java/com/example/hospital/hospital_system/DTO/PatientFormDTO.java
package com.example.hospital.hospital_system.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


/**
 * PatientFormDTO
 *
 * هذا الكلاس يمثل كائن نقل بيانات (Data Transfer Object - DTO) يُستخدم لنقل بيانات المريض
 * بين الواجهة الأمامية (Forms) وطبقة المعالجة (Controller) في النظام.
 *
 * الحقول:
 * - id: معرّف المريض (يُستخدم في عمليات التعديل، ويمكن أن يكون null عند الإضافة)
 * - name: اسم المريض (مطلوب، ويجب أن يتراوح طوله بين 2 و100 حرف)
 * - phone: رقم هاتف المريض (مطلوب، يجب أن يحتوي فقط على أرقام ويتراوح طوله بين 7 إلى 15 رقمًا)
 *
 * تم استخدام تعليقات Jakarta Bean Validation لضمان صحة المدخلات:
 * - @NotBlank: لضمان عدم ترك الحقل فارغًا
 * - @Size: لتحديد الحد الأدنى والأقصى لطول النص
 * - @Pattern: للتحقق من صيغة رقم الهاتف
 *
 * يُستخدم هذا الكائن داخل `PatientController` لعرض نموذج إضافة أو تعديل مريض ومعالجة البيانات المدخلة.
 */


public class PatientFormDTO {

    private Long id; // للتعامل مع التعديل (قد يكون null عند الإضافة)

    @NotBlank(message = "الاسم مطلوب")
    @Size(min = 2, max = 100, message = "الاسم يجب أن يكون بين 2 و 100 حرفاً")
    private String name;

    @NotBlank(message = "رقم الهاتف مطلوب")
    @Pattern(regexp = "^[0-9]{7,15}$", message = "رقم الهاتف غير صالح (يجب أن يكون من 7 إلى 15 رقمًا)")
    private String phone; // هذا الحقل هو الذي سيتم التحقق منه قبل التشفير

    // Constructors
    public PatientFormDTO() {
    }

    public PatientFormDTO(Long id, String name, String phone) {
        this.id = id;
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
}