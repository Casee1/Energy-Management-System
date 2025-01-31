package com.example.devicemanagement.repository;

import com.example.devicemanagement.dto.DeviceUserDTO;
import com.example.devicemanagement.entity.DeviceUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceUserRepository extends JpaRepository<DeviceUser, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM DeviceUser du WHERE du.idUser = :idUser")
    void deleteByIdUser(@Param("idUser") UUID idUser);

    List<DeviceUser> findByIdUser(UUID id_user);
}
