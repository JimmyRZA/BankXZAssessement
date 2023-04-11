package com.banking.system.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.banking.system.model.SavingsAccount;

public interface SavingsAccountRepository extends CrudRepository<SavingsAccount, String>{
	
	public Optional<SavingsAccount> findById(String id);
	
	@Query(nativeQuery = true, value="SELECT * FROM CURRENT_ACCOUNT  WHERE CUSTOMERD=:id AND ACCOUNT_NUMBER=:accountNo")
	public List<SavingsAccount> findAllByIdAndAccountNo(@Param("id")  String id, @Param("accountNo") int accountNo);

}
