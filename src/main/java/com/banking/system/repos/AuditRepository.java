package com.banking.system.repos;

import org.springframework.data.repository.CrudRepository;

import com.banking.system.model.AuditEntry;

public interface AuditRepository extends CrudRepository<AuditEntry, String>{

}
