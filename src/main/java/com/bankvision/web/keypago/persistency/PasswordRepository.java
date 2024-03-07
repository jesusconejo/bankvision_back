package com.bankvision.web.keypago.persistency;

import com.bankvision.web.keypago.models.Password;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PasswordRepository extends MongoRepository<Password, String> {
    Password findByUser(String user);
}
