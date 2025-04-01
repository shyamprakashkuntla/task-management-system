package com.ve.task_management.service;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ve.task_management.model.Clients;
import com.ve.task_management.payload.ClientsRequest;
import com.ve.task_management.payload.ClientsResponse;
import com.ve.task_management.repository.ClientsRepository;
@Service
public class ClientServiceImpl implements ClientService {
	@Autowired
	ClientsRepository clientRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public List<ClientsResponse> getAllClients() {
		
		List<Clients> list_clients = clientRepository.findAll();

		List<ClientsResponse> list_res = list_clients.stream()
				.map(client -> modelMapper.map(client, ClientsResponse.class))
				.collect(Collectors.toList());
		return list_res;
	
	}

	@Override
	public Clients getClientById(Integer clientId) {
		return clientRepository.findById(clientId)
				.orElseThrow(() -> new RuntimeException("client not found for ID: " + clientId));
		
	}

	@Override
	public Clients createClient(ClientsRequest clientRequest) {
		Clients client = modelMapper.map(clientRequest, Clients.class);
        return clientRepository.save(client);
	}

	@Override
	public void updateClient(Integer clientId, ClientsRequest clientRequest) {
		Clients existingClient = clientRepository.findById(clientId)
		        .orElseThrow(() -> new RuntimeException("Allocation not found with id: " + clientId));
		    
		    // Use ModelMapper to map updated fields from the request to the existing entity
		    modelMapper.map(clientRequest, existingClient);

		    // Save the updated entity
		    clientRepository.save(existingClient);
		
	}

	@Override
	public void deleteClientById(Integer clientId) {
		 Optional<Clients> optionalClient = clientRepository.findByClientIdAndDeletedFalse(clientId);

	        if (optionalClient.isPresent()) {
	            Clients client = optionalClient.get();
	          client.setDeleted(true); // Mark as deleted
	            clientRepository.save(client); // Save the updated profile
	        } else {
	            throw new RuntimeException("client not found or already deleted for Client ID: " + clientId);}
		
	}
		
		
	}

	


