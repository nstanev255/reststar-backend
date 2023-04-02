package com.reststar.repository;

import com.reststar.dto.UserInformationDTO;
import com.reststar.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInformationRepository extends JpaRepository<UserInformation, Long> {
    UserInformation findByUserEntity_Id(Long id);
    UserInformation findByEmail(String email);
}
