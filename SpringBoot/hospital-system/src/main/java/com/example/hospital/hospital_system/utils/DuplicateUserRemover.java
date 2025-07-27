package com.example.hospital.hospital_system.utils;


import com.example.hospital.hospital_system.repository.UserRepository;
import com.example.hospital.hospital_system.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;
import org.springframework.context.event.EventListener;

import java.util.*;

@Component
public class DuplicateUserRemover {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;
    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void removeDuplicateUsers() {
        // حذف من جدول users_roles للمستخدمين المكررين
        String deleteUsersRolesSql = "DELETE FROM users_roles " +
                "WHERE user_id NOT IN (" +
                "SELECT * FROM (" +
                "SELECT MIN(id) FROM users GROUP BY username" +
                ") AS unique_users)";

        entityManager.createNativeQuery(deleteUsersRolesSql).executeUpdate();

        // حذف المستخدمين المكررين من جدول users
        String deleteUsersSql = "DELETE FROM users " +
                "WHERE id NOT IN (" +
                "SELECT * FROM (" +
                "SELECT MIN(id) FROM users GROUP BY username" +
                ") AS unique_users)";

        entityManager.createNativeQuery(deleteUsersSql).executeUpdate();

        System.out.println("Duplicate users and their roles removed.");
    }
}
