package com.workintech.ecommerce.repository;

import com.workintech.ecommerce.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setAuthority("ROLE_USER");
        roleRepository.save(testRole);
    }

    @DisplayName("Can save and retrieve role by authority")
    @Test
    void saveAndFindByAuthority() {
        Optional<Role> retrievedRole = roleRepository.findByAuthority("ROLE_USER");
        assertTrue(retrievedRole.isPresent(), "Role should be found by authority.");
        assertEquals("ROLE_USER", retrievedRole.get().getAuthority(), "Authority should match.");
    }

    @DisplayName("Can update role authority")
    @Test
    void updateRoleAuthority() {
        Optional<Role> retrievedRole = roleRepository.findByAuthority("ROLE_USER");
        assertTrue(retrievedRole.isPresent(), "Role should be found by authority.");

        Role roleToUpdate = retrievedRole.get();
        roleToUpdate.setAuthority("ROLE_ADMIN");
        roleRepository.save(roleToUpdate);

        Optional<Role> updatedRole = roleRepository.findByAuthority("ROLE_ADMIN");
        assertTrue(updatedRole.isPresent(), "Updated role should be found by new authority.");
        assertEquals("ROLE_ADMIN", updatedRole.get().getAuthority(), "Updated authority should match.");
    }

    @DisplayName("Can delete role")
    @Test
    void deleteRole() {
        Optional<Role> retrievedRole = roleRepository.findByAuthority("ROLE_USER");
        assertTrue(retrievedRole.isPresent(), "Role should be found by authority.");

        Role roleToDelete = retrievedRole.get();
        roleRepository.delete(roleToDelete);

        Optional<Role> deletedRole = roleRepository.findByAuthority("ROLE_USER");
        assertFalse(deletedRole.isPresent(), "Role should be deleted and not found.");
    }

    @DisplayName("Cannot find role by nonexistent authority")
    @Test
    void findByAuthorityNotFound() {
        Optional<Role> role = roleRepository.findByAuthority("NONEXISTENT_ROLE");
        assertFalse(role.isPresent(), "Role should not be found by nonexistent authority.");
    }
}
