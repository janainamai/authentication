package br.com.authentication.service.impl;

import br.com.authentication.domain.Role;
import br.com.authentication.domain.UserAccount;
import br.com.authentication.dto.CreateUserRoleDto;
import br.com.authentication.dto.UserRoleCreatedDto;
import br.com.authentication.exception.BadRequestException;
import br.com.authentication.repository.RoleRepository;
import br.com.authentication.repository.UserRepository;
import br.com.authentication.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserRoleCreatedDto saveUserRole(CreateUserRoleDto dto) {
        UserAccount userAccount = getUserAccount(dto);
        List<Role> roles = getRoles(dto);

        userAccount.setRoles(roles);
        userRepository.save(userAccount);

        UserRoleCreatedDto userRoleCreatedDto = new UserRoleCreatedDto();
        userRoleCreatedDto.setUsername(userAccount.getUsername());
        userRoleCreatedDto.setRoles(roles.stream().map(Role::getName).collect(Collectors.toList()));

        return userRoleCreatedDto;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    private List<Role> getRoles(CreateUserRoleDto dto) {
        List<Role> roles = roleRepository.findAllById(dto.getRoleIds());
        if (CollectionUtils.isEmpty(roles)) {
            throw new BadRequestException("Nenhuma role foi encontrada");
        }
        return roles;
    }

    private UserAccount getUserAccount(CreateUserRoleDto dto) {
        return userRepository.findById(dto.getIdUser())
                .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));
    }
}
