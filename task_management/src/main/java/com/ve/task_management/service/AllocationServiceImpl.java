package com.ve.task_management.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ve.task_management.model.Allocation;
import com.ve.task_management.payload.AllocationRequest;
import com.ve.task_management.payload.AllocationResponse;
import com.ve.task_management.repository.AllocationRepository;

@Service
public class AllocationServiceImpl implements AllocationService {

    @Autowired
    private AllocationRepository allocationRepository;

    @Autowired
    private ModelMapper modelMapper;

    // ✅ Get all allocations
    @Override
    public List<AllocationResponse> getAllAllocations() {
        List<Allocation> allocations = allocationRepository.findAll();

        return allocations.stream()
                .map(allocation -> modelMapper.map(allocation, AllocationResponse.class))
                .collect(Collectors.toList());
    }

    // ✅ Create a new allocation
    @Override
    public Allocation createAllocation(AllocationRequest allocationRequest) {
        Allocation allocation = modelMapper.map(allocationRequest, Allocation.class);
        return allocationRepository.save(allocation);
    }

    // ✅ Get allocation by ID (throwing custom exception)
    @Override
    public Allocation getByAllocationId(Integer allocationId) {
        return allocationRepository.findById(allocationId)
                .orElseThrow(() -> new RuntimeException("Allocation not found with ID: " + allocationId));
    }

    // ✅ Soft delete allocation by ID
    @Override
    public void deleteAllocationById(Integer allocationId) {
        Optional<Allocation> optionalAllocation = allocationRepository.findByAllocationIdAndDeletedFalse(allocationId);

        if (optionalAllocation.isPresent()) {
            Allocation allocation = optionalAllocation.get();
            allocation.setDeleted(true);  // Mark as deleted
            allocationRepository.save(allocation);
        } else {
            throw new RuntimeException("Allocation not found or already deleted with ID: " + allocationId);
        }
    }

    // ✅ Update allocation by ID
    @Override
    public void updateAllocation(Integer allocationId, AllocationRequest allocationRequest) {
        Allocation existingAllocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new RuntimeException("Allocation not found with ID: " + allocationId));

        // Map the updated fields
        modelMapper.map(allocationRequest, existingAllocation);

        // Save the updated entity
        allocationRepository.save(existingAllocation);
    }
}
