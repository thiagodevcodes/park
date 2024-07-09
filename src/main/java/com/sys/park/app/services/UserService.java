package com.sys.park.app.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sys.park.app.dtos.Auth.AuthenticationDto;
import com.sys.park.app.dtos.Auth.LoginResponseDto;
import com.sys.park.app.dtos.Auth.RegisterForm;
import com.sys.park.app.dtos.User.UserDto;
import com.sys.park.app.dtos.User.UserFormUpdate;
import com.sys.park.app.models.PersonModel;
import com.sys.park.app.models.UserModel;
import com.sys.park.app.repositories.PersonRepository;
import com.sys.park.app.repositories.UserRepository;
import com.sys.park.app.services.exceptions.BusinessRuleException;
import com.sys.park.app.services.exceptions.DataIntegrityException;
import com.sys.park.app.services.exceptions.NotFoundException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserDto> findAll() {
        try {
            List<UserModel> userModelList = userRepository.findAll();

            return userModelList.stream()
                    .map(user -> modelMapper.map(user, UserDto.class))
                    .collect(Collectors.toList());
        } catch (BusinessRuleException e) {
            throw new BusinessRuleException("Não é possível consultar os Usuários!");
        }
    }

    public UserDto findById(Integer id) {
        try {
            UserModel userModel = userRepository.findById(id).get();
            return modelMapper.map(userModel, UserDto.class);
        } catch (NoSuchElementException e) {
            throw new NotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + UserModel.class.getName());
        }
    }

    public UserDto insert(UserDto userDto) {
        try {
            UserModel newUser = modelMapper.map(userDto, UserModel.class);

            UserDetails byLogin = userRepository.findByUsername(newUser.getUsername());

            if (!byLogin.equals(null)) {
                throw new DataIntegrityException("Usuário já registrado.");
            }

            newUser = userRepository.save(newUser);
            return modelMapper.map(newUser, UserDto.class);

        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Usuário não foi(foram) preenchido(s).");
        }
    }

    public UserDto updateById(UserFormUpdate userForm, Integer id) {
        try {
            Optional<UserModel> userExist = userRepository.findById(id);

            if (userExist.isPresent()) {
                UserModel userUpdated = userExist.get();

                modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
                modelMapper.map(userForm, userUpdated);

                if (!userForm.getPassword().equals(null)) {
                    String encryptedPassword = new BCryptPasswordEncoder().encode(userForm.getPassword());
                    userUpdated.setPassword(encryptedPassword);
                }

                userUpdated = userRepository.save(userUpdated);

                return modelMapper.map(userUpdated, UserDto.class);
            } else {
                throw new DataIntegrityException("O Id do Usuário não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) do Usuário não foi(foram) preenchido(s).");
        }
    }

    public void deleteById(Integer id) {
        try {
            if (userRepository.existsById(id)) {
                userRepository.deleteById(id);

            } else {
                throw new DataIntegrityException("O Id do Usuário não existe na base de dados!");
            }
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possível excluir o Usuário!");
        }
    }

    public UserDto addNewUser(RegisterForm data) {
        try {
            PersonModel person;

            if (personRepository.existsByCpf(data.getCpf())) {
                person = personRepository.findByCpf(data.getCpf()).get();
                Optional<UserModel> user = this.userRepository.findByIdPerson(person.getId());

                if (user.isPresent()) {
                    if (user.get().getIsActive().equals(true))
                        throw new DataIntegrityException("Usuário já está ativo");

                    if (personRepository.existsByEmail(data.getEmail()) && user.get().getIsActive().equals(true))
                        throw new DataIntegrityException("Email já cadastrado");

                    if (user.get().getIsActive().equals(false)) {
                        user.get().setIsActive(true);

                        person.setEmail(data.getEmail());
                        person.setPhone(data.getPhone());
                        person.setName(data.getName());

                        person = personRepository.save(person);
                        this.userRepository.save(user.get());
                    }
                }
            } else {
                person = new PersonModel(data.getName(), data.getCpf(), data.getEmail(), data.getPhone());
                person = personRepository.save(person);
            }

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
            UserModel newUser = new UserModel(data.getUsername(), encryptedPassword, data.getRole(),
                    person.getId());
            newUser = this.userRepository.save(newUser);

            return modelMapper.map(newUser, UserDto.class);

        } catch (

        DataIntegrityViolationException e) {
            throw new DataIntegrityException("Campo(s) obrigatório(s) da Usuário não foi(foram) preenchido(s).");
        }
    }

    public LoginResponseDto loginUser(AuthenticationDto data) {
        if (userRepository.existsByUsername(data.getUsername())) {
            UserDetails user = userRepository.findByUsername(data.getUsername());

            if (!user.isEnabled())
                throw new DataIntegrityException("Usuário não está ativo!");
        }

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(), data.getPassword());
        System.out.println(usernamePassword);
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return new LoginResponseDto(token);
    }
}
