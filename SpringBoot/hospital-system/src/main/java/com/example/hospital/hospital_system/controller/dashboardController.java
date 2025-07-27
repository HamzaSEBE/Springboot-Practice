package com.example.hospital.hospital_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



/**
 * dashboardController
 *
 * هذا الكلاس مسؤول عن توجيه المستخدم إلى الصفحة الرئيسية (home page) للتطبيق.
 * عند الوصول إلى المسار "/home"، يتم عرض صفحة "home.html" الموجودة في مجلد القوالب (templates).
 *
 * يُستخدم هذا الكلاس في التحكم بعملية عرض واجهة المستخدم الرئيسية بعد تسجيل الدخول أو عند الوصول إلى الصفحة الرئيسية.
 */



@Controller
public class dashboardController {

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}
