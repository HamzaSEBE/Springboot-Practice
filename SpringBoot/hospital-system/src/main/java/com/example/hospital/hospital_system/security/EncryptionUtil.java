package com.example.hospital.hospital_system.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * EncryptionUtil
 *
 * هذا الكلاس مسؤول عن تشفير وفك تشفير النصوص باستخدام خوارزمية AES.
 * يُستخدم لحماية البيانات الحساسة مثل أرقام الهواتف أو كلمات المرور قبل حفظها في قاعدة البيانات.
 *
 * الخصائص:
 * - secretKey: مفتاح التشفير (يجب أن يكون طوله 16 حرفًا بالضبط، ويتم تحميله من ملف التهيئة application.properties).
 * - keySpec: كائن يمثل المفتاح بصيغة مناسبة لخوارزمية AES.
 *
 * الدوال الرئيسية:
 * - init(): تهيئة المفتاح بعد تحميل الكلاس، ويتحقق من صحة المفتاح وطوله.
 * - encrypt(String value): تشفير النص العادي وإرجاع النص المشفر في صيغة Base64.
 * - decrypt(String encryptedValue): فك تشفير النص المشفر بصيغة Base64 وإرجاع النص العادي.
 *
 * معالجة الأخطاء:
 * - في حالة وجود خطأ أثناء التهيئة أو التشفير أو فك التشفير، يتم تسجيل الخطأ باستخدام Logger مع تقديم معلومات جزئية عن النص المتضرر.
 * - ترمي دوال التشفير وفك التشفير استثناء RuntimeException في حال حدوث مشكلة، لضمان كشف الخطأ ومعالجته على المستوى الأعلى.
 *
 * ملاحظات أمنية:
 * - مفتاح التشفير يجب أن يكون سريًا ومخزنًا بشكل آمن في ملف التهيئة ولا يُفصح عنه في الكود المصدري.
 * - يتم استخدام خوارزمية AES التي تعتبر معيارًا قويًا للتشفير المتماثل.
 */

@Component
public class EncryptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(EncryptionUtil.class);

    private static final String ALGORITHM = "AES";

    @Value("${encryption.secret.key}")
    private String secretKey;

    private SecretKeySpec keySpec;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.length() != 16) {

            logger.error(
                    "CONFIGURATION_ERROR: AES Encryption secret key must be exactly 16 characters long and configured in application.properties as 'encryption.secret.key'. Current length: {}",
                    (secretKey != null ? secretKey.length() : "null")
            );
            throw new IllegalArgumentException(
                    "AES Encryption secret key must be exactly 16 characters long and configured in application.properties as 'encryption.secret.key'. Current length: " +
                            (secretKey != null ? secretKey.length() : "null")
            );
        }
        this.keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        logger.info("EncryptionUtil initialized successfully. Key length: {}", secretKey.length());
    }

    public String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            logger.debug("Value encrypted successfully.");
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {

            logger.error("ENCRYPTION_FAILED: Encryption error for value (partial): {}. Error: {}", value.substring(0, Math.min(value.length(), 20)) + "...", e.getMessage(), e);
            throw new RuntimeException("Encryption error", e);
        }
    }

    public String decrypt(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedValue));
            logger.debug("Value decrypted successfully.");
            return new String(decrypted);
        } catch (Exception e) {

            logger.error("DECRYPTION_FAILED: Decryption error for value (partial): {}. Error: {}", encryptedValue.substring(0, Math.min(encryptedValue.length(), 50)) + "...", e.getMessage(), e);
            throw new RuntimeException("Decryption error", e);
        }
    }
}