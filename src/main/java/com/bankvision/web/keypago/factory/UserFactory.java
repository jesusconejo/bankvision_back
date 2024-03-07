package com.bankvision.web.keypago.factory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import com.bankvision.web.keypago.dto.PasswordRequest;
import com.bankvision.web.keypago.models.Password;
import com.bankvision.web.keypago.models.User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserFactory {
    public User createUser(String identificationNumber, String identificationType, Date dateOfBirth, String phone, String email) {
        User user = new User();
        user.setIdentificationNumber(identificationNumber);
        user.setIdentificationType(identificationType);
        user.setDateOfBirth(dateOfBirth);
        user.setPhone(phone);
        user.setEmail(email);
        // Calcular la edad
        user.setAge(calcularEdad(dateOfBirth));
        return user;
    }

    public int calcularEdad(Date fechaNacimiento) {
        // Obtener la fecha actual
        Calendar fechaActual = Calendar.getInstance();
        // Obtener la fecha de nacimiento en un objeto Calendar
        Calendar fechaNac = Calendar.getInstance();
        fechaNac.setTime(fechaNacimiento);

        // Calcular la edad
        int edad = fechaActual.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);

        // Ajustar la edad si aún no ha pasado el cumpleaños este año
        if (fechaActual.get(Calendar.DAY_OF_YEAR) < fechaNac.get(Calendar.DAY_OF_YEAR)) {
            edad--;
        }

        return edad;
    }

    public Password createPassword(String user, String password) {
        Password password1 = new Password();
        password1.setUser(user);
        password1.setPassword(encryptPassword(password));
        return password1;
    }

    public  String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            String hashedPassword = Base64.getEncoder().encodeToString(hashBytes);
            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkPassword(String password, String hashedPassword) {
        String encryptedPassword = encryptPassword(password);
        return encryptedPassword.equals(hashedPassword);
    }
    public Password createCredentials(PasswordRequest passwordRequest) {

       return createPassword(passwordRequest.getUser(), passwordRequest.getPassword());

    }
}
