package com.ve.task_management.service;

import java.util.List;

import com.ve.task_management.model.Clients;
import com.ve.task_management.payload.ClientsRequest;
import com.ve.task_management.payload.ClientsResponse;

public interface ClientService {
	List<ClientsResponse> getAllClients();
	Clients getClientById(Integer clientId);
	Clients createClient(ClientsRequest clientRequest);
	void updateClient(Integer clientId,ClientsRequest clientRequest);
	void deleteClientById(Integer clientId);
	
	
	
	
	
}
