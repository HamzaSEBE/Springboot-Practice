package com.example.hospital.hospital_system.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



/**
 * DoctorFormDTO
 *
 * هذا الكلاس يمثل كائن نقل بيانات (Data Transfer Object - DTO) يُستخدم لنقل بيانات الطبيب
 * بين الواجهة الأمامية (Forms) وطبقة المعالجة (Controller) في النظام.
 *
 * الحقول:
 * - id: معرّف الطبيب (يُستخدم في عمليات التعديل، ويمكن أن يكون null عند الإضافة)
 * - name: اسم الطبيب (مطلوب، بين 2 و100 حرف)
 * - specialization: التخصص الطبي للطبيب (مطلوب، بين 2 و100 حرف)
 * - phone: رقم هاتف الطبيب (مطلوب، يجب أن يحتوي فقط على أرقام ويتراوح طوله بين 7 إلى 15 رقمًا)
 * - email: البريد الإلكتروني للطبيب (مطلوب، ويجب أن يكون بصيغة صحيحة وطوله لا يتجاوز 100 حرف)
 *
 * تم استخدام تعليقات Jakarta Bean Validation لضمان صحة المدخلات:
 * - @NotBlank: لضمان عدم ترك الحقل فارغًا
 * - @Size: لتحديد الحد الأدنى والأقصى لطول السلسلة النصية
 * - @Pattern: للتحقق من صيغة رقم الهاتف
 * - @Email: للتحقق من صيغة البريد الإلكتروني
 *
 * يُستخدم هذا الكائن داخل `DoctorController` لعرض نموذج إضافة أو تعديل طبيب ومعالجة البيانات المدخلة.
 */


public class DoctorFormDTO {

    private Long id; // For update operations (can be null for new doctors)

    @NotBlank(message = "اسم الطبيب مطلوب")
    @Size(min = 2, max = 100, message = "الاسم يجب أن يكون بين 2 و 100 حرفاً")
    private String name;

    @NotBlank(message = "التخصص مطلوب")
    @Size(min = 2, max = 100, message = "التخصص يجب أن يكون بين 2 و 100 حرفاً")
    private String specialization;

    @NotBlank(message = "رقم الهاتف مطلوب")
    @Pattern(regexp = "^[0-9]{7,15}$", message = "رقم الهاتف غير صالح (يجب أن يكون من 7 إلى 15 رقمًا)")
    private String phone; // This field will be validated before encryption

    @NotBlank(message = "البريد الإلكتروني مطلوب")
    @Email(message = "صيغة البريد الإلكتروني غير صحيحة")
    @Size(max = 100, message = "البريد الإلكتروني طويل جداً")
    private String email;

    // Constructors
    public DoctorFormDTO() {
    }

    public DoctorFormDTO(Long id, String name, String specialization, String phone, String email) {
        this.id = id;
        this.name = name;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}