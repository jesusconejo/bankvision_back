package com.bankvision.web.keypago.controllers;

import com.bankvision.web.keypago.dto.PasswordRequest;
import com.bankvision.web.keypago.factory.UserFactory;
import com.bankvision.web.keypago.models.Password;
import com.bankvision.web.keypago.models.User;
import com.bankvision.web.keypago.persistency.PasswordRepository;
import com.bankvision.web.keypago.persistency.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
        RequestMethod.OPTIONS }, allowedHeaders = "*")
public class AuthController {
    @Autowired
    private UserFactory userFactory;
    @Autowired
    private PasswordRepository passwordRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/userLogin")
    public ResponseEntity userLogin(@RequestBody PasswordRequest passwordRequest) {
        Optional<User> user =userRepository.findByIdentificationNumber(passwordRequest.getUser());

        if(user.isEmpty()){
            return new ResponseEntity("Usuario no existe", HttpStatus.NOT_FOUND);
        }
        Password password = passwordRepository.findByUser(passwordRequest.getUser());
        if(!userFactory.checkPassword(passwordRequest.getPassword(),password.getPassword())){
            return new ResponseEntity("Password Invalido", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(user, HttpStatus.ACCEPTED);
    }

    @PutMapping("/updatePassword")
    public ResponseEntity updatePassword( @RequestBody PasswordRequest passwordRequest) {

        Optional<User> existingUser = userRepository.findByIdentificationNumber(passwordRequest.getUser());
        if(existingUser.isEmpty()){
            return new ResponseEntity<>("User not found with id: " + passwordRequest.getUser(), HttpStatus.NOT_FOUND);
        }

       Password password = passwordRepository.findByUser(passwordRequest.getUser());
        //password.setUser(passwordRequest.getUser());
        password.setPassword(userFactory.encryptPassword(passwordRequest.getPassword()));
        passwordRepository.save(password);
        return new ResponseEntity("OK", HttpStatus.ACCEPTED);

    }
}
