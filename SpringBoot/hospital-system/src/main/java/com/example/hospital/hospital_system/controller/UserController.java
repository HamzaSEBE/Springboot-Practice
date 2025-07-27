// src/main/java/com/example/hospital/hospital_system/controller/UserController.java
package com.example.hospital.hospital_system.controller;

import com.example.hospital.hospital_system.DTO.ChangePasswordRequest;
import com.example.hospital.hospital_system.service.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.slf4j.Logger; // استيراد Logger
import org.slf4j.LoggerFactory; // استيراد LoggerFactory
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.prepost.PreAuthorize;


/**
 * UserController
 *
 * هذا الكلاس مسؤول عن إدارة العمليات المتعلقة بالمستخدمين في نظام إدارة المستشفى، ويشمل:
 *
 * - عرض نموذج تغيير كلمة المرور (GET /change-password)
 * - معالجة طلب تغيير كلمة المرور بعد التحقق من تطابق البيانات (POST /change-password)
 * - عرض صفحة إدارة المستخدمين (GET /users) – مخصصة للمستخدمين الذين يمتلكون دور المسؤول فقط (ADMIN)
 *
 * الميزات الأمنية والوظيفية:
 * - يتم استخدام {@link org.springframework.security.core.Authentication} و {@link SecurityContextHolder} للتحقق من هوية المستخدم النشط
 * - يعتمد على خدمة {@link com.example.hospital.hospital_system.service.CustomUserDetailsService} لتنفيذ منطق تغيير كلمة المرور
 * - يتم استخدام سجلات {@link Logger} لتتبع الأحداث المهمة مثل محاولات التغيير الناجحة والفاشلة
 * - يدعم التحقق من صحة النموذج باستخدام Bean Validation و {@link BindingResult}
 * - يحمي صفحة إدارة المستخدمين باستخدام التعليمة {@code @PreAuthorize("hasRole('ADMIN')")}
 *
 * صفحات الواجهة المرتبطة:
 * - change-password.html: لتغيير كلمة المرور
 * - users.html: لإدارة المستخدمين (للمسؤول فقط)
 *
 * هذا الكلاس يعزز الأمان الشخصي من خلال دعم تغيير كلمة المرور ويوفر تحكمًا إداريًا في الوصول للمستخدمين.
 */





@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        logger.debug("Accessing change password form.");
        model.addAttribute("changePasswordRequest", new ChangePasswordRequest());
        if (model.containsAttribute("message")) {
            model.addAttribute("message", model.getAttribute("message"));
            model.addAttribute("messageType", model.getAttribute("messageType"));
        }
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@Valid @ModelAttribute("changePasswordRequest") ChangePasswordRequest request,
                                 BindingResult result,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {

        String username = authentication.getName();

        if (result.hasErrors()) {
            logger.warn("INPUT_VALIDATION_ERROR: Validation errors during password change for user '{}': {}", username, result.getAllErrors());
            redirectAttributes.addFlashAttribute("message", "الرجاء تصحيح الأخطاء في النموذج.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordRequest", result);
            redirectAttributes.addFlashAttribute("changePasswordRequest", request);
            return "redirect:/change-password";
        }

        // تحقق من تطابق كلمة المرور الجديدة وتأكيدها
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            logger.warn("PASSWORD_MISMATCH: New password and confirmation do not match for user: {}", username);
            redirectAttributes.addFlashAttribute("message", "كلمة المرور الجديدة وتأكيدها غير متطابقين.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changePasswordRequest", result);
            redirectAttributes.addFlashAttribute("changePasswordRequest", request);
            return "redirect:/change-password";
        }

        logger.info("Attempting to change password for user: {}", username);
        boolean success = customUserDetailsService.changeUserPassword(username, request.getCurrentPassword(), request.getNewPassword());

        if (success) {

            SecurityContextHolder.clearContext();
            logger.info("PASSWORD_CHANGE_SUCCESS: Password changed successfully for user: {}", username);
            redirectAttributes.addFlashAttribute("message", "تم تغيير كلمة المرور بنجاح! يرجى تسجيل الدخول بكلمة المرور الجديدة.");
            redirectAttributes.addFlashAttribute("messageType", "success");
            return "redirect:/login?logout";
        } else {
            logger.error("PASSWORD_CHANGE_FAILURE: Failed to change password for user: {}. (Details in CustomUserDetailsService logs)", username);
            redirectAttributes.addFlashAttribute("message", "فشل تغيير كلمة المرور. تأكد من صحة كلمة المرور الحالية وأن كلمة المرور الجديدة تفي بالمتطلبات.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/change-password";
        }
    }


    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageUsers() {
        logger.info("ADMIN_ACTION: Accessing user management page.");
        return "users";
    }
}