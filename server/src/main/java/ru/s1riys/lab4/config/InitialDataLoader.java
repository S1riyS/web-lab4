package ru.s1riys.lab4.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import ru.s1riys.lab4.domain.entity.Permission;
import ru.s1riys.lab4.repository.PermissionRepository;

@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        createPermissionIfNotFound(Permission.DOT_CREATE);
        createPermissionIfNotFound(Permission.DOT_READ);
        createPermissionIfNotFound(Permission.DOT_DELETE);

        alreadySetup = true;
    }

    @Transactional
    private Permission createPermissionIfNotFound(String name) {
        Permission permission = permissionRepository.findByName(name);
        if (permission == null) {
            permission = Permission.builder()
                    .name(name)
                    .build();
            permissionRepository.save(permission);
        }
        return permission;
    }
}
