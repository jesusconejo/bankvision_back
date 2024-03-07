package com.bankvision.web.keypago.controllers;

import com.bankvision.web.keypago.exception.ResourceNotFoundException;
import com.bankvision.web.keypago.models.Password;
import com.bankvision.web.keypago.persistency.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bankvision.web.keypago.dto.UserRequest;
import com.bankvision.web.keypago.factory.UserFactory;
import com.bankvision.web.keypago.models.User;
import com.bankvision.web.keypago.persistency.UserRepository;

@RestController
@RequestMapping("/api/users/")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
        RequestMethod.OPTIONS }, allowedHeaders = "*")
public class UserController {

      @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private PasswordRepository passwordRepository;

    @PostMapping("/createUser")
    public ResponseEntity createUser(@RequestBody UserRequest userRequest) {
        //validarContrasena(userRequest.getPassword());
        User user = userFactory.createUser(userRequest.getIdentificationNumber(), userRequest.getIdentificationType(), userRequest.getDateOfBirth(), userRequest.getPhone(), userRequest.getEmail());
        Password password = new Password();
        password.setUser(userRequest.getIdentificationNumber());
        password.setPassword(userFactory.encryptPassword(userRequest.getPassword()));
                passwordRepository.save(password);
        return new ResponseEntity(userRepository.save(user), HttpStatus.ACCEPTED);
    }

    @GetMapping("/getUser")
    public User getUser(@RequestParam String id) {
        return userRepository.findByIdentificationNumber(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @PutMapping("/updateUser")
    public User updateUser(@RequestParam String id, @RequestBody UserRequest userRequest) {

        User existingUser = userRepository.findByIdentificationNumber(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        existingUser.setIdentificationNumber(userRequest.getIdentificationNumber());
        existingUser.setIdentificationType(userRequest.getIdentificationType());
        existingUser.setDateOfBirth(userRequest.getDateOfBirth());
        existingUser.setPhone(userRequest.getPhone());
        existingUser.setEmail(userRequest.getEmail());
        existingUser.setAge(userFactory.calcularEdad( userRequest.getDateOfBirth()));
        return userRepository.save(existingUser);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam String id) {
        User existingUser = userRepository.findByIdentificationNumber(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        userRepository.delete(existingUser);
        return ResponseEntity.ok().build();
    }

    private void validarContrasena(String password) {
        // Validar la contraseña según los criterios requeridos
    }
    
}
