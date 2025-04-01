package com.ve.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ve.task_management.model.Clients;

public interface ClientsRepository extends JpaRepository<Clients,Integer> {

	Optional<Clients> findByClientIdAndDeletedFalse(Integer clientId);

}
