package com.devops.hafalat_backend.repository;
import com.devops.hafalat_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
}
