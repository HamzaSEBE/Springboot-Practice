// src/main/java/com/example/hospital/hospital_system/service/CustomUserDetailsService.java
package com.example.hospital.hospital_system.service;

import com.example.hospital.hospital_system.repository.UserRepository;
import com.example.hospital.hospital_system.entity.User;
import org.slf4j.Logger; // استيراد Logger
import org.slf4j.LoggerFactory; // استيراد LoggerFactory
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;



/**
 * CustomUserDetailsService
 *
 * هذا الكلاس مسؤول عن تحميل تفاصيل المستخدم من قاعدة البيانات لعمليات
 * التوثيق (Authentication) ضمن Spring Security، بالإضافة إلى دعم تغيير كلمة المرور.
 *
 * الوظائف الرئيسية:
 * - loadUserByUsername(String username):
 *   يقوم بالبحث عن المستخدم في قاعدة البيانات باستخدام اسم المستخدم.
 *   في حالة عدم وجود المستخدم يرمي استثناء UsernameNotFoundException.
 *   إذا تم العثور على المستخدم، يُعيد كائن UserDetails يحتوي على اسم المستخدم،
 *   كلمة المرور المشفرة، والصلاحيات (Roles) المرتبطة به.
 *
 * - changeUserPassword(String username, String currentPassword, String newPassword):
 *   يسمح للمستخدم بتغيير كلمة المرور بعد التحقق من كلمة المرور الحالية.
 *   يقوم بالتحقق من وجود المستخدم، صحة كلمة المرور الحالية، وقوة كلمة المرور الجديدة.
 *   في حال فشل أي تحقق، يتم تسجيل تحذير ويتم إرجاع false.
 *   عند النجاح، يتم ترميز كلمة المرور الجديدة وحفظها في قاعدة البيانات، مع تسجيل النجاح وإرجاع true.
 *
 * الخصائص:
 * - userRepository: للتفاعل مع بيانات المستخدمين في قاعدة البيانات.
 * - passwordEncoder: لترميز وفك ترميز كلمات المرور.
 *
 * يتم استخدام Logger لتتبع الأحداث المهمة مثل الفشل في التوثيق، محاولة تغيير كلمة المرور،
 * والنجاحات أو الإخفاقات ذات الصلة.
 *
 * يستخدم هذا الكلاس ضمن إطار Spring Security كجزء من آلية المصادقة وتفويض الوصول.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailsService(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.userRepository = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            logger.warn("AUTHENTICATION_FAILURE: User not found for username: {}", username);
            throw new UsernameNotFoundException("User not found");
        }

        User foundUser = user.get();

        logger.debug("User '{}' loaded successfully with roles: {}", foundUser.getUsername(), foundUser.getRoles().stream().map(r -> r.getName()).collect(Collectors.joining(",")));

        return new org.springframework.security.core.userdetails.User(
                foundUser.getUsername(),
                foundUser.getPassword(),
                foundUser.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public boolean changeUserPassword(String username, String currentPassword, String newPassword) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            logger.warn("PASSWORD_CHANGE_FAILURE: User '{}' not found during password change attempt.", username);
            return false;
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            logger.warn("PASSWORD_CHANGE_FAILURE: User '{}' provided incorrect current password.", username);
            return false;
        }

        // تحققات لقوة كلمة المرور الجديدة
        if (newPassword.length() < 8 ||
                !newPassword.matches(".*[0-9].*") ||
                !newPassword.matches(".*[a-z].*") ||
                !newPassword.matches(".*[A-Z].*") ||
                !newPassword.matches(".*[!@#$%^&*()].*"))
        {
            logger.warn("PASSWORD_CHANGE_FAILURE: New password for user '{}' does not meet complexity requirements.", username);

            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("PASSWORD_CHANGE_SUCCESS: Password successfully changed for user: {}", username);
        return true;
    }
}