package com.example.hospital.hospital_system.controller;

import com.example.hospital.hospital_system.DTO.DoctorFormDTO;
import com.example.hospital.hospital_system.entity.Doctor;
import com.example.hospital.hospital_system.repository.DoctorRepository;
import com.example.hospital.hospital_system.security.EncryptionUtil;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.security.core.context.SecurityContextHolder;




/**
 * DoctorController
 *
 * كلاس مسؤول عن إدارة عمليات CRUD المتعلقة بالأطباء داخل النظام.
 * يوفر واجهات عرض وتعديل وحذف وإضافة الأطباء، ويقوم بما يلي:
 *
 * - عرض قائمة الأطباء مع فك تشفير البيانات الحساسة مثل الهاتف والبريد الإلكتروني.
 * - تقديم واجهات لإضافة وتعديل بيانات الأطباء (مقيدة لدور ADMIN فقط).
 * - استخدام التحقق من الصحة (Validation) عند حفظ البيانات.
 * - حماية الوصول باستخدام التعليقات التوضيحية `@PreAuthorize` للتحكم في الصلاحيات.
 * - تسجيل الأحداث الأمنية (Audit Events) لكل عملية ناجحة أو فاشلة باستخدام `AuditEventRepository`.
 * - قياس أداء العمليات وعدد مرات التنفيذ باستخدام Micrometer عبر `MeterRegistry`.
 * - تشفير البيانات الحساسة عند الحفظ وفك تشفيرها عند العرض باستخدام `EncryptionUtil`.
 *
 * يستخدم القالبين الرئيسيين: doctors.html و addDoctor.html.
 */



@Controller
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorRepository doctorRepository;
    private final EncryptionUtil encryptionUtil;
    private final MeterRegistry meterRegistry;
    private final AuditEventRepository auditEventRepository;

    public DoctorController(DoctorRepository doctorRepository, EncryptionUtil encryptionUtil,
                            MeterRegistry meterRegistry, AuditEventRepository auditEventRepository) {
        this.doctorRepository = doctorRepository;
        this.encryptionUtil = encryptionUtil;
        this.meterRegistry = meterRegistry;
        this.auditEventRepository = auditEventRepository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'ASSISTANT')")
    public String listDoctors(Model model) {
        List<Doctor> doctors = doctorRepository.findAll();
        List<Doctor> decryptedDoctors = doctors.stream().map(doctor -> {
            String decryptedPhone = encryptionUtil.decrypt(doctor.getPhone());
            String decryptedEmail = encryptionUtil.decrypt(doctor.getEmail());
            doctor.setPhone(decryptedPhone);
            doctor.setEmail(decryptedEmail);
            return doctor;
        }).collect(Collectors.toList());

        model.addAttribute("doctors", decryptedDoctors);
        meterRegistry.counter("doctor_view_count").increment();
        return "doctors";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddDoctorForm(Model model) {
        model.addAttribute("doctorFormDTO", new DoctorFormDTO());
        model.addAttribute("pageTitle", "إضافة طبيب جديد");
        return "addDoctor";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditDoctorForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isPresent()) {
            Doctor doctor = doctorOptional.get();
            DoctorFormDTO doctorFormDTO = new DoctorFormDTO(
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    encryptionUtil.decrypt(doctor.getPhone()),
                    encryptionUtil.decrypt(doctor.getEmail())
            );
            model.addAttribute("doctorFormDTO", doctorFormDTO);
            model.addAttribute("pageTitle", "تعديل بيانات الطبيب");
            meterRegistry.counter("doctor_edit_form_access").increment();
            return "addDoctor";
        } else {
            redirectAttributes.addFlashAttribute("message", "الطبيب غير موجود!");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/doctors";
        }
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveDoctor(@Valid @ModelAttribute("doctorFormDTO") DoctorFormDTO doctorFormDTO,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String eventType;

        if (result.hasErrors()) {
            auditEventRepository.add(new AuditEvent(username, "DOCTOR_SAVE_FAILED", "validationErrors", result.getAllErrors().toString()));
            return "addDoctor";
        }

        Doctor doctor;
        String message;
        if (doctorFormDTO.getId() != null) {
            doctor = doctorRepository.findById(doctorFormDTO.getId())
                    .orElseThrow(() -> new RuntimeException("الطبيب غير موجود ID: " + doctorFormDTO.getId()));
            message = "تم تحديث بيانات الطبيب بنجاح!";
            eventType = "DOCTOR_UPDATED";
            meterRegistry.counter("doctor_updates_total").increment();
        } else {
            doctor = new Doctor();
            message = "تم إضافة الطبيب بنجاح!";
            eventType = "DOCTOR_CREATED";
            meterRegistry.counter("doctor_creations_total").increment();
        }

        doctor.setName(doctorFormDTO.getName());
        doctor.setSpecialization(doctorFormDTO.getSpecialization());

        String encryptedPhone = encryptionUtil.encrypt(doctorFormDTO.getPhone());
        doctor.setPhone(encryptedPhone);

        String encryptedEmail = encryptionUtil.encrypt(doctorFormDTO.getEmail());
        doctor.setEmail(encryptedEmail);

        doctorRepository.save(doctor);

        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("messageType", "success");

        //  تسجيل حدث تدقيق للعملية الناجحة**
        auditEventRepository.add(new AuditEvent(username, eventType,
                "doctorId", doctor.getId() != null ? doctor.getId().toString() : "N/A",
                "doctorName", doctor.getName()));

        return "redirect:/doctors";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName(); // الحصول على المستخدم الحالي
        try {
            doctorRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "تم حذف الطبيب بنجاح!");
            redirectAttributes.addFlashAttribute("messageType", "success");
            meterRegistry.counter("doctor_deletions_total").increment();
            // **جديد: تسجيل حدث تدقيق للحذف الناجح**
            auditEventRepository.add(new AuditEvent(username, "DOCTOR_DELETED",
                    "doctorId", id.toString()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "فشل حذف الطبيب: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            meterRegistry.counter("doctor_deletions_failed").increment();
            //  تسجيل حدث تدقيق للفشل في الحذف**
            auditEventRepository.add(new AuditEvent(username, "DOCTOR_DELETION_FAILED",
                    "doctorId", id.toString(), "error", e.getMessage()));
        }
        return "redirect:/doctors";
    }
}