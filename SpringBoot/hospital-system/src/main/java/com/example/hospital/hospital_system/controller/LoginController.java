// src/main/java/com/example/hospital/hospital_system/controller/LoginController.java
package com.example.hospital.hospital_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * LoginController
 *
 * كلاس مسؤول عن إدارة عمليات تسجيل الدخول والخروج للمستخدمين ضمن النظام.
 * يوفر المسارات التالية:
 *
 * - GET /login: يعرض صفحة تسجيل الدخول.
 * - GET /login?error: يعيد صفحة تسجيل الدخول عند فشل عملية المصادقة، مع تسجيل الحدث.
 * - GET /post-login-redirect: يُستخدم لإعادة توجيه المستخدم بعد تسجيل الدخول الناجح إلى الصفحة الرئيسية،
 *   مع تسجيل بيانات المستخدم والأدوار.
 * - GET /logout: يعيد توجيه المستخدم إلى صفحة تسجيل الدخول مع إشعار بالخروج وتسجيل الحدث.
 *
 * يتم تسجيل الأحداث المختلفة باستخدام SLF4J لغايات التدقيق والتحليل الأمني.
 */



@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping("/login")
    public String login() {
        logger.debug("Accessing login page.");
        return "login";
    }



    @GetMapping("/post-login-redirect") // اسم مسار جديد وفريد لمعالجة ما بعد تسجيل الدخول
    public String loginSuccess(Authentication authentication) {
        logger.info("LOGIN_SUCCESS: User '{}' logged in successfully. Roles: {}",
                authentication.getName(), authentication.getAuthorities());

        return "redirect:/home";
    }

    @GetMapping(value = "/login", params = "error")
    public String loginError() {
        logger.warn("LOGIN_FAILURE: A login attempt failed.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        logger.info("LOGOUT: User logged out.");
        return "redirect:/login?logout";
    }
}