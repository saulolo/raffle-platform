package com.cidenet.raffleplatform.repository;

import com.cidenet.raffleplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Se encarga de gestionar la persistencia de los datos de los usuarios.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por número de documento.
     * 
     * @param documentNumber, número de documento.
     * @return un objeto de tipo Optional<User>.
     */
    Optional<User> findByDocumentNumber(String documentNumber);

    /**
     * Verifica si existe un usuario por número de documento.
     * 
     * @param documentNumber, número de documento.
     * @return un valor booleano.
     */
    Boolean existsByDocumentNumber(String documentNumber);

}
