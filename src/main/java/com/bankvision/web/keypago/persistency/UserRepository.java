package com.bankvision.web.keypago.persistency;

import com.bankvision.web.keypago.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByIdentificationNumber(String iNumber);

    //User save(User user);
}
