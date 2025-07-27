
package com.example.hospital.hospital_system.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {



    /**
     * ChangePasswordRequest
     *
     * هذا الكلاس يمثل نموذج البيانات (DTO) المستخدم عند تقديم طلب لتغيير كلمة مرور المستخدم.
     *
     * يحتوي على الحقول التالية:
     * - currentPassword: كلمة المرور الحالية التي يستخدمها المستخدم (مطلوبة)
     * - newPassword: كلمة المرور الجديدة التي يرغب المستخدم في تعيينها (مطلوبة ويجب أن تتكون من 8 أحرف على الأقل)
     * - confirmNewPassword: تأكيد كلمة المرور الجديدة (مطلوبة)
     *
     * تم استخدام التعليقات التوضيحية من Jakarta Bean Validation للتحقق من صحة القيم المدخلة:
     * - @NotBlank: للتحقق من أن الحقل غير فارغ
     * - @Size: لتحديد الحد الأدنى لطول كلمة المرور الجديدة
     *
     * يمكن لاحقًا تعزيز التحقق بإضافة نمط (Regex) لضمان قوة كلمة المرور من حيث احتوائها على:
     * - حرف كبير، حرف صغير، رقم، ورمز خاص
     *
     * يُستخدم هذا الكلاس ضمن `UserController` لمعالجة منطق تغيير كلمة المرور.
     */


    @NotBlank(message = "كلمة المرور الحالية مطلوبة.")
    private String currentPassword;

    @NotBlank(message = "كلمة المرور الجديدة مطلوبة.")
    @Size(min = 8, message = "كلمة المرور الجديدة يجب أن تتكون من 8 أحرف على الأقل.")
    // يمكن إضافة المزيد من التحقق من قوة كلمة المرور هنا باستخدام Regular Expressions
    // مثال: @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()])(?=\\S+$).{8,}$", message = "كلمة المرور يجب أن تحتوي على حرف كبير وصغير ورقم ورمز.")
    private String newPassword;

    @NotBlank(message = "تأكيد كلمة المرور الجديدة مطلوب.")
    private String confirmNewPassword;

    // Getters and Setters
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}