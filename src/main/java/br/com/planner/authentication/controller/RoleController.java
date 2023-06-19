package br.com.planner.authentication.controller;

import br.com.planner.authentication.domain.Role;
import br.com.planner.authentication.dto.CreateUserRoleDto;
import br.com.planner.authentication.dto.UserRoleCreatedDto;
import br.com.planner.authentication.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth/roles")
public class RoleController {

    @Autowired
    private UserRoleService roleService;

    @GetMapping()
    public ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserRoleCreatedDto> createUserRole(@Valid @RequestBody CreateUserRoleDto dto) {
        return new ResponseEntity<>(roleService.saveUserRole(dto), HttpStatus.CREATED);
    }

}
