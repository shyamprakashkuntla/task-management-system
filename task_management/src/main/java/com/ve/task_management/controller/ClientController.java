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
import com.ve.task_management.model.Clients;
import com.ve.task_management.payload.ClientsRequest;
import com.ve.task_management.payload.ClientsResponse;
import com.ve.task_management.service.ClientService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name="CLIENT CONTROLLER",description ="Get,update,Read all,Read by ID,Delete by Id")
public class ClientController {
	@Autowired
	ClientService clientService;
	@Autowired 
	private ModelMapper modelMapper;
	 @GetMapping("/getAllClients")
	    public ResponseWrapper<List<ClientsResponse>> getAllClients() {
	        List<ClientsResponse> clientList = clientService.getAllClients();
	        return new ResponseWrapper<>(HttpStatus.OK, "Clients fetched successfully", true, clientList);
	    }

	    @PostMapping("/saveClients")
	    public ResponseWrapper<Void> createClient(@Valid @RequestBody ClientsRequest clientRequest) {
	        clientService.createClient(clientRequest);
	        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.CLIENTS_SUCCESSFULLY, true);
	    }

	 
	    @GetMapping("/Clients/{clientId}")
	    public ResponseWrapper<ClientsResponse> getClientById(@PathVariable Integer clientId) {
	        Clients cl = clientService.getClientById(clientId);

	        // Use ModelMapper to map the entity to the response DTO
	        ClientsResponse response = modelMapper.map(cl, ClientsResponse.class);

	        return new ResponseWrapper<>(HttpStatus.OK, "Client fetched successfully", true, response);
	    }
	    @DeleteMapping("/deleteClient/{clientId}")
	    public ResponseWrapper<Void> deleteClient(@PathVariable Integer clientId) {
	        clientService.deleteClientById(clientId);
	        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.CLIENT_DELETED_SUCCESSFULLY, true);
	    }

	    @PutMapping("/updateClient/{clientId}")
	    public ResponseWrapper<Void> updateClient(
	            @PathVariable Integer clientId,
	            @Valid @RequestBody ClientsRequest clientsRequest) {
	        clientService.updateClient(clientId, clientsRequest);
	        return new ResponseWrapper<>(HttpStatus.OK, CommonConstants.CLIENT_UPDATED_SUCCESSFULLY, true);
	    }

}
