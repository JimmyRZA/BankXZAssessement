package com.banking.system.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SavingsAccount {
	public SavingsAccount() {
		this.balance = 500;
	}
	@Id
	private String customerID;
	
	
	private long accountNumber;
	private String customerName;
	private String customerSurname;
	private int accountAge;
	private double balance;
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerSurname() {
		return customerSurname;
	}
	public void setCustomerSurname(String customerSurname) {
		this.customerSurname = customerSurname;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	public int getAccountAge() {
		return accountAge;
	}
	public void setAccountAge(int accountAge) {
		this.accountAge = accountAge;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	

}
