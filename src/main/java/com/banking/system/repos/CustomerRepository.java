package com.banking.system.repos;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.banking.system.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, String>{
	
	@Override
	public Optional<Customer> findById(String id);

}
