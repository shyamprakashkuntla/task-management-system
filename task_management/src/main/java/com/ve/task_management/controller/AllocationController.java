package com.ve.task_management.controller;


import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ve.task_management.constants.CommonConstants;
import com.ve.task_management.constants.ResponseWrapper;
import com.ve.task_management.model.Allocation;
import com.ve.task_management.payload.AllocationRequest;
import com.ve.task_management.payload.AllocationResponse;
import com.ve.task_management.service.AllocationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name="ALLOCATION CONTROLLER",description ="Get,update,Read all,Read by ID,Delete by Id")
public class AllocationController {
	@Autowired
	AllocationService allocationService;
	@Autowired 
	private ModelMapper modelMapper;
	  @GetMapping("/getAllAllocation")
	    public ResponseWrapper<List<AllocationResponse>> getAllAllocation() {
	        List<AllocationResponse> allocationList = allocationService.getAllAllocations();
	        return new ResponseWrapper<>(HttpStatus.OK, "Profiles fetched successfully", true, allocationList);
	    }

	    @PostMapping("/saveAllocation")
	    public ResponseWrapper<Void> createAllocation(@Valid @RequestBody AllocationRequest allocationRequest) {
	        allocationService.createAllocation(allocationRequest);
	        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.ALLOCATIION_SUCCESSFULLY, true);
	    }

	 
	    @GetMapping("/Allocation/{allocationId}")
	    public ResponseWrapper<AllocationResponse> getAllocationById(@PathVariable Integer allocationId) {
	        Allocation al = allocationService.getByAllocationId(allocationId);

	        // Use ModelMapper to map the entity to the response DTO
	        AllocationResponse response = modelMapper.map(al, AllocationResponse.class);

	        return new ResponseWrapper<>(HttpStatus.OK, "Allocation fetched successfully", true, response);
	    }
	    @DeleteMapping("/deleteAllocation/{allocationId}")
	    public ResponseWrapper<Void> deleteAllocation(@PathVariable Integer allocationId) {
	        allocationService.deleteAllocationById(allocationId);
	        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.ALLOCATION_DELETED_SUCCESSFULLY, true);
	    }

	    @PutMapping("/updateAllocation/{allocationId}")
	    public ResponseWrapper<Void> updateAllocation(
	            @PathVariable Integer allocationId,
	            @Valid @RequestBody AllocationRequest allocationRequest) {
	        allocationService.updateAllocation(allocationId, allocationRequest);
	        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.ALLOCATION_UPDATED_SUCCESSFULLY, true);
	    }


}
