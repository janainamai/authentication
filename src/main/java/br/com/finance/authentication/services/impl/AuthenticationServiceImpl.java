package br.com.finance.authentication.services.impl;

import br.com.finance.authentication.domain.entities.RoleEntity;
import br.com.finance.authentication.domain.entities.UserEntity;
import br.com.finance.authentication.infra.exception.BadRequestException;
import br.com.finance.authentication.repositories.RoleRepository;
import br.com.finance.authentication.repositories.UserRepository;
import br.com.finance.authentication.services.AuthenticationService;
import br.com.finance.authentication.services.EncoderService;
import br.com.finance.authentication.services.TokenService;
import br.com.finance.authentication.services.dto.LoginDto;
import br.com.finance.authentication.services.dto.RegisterUserDto;
import br.com.finance.authentication.utils.RoleConstants;
import br.com.finance.authentication.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserValidator validator;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private EncoderService encoder;
    @Autowired
    private TokenService tokenService;

    @Override
    @Transactional
    public void register(RegisterUserDto dto) {
        validator.validateSamePassword(dto.getPassword(), dto.getConfirmPassword());
        validator.validateUsernameAlreadyExists(dto.getLogin());

        UserEntity user = new UserEntity();
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));

        RoleEntity freeRole = roleRepository.findByName(RoleConstants.FREE_USER)
                .orElseThrow(() -> new BadRequestException("Role not found with name: " + RoleConstants.FREE_USER));
        user.setRoles(List.of(freeRole));

        userRepository.save(user);
    }

    @Override
    public String login(LoginDto dto) {
        var loginPassword = new UsernamePasswordAuthenticationToken(dto.getLogin(), dto.getPassword());
        Authentication auth = authenticationManager.authenticate(loginPassword);
        return tokenService.generateToken((UserEntity) auth.getPrincipal());
    }

}
