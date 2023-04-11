package com.banking.system.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@RequestScope
@Component
public class CurrentAccountDetail {
	private long accountNumber;
	private long targetAccount;
	private String customerID;
	private String customerName;
	private String customerSurname;
	private String accountType;
	private String bankName;
	private String branchCode;
	private String intransferamount;
	private int accountAge;
	private double balance;
	private double transferAmount;
	
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
	public double getTransferAmount() {
		return transferAmount;
	}
	public void setTransferAmount(int transferAmount) {
		this.transferAmount = transferAmount;
	}
	public long getTargetAccount() {
		return targetAccount;
	}
	public void setTargetAccount(long targetAccount) {
		this.targetAccount = targetAccount;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getIntransferamount() {
		return intransferamount;
	}
	public void setIntransferamount(String intransferamount) {
		this.intransferamount = intransferamount;
	}
	

}
