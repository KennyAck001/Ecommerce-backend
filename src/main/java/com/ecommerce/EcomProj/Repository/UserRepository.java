package com.ecommerce.EcomProj.Repository;

import com.ecommerce.EcomProj.Model.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByUserName(String username);

    boolean existsByEmail(@NotBlank @Size(max = 50) String email);


    boolean existsByUserName(@NotBlank @Size(min = 3, max = 20) String userName);
}
