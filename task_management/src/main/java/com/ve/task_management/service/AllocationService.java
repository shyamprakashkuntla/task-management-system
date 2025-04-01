package com.ve.task_management.service;

import java.util.List;

import com.ve.task_management.model.Allocation;
import com.ve.task_management.payload.AllocationRequest;
import com.ve.task_management.payload.AllocationResponse;


public interface AllocationService {
	List<AllocationResponse> getAllAllocations();
	Allocation createAllocation(AllocationRequest  allocationRequest);
	Allocation getByAllocationId(Integer allocationId);
	void  updateAllocation(Integer allocationId ,AllocationRequest  allocationRequest);
	void deleteAllocationById (Integer allocationId);
	

	

	

	//String saveAll(List<CommunityJobResponse> communityJobResponse);

	

	



//	public CommunityJob updateJobStatus(Integer communityIdNo, String statusType);
	

}
