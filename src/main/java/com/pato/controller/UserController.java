package com.pato.controller;

import com.pato.model.Role;
import com.pato.model.UserSec;
import com.pato.service.interfaces.IRoleService;
import com.pato.service.interfaces.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    private final IRoleService roleService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserSec userSec) {

        Set<Role> roleList = new HashSet<>();
        Role readRole;

        userSec.setPassword(userService.encriptPassword(userSec.getPassword()));

        for (Role role : userSec.getRolesList()) {
            readRole = roleService.findById(role.getId()).orElse(null);
            if (readRole != null) {
                roleList.add(readRole);
            }
        }

        if (roleList.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Los roles enviados no existen.");
        }

        userSec.setRolesList(roleList);

        UserSec newUser = userService.save(userSec);

        return ResponseEntity.ok(newUser);
    }
}