package com.example.hospital.hospital_system.controller;

import com.example.hospital.hospital_system.DTO.PatientFormDTO;
import com.example.hospital.hospital_system.entity.Patient;
import com.example.hospital.hospital_system.repository.PatientRepository;
import com.example.hospital.hospital_system.security.EncryptionUtil;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.security.core.context.SecurityContextHolder;


/**
 * PatientController
 *
 * هذا الكلاس مسؤول عن إدارة جميع العمليات المتعلقة بالمرضى ضمن النظام، بما في ذلك:
 *
 * - عرض قائمة المرضى مع فك تشفير أرقام الهواتف (GET /patients)
 * - عرض نموذج إضافة مريض جديد (GET /patients/new)
 * - عرض نموذج تعديل مريض موجود (GET /patients/edit/{id})
 * - حفظ بيانات مريض جديد أو محدث (POST /patients/save)
 * - حذف مريض باستخدام المعرف (POST /patients/delete/{id})
 *
 * المميزات الأمنية والمراقبة المضمنة:
 * - يتم تشفير/فك تشفير أرقام الهواتف باستخدام {@link EncryptionUtil}
 * - يتم استخدام {@link MeterRegistry} لتسجيل عدادات المراقبة (عدد المشاهدات، التعديلات، الحذف...)
 * - يتم تسجيل الأحداث المهمة باستخدام {@link AuditEventRepository} لأغراض التدقيق
 * - يتم حفظ اسم المستخدم الحالي في كل عملية عبر {@link SecurityContextHolder}
 *
 * صفحات الواجهة المرتبطة:
 * - patients.html: لعرض القائمة
 * - addPatient.html: لإضافة/تعديل مريض
 *
 * هذا الكلاس يساهم في حماية البيانات، تحسين قابلية التدقيق، ودعم مقاييس الأداء بشكل آمن وفعّال.
 */



@Controller
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;
    private final EncryptionUtil encryptionUtil;
    private final MeterRegistry meterRegistry;
    private final AuditEventRepository auditEventRepository;

    public PatientController(PatientRepository patientRepository, EncryptionUtil encryptionUtil,
                             MeterRegistry meterRegistry, AuditEventRepository auditEventRepository) {
        this.patientRepository = patientRepository;
        this.encryptionUtil = encryptionUtil;
        this.meterRegistry = meterRegistry;
        this.auditEventRepository = auditEventRepository;
    }

    @GetMapping
    public String listPatients(Model model) {
        List<Patient> patients = patientRepository.findAll();
        // فك تشفير أرقام الهواتف لعرضها
        List<Patient> decryptedPatients = patients.stream().map(patient -> {
            String decryptedPhone = encryptionUtil.decrypt(patient.getPhone());
            Patient tempPatient = new Patient();
            tempPatient.setId(patient.getId());
            tempPatient.setName(patient.getName());
            tempPatient.setPhone(decryptedPhone);
            return tempPatient;
        }).collect(Collectors.toList());

        model.addAttribute("patients", decryptedPatients);
        meterRegistry.counter("patient_view_count").increment();
        return "patients";
    }

    @GetMapping("/new")
    public String showNewPatientForm(Model model) {
        model.addAttribute("patientFormDTO", new PatientFormDTO());
        if (model.containsAttribute("message")) {
            model.addAttribute("message", model.getAttribute("message"));
            model.addAttribute("messageType", model.getAttribute("messageType"));
        }
        return "addPatient";
    }

    @GetMapping("/edit/{id}")
    public String showEditPatientForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();
            String decryptedPhone = encryptionUtil.decrypt(patient.getPhone());
            PatientFormDTO patientFormDTO = new PatientFormDTO(patient.getId(), patient.getName(), decryptedPhone);
            model.addAttribute("patientFormDTO", patientFormDTO);
            if (model.containsAttribute("message")) {
                model.addAttribute("message", model.getAttribute("message"));
                model.addAttribute("messageType", model.getAttribute("messageType"));
            }
            meterRegistry.counter("patient_edit_form_access").increment();
            return "addPatient";
        } else {
            redirectAttributes.addFlashAttribute("message", "المريض غير موجود!");
            redirectAttributes.addFlashAttribute("messageType", "error");

            return "redirect:/patients";
        }
    }

    @PostMapping("/save")
    public String savePatient(@Valid @ModelAttribute("patientFormDTO") PatientFormDTO patientFormDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String eventType;

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.patientFormDTO", bindingResult);
            redirectAttributes.addFlashAttribute("patientFormDTO", patientFormDTO);
            redirectAttributes.addFlashAttribute("message", "يرجى تصحيح الأخطاء في النموذج.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            auditEventRepository.add(new AuditEvent(username, "PATIENT_SAVE_FAILED", "validationErrors", bindingResult.getAllErrors().toString()));
            return "redirect:/patients/new";
        }

        Patient patient;
        String message;
        if (patientFormDTO.getId() != null) {
            patient = patientRepository.findById(patientFormDTO.getId())
                    .orElseThrow(() -> new RuntimeException("المريض غير موجود ID: " + patientFormDTO.getId()));
            message = "تم تحديث بيانات المريض بنجاح!";
            eventType = "PATIENT_UPDATED";
            meterRegistry.counter("patient_updates_total").increment();
        } else {
            patient = new Patient();
            message = "تم إضافة المريض بنجاح!";
            eventType = "PATIENT_CREATED";
            meterRegistry.counter("patient_creations_total").increment();
        }
        patient.setName(patientFormDTO.getName());

        String encryptedPhone = encryptionUtil.encrypt(patientFormDTO.getPhone());
        patient.setPhone(encryptedPhone);

        patientRepository.save(patient);

        redirectAttributes.addFlashAttribute("message", message);
        redirectAttributes.addFlashAttribute("messageType", "success");

        //  تسجيل حدث تدقيق للعملية الناجحة**
        auditEventRepository.add(new AuditEvent(username, eventType,
                "patientId", patient.getId() != null ? patient.getId().toString() : "N/A",
                "patientName", patient.getName()));

        return "redirect:/patients";
    }

    @PostMapping("/delete/{id}")
    public String deletePatient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            patientRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "تم حذف المريض بنجاح!");
            redirectAttributes.addFlashAttribute("messageType", "success");
            meterRegistry.counter("patient_deletions_total").increment();
            //  تسجيل حدث تدقيق للحذف الناجح**
            auditEventRepository.add(new AuditEvent(username, "PATIENT_DELETED",
                    "patientId", id.toString()));
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "فشل حذف المريض: " + e.getMessage());
            redirectAttributes.addFlashAttribute("messageType", "error");
            meterRegistry.counter("patient_deletions_failed").increment();
            //  تسجيل حدث تدقيق للفشل في الحذف**
            auditEventRepository.add(new AuditEvent(username, "PATIENT_DELETION_FAILED",
                    "patientId", id.toString(), "error", e.getMessage()));
        }
        return "redirect:/patients";
    }
}