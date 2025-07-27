package com.example.hospital.hospital_system.config;

import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.boot.actuate.audit.InMemoryAuditEventRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * هذا الكلاس يقوم بتكوين مستودع مخصص لتخزين أحداث التدقيق (Audit Events) داخل تطبيق Spring.
 * يتم تخزين الأحداث في الذاكرة باستخدام InMemoryAuditEventRepository مع إضافة فلتر يتجاهل الأحداث
 * المرتبطة بالمستخدم المجهول "anonymousUser".
 * كما يتيح إمكانية البحث عن الأحداث المخزنة بناءً على المستخدم أو الوقت أو نوع الحدث.
 */
@Configuration
public class FilteredAuditEventRepository {

    private static final Logger logger = LoggerFactory.getLogger(FilteredAuditEventRepository.class);

    @Bean
    public AuditEventRepository auditEventRepository() {
        final InMemoryAuditEventRepository delegate = new InMemoryAuditEventRepository();

        return new AuditEventRepository() {
            @Override
            public void add(AuditEvent event) {
                logger.debug("Attempting to add audit event: {}", event);
                if (event != null && !"anonymousUser".equals(event.getPrincipal())) {
                    delegate.add(event);
                    logger.debug("Audit event added: {}", event);
                } else {
                    logger.debug("Audit event skipped for anonymousUser or null event: {}", event);
                }
            }

            @Override
            public List<AuditEvent> find(String principal, Instant after, String type) {
                return delegate.find(principal, after, type);
            }
        };
    }
}
