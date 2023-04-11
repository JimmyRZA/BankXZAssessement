package com.banking.system.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.banking.system.model.CurrentAccount;


public interface CurrentAccountRepository extends CrudRepository<CurrentAccount, String>{
	
	public Optional<CurrentAccount> findById(String id);
	
	@Query(nativeQuery = true, value="SELECT * FROM CURRENT_ACCOUNT  WHERE CUSTOMERD=:id AND ACCOUNT_NUMBER=:accountNo")
	public List<CurrentAccount> findAllByIdAndAccountNo(@Param("id")  String id, @Param("accountNo") int accountNo);

}
