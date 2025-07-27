// src/main/java/com/example/hospital/hospital_system/config/SecurityConfig.java

package com.example.hospital.hospital_system.config;

import com.example.hospital.hospital_system.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;


/**
 * FilteredAuditEventRepository
 *
 * هذا الكلاس يقوم بتكوين مستودع مخصص لأحداث التدقيق (Audit Events) باستخدام Spring Boot Actuator.
 * يتم استخدام مستودع في الذاكرة (InMemoryAuditEventRepository) لتخزين الأحداث، مع تطبيق فلترة
 * تمنع تخزين الأحداث الخاصة بالمستخدم المجهول "anonymousUser".
 *
 * يساهم هذا التكوين في تقليل ضوضاء السجلات وتحسين تتبع الأحداث الأمنية للمستخدمين الفعليين فقط.
 *
 * يوفر أيضًا إمكانية البحث عن الأحداث حسب المستخدم والوقت والنوع.
 */





@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/users").hasRole("ADMIN")
                        .requestMatchers("/doctors/**").hasAnyRole("ADMIN", "ASSISTANT")
                        .requestMatchers("/patients/**").hasAnyRole("ADMIN", "ASSISTANT")
                        .requestMatchers("/assistant/**").hasRole("ASSISTANT")
                        .requestMatchers("/change-password").authenticated()
                        .requestMatchers("/home").authenticated()
                        .requestMatchers("/actuator/**").hasRole("ADMIN")
                        .requestMatchers("/post-login-redirect").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/post-login-redirect", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .headers(headers -> headers
                        .xssProtection(Customizer.withDefaults())
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com https://cdnjs.cloudflare.com; img-src 'self' data:; font-src 'self' https://fonts.gstatic.com https://cdnjs.cloudflare.com;"))
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                        .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
                        .contentTypeOptions(Customizer.withDefaults())
                )
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}