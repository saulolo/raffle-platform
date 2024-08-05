package com.cidenet.raffleplatform.repository;

import com.cidenet.raffleplatform.enums.RoleEnum;
import com.cidenet.raffleplatform.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Se encarga de gestionar la persistencia de los datos de los roles.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Busca un rol por su nombre.
     *
     * @param roleEnum, nombre del rol.
     * @return un objeto de tipo Optional<Role>.
     */
    Optional<Role> findByRoleEnum(RoleEnum roleEnum);
}
