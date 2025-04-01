package com.ve.task_management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ve.task_management.model.Allocation;

public interface AllocationRepository extends JpaRepository<Allocation,Integer> {

	Optional<Allocation> findByAllocationIdAndDeletedFalse(Integer allocationId);

}
