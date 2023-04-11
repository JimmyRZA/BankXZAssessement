package com.banking.system.controllers;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.banking.system.model.AuditEntry;
import com.banking.system.model.CurrentAccount;
import com.banking.system.model.CurrentAccountDetail;
import com.banking.system.model.Customer;
import com.banking.system.model.CustomerDetail;
import com.banking.system.model.SavingsAccount;
import com.banking.system.repos.AuditRepository;
import com.banking.system.repos.CurrentAccountRepository;
import com.banking.system.repos.CustomerRepository;
import com.banking.system.repos.SavingsAccountRepository;

/**
 * @author Jimmy Mhlanga
 * Disclaimer input validations assumed to be done on front-end side and back-end just processes
 *
 */
@Controller
@RequestMapping("/transactional")
public class TransactionalAccountBalanceController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionalAccountBalanceController.class);

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	SavingsAccountRepository savingsAccountRepo;

	@Autowired
	CurrentAccountRepository currentAccountRepo;

	@Autowired
	AuditRepository auditRepo;

	@GetMapping("/home")
	public String displayHomePage() {
		return "BankingHomePageLanding";
	}

	@GetMapping("/transfer")
	public String performTransfer(Model model) {
		model.addAttribute("currentAccountDetail", new CurrentAccountDetail());
		logger.debug("Pre-transfer");

		return "transfer";
	}

	@GetMapping("/debitorcredit")
	public String performDebitOrCredit(Model model) {
		logger.debug("Pre-Debit or credit");
		model.addAttribute("currentAccountDetail", new CurrentAccountDetail());

		return "do-debit-credit.html";
	}

	@PostMapping("/performDebitOrCredit")
	public String performCRDbt(CurrentAccountDetail currentAccountDetail, Model model) {
		logger.debug("Perform Debit or credit");

		if (currentAccountDetail.getAccountType().equalsIgnoreCase("Savings")) {
			logger.debug("Perform Debit or credit from Savings Account");
			Optional<SavingsAccount> savingsAccount = savingsAccountRepo
					.findById((currentAccountDetail.getCustomerID()));
			if (savingsAccount.isPresent()) {
				long savingsBal = (long) savingsAccount.get().getBalance();
				long credDbtAmount = new Long(currentAccountDetail.getIntransferamount());
				savingsBal = savingsBal + credDbtAmount;
				savingsAccount.get().setBalance(savingsBal);
				savingsAccountRepo.save(savingsAccount.get());
				Customer customer = createCustomerCurrecyDetail(currentAccountDetail);
				createAudit(customer, "Debit Or Credit performed on this account: 500600" + currentAccountDetail.getCustomerID()
				+ ", amount: " + currentAccountDetail.getIntransferamount());
			}

		} else if (currentAccountDetail.getAccountType().equalsIgnoreCase("Current")) {
			logger.debug("Perform Debit or credit from Current Account");
			Optional<CurrentAccount> currentAccount = currentAccountRepo
					.findById((currentAccountDetail.getCustomerID()));
			if (currentAccount.isPresent()) {
				long currBal = (long) currentAccount.get().getBalance();
				long credDbtAmount = new Long(currentAccountDetail.getIntransferamount());
				currBal = currBal + credDbtAmount;
				currentAccount.get().setBalance(currBal);
				currentAccountRepo.save(currentAccount.get());
				Customer customer = createCustomerCurrecyDetail(currentAccountDetail);
				createAudit(customer, "Debit Or Credit performed on this account: 700800" + currentAccountDetail.getCustomerID()
				+ ", amount: " + currentAccountDetail.getIntransferamount());
			}
		}
		model.addAttribute("currentAccountDetail", currentAccountDetail);
		logger.debug("Debit or credit complete");

		return "transfer-complete";
	}

	@PostMapping("/doTransfer")
	public String doTransfer(CurrentAccountDetail currentAccountDetail, Model model) {
		logger.debug("Pre Transfer");

		Optional<CurrentAccount> currentAccount = currentAccountRepo.findById((currentAccountDetail.getCustomerID()));

		if (currentAccount.isPresent()) {
			currentAccountDetail.setAccountNumber(currentAccount.get().getAccountNumber());
			currentAccountDetail.setBalance(currentAccount.get().getBalance());

		} // TODO throw an error if account not found

		Optional<SavingsAccount> savingsAccount = savingsAccountRepo.findById((currentAccountDetail.getCustomerID()));

		if (!savingsAccount.isPresent()) {
			// TODO Throw an error if savings account is not found
		}

		model.addAttribute("currentAccountDetail", currentAccountDetail);

		return "do-transfer";
	}

	@PostMapping("/performTransfer")
	public String performTransfer(CurrentAccountDetail currentAccountDetail, Model model) {
		logger.debug("Perform Transfer");
		boolean currentAccountChecksOut = false;
		boolean savingsAccountChecksOut = false;
		// Transfer Logic goes here
		Optional<CurrentAccount> currentAccount = currentAccountRepo.findById(currentAccountDetail.getCustomerID());

		// Check if current account to transfer from exists
		if (currentAccount.isPresent()) {
			currentAccountChecksOut = true;
		}

		// check target account and if it is savings account
		Optional<SavingsAccount> savingsAccount = savingsAccountRepo.findById(currentAccountDetail.getCustomerID());

		if (savingsAccount.isPresent()) {
			savingsAccountChecksOut = true;
		}

		if (currentAccountChecksOut && savingsAccountChecksOut) {
			CurrentAccount currAcc = currentAccount.get();
			if (currAcc.getBalance() > (Double.parseDouble(currentAccountDetail.getIntransferamount())
					+ Double.parseDouble(currentAccountDetail.getIntransferamount()) * 0.05)) {
				currAcc.setBalance(
						currAcc.getBalance() - (Double.parseDouble(currentAccountDetail.getIntransferamount())
								+ Double.parseDouble(currentAccountDetail.getIntransferamount()) * 0.05));
				currentAccountRepo.save(currAcc);
				Customer customer = createCustomerCurrecyDetail(currentAccountDetail);
				createAudit(customer, "Transfer performed on this account: " + currAcc.getAccountNumber()
						+ ", amount transferred " + currentAccountDetail.getIntransferamount());

				SavingsAccount savingsAcc = savingsAccount.get();
				savingsAcc.setBalance(
						savingsAcc.getBalance() + (Double.parseDouble(currentAccountDetail.getIntransferamount())
								+ Double.parseDouble(currentAccountDetail.getIntransferamount()) * 0.5));
				savingsAccountRepo.save(savingsAcc);
				createAudit(customer, "Payment made into this account: " + savingsAcc.getAccountNumber() + ","
						+ " amount paid " + currentAccountDetail.getIntransferamount());

			} else {
				// TODO throw error insufficient funds //
			}

		}
		logger.debug("Transfer complete");

		model.addAttribute("currentAccountDetail", currentAccountDetail);

		return "transfer-complete";
	}

	@GetMapping("/addCustomer")
	public String addCustomer(Model model) {
		model.addAttribute("customerDetail", new CustomerDetail());
		return "add-customer";
	}

	@PostMapping("/doAddCustomer")
	public String doAddCustomer(CustomerDetail customerDetail, Model model) {
		model.addAttribute("customerDetail", customerDetail);
		Customer customer = saveCustomer(customerDetail);

		// Audit
		createAudit(customer, "New Customer added with customerID ");

		// Add Savings Account
		addSavingsAccount(customer);

		// Audit
		createAudit(customer, "Savings Account for customer with customerID ");

		// Add Current Account
		addCurrentAccount(customer);

		// Audit
		createAudit(customer, "Current Account for customer with customerID ");

		return "customer-added";

	}

	private void addCurrentAccount(Customer customer) {
		logger.debug("Add Current Account for customer "+ customer.getCustomerID());
		CurrentAccount currentAccount = new CurrentAccount();
		currentAccount.setCustomerID(customer.getCustomerID());
		String accountNo = "700800" + customer.getCustomerID();
		currentAccount.setAccountNumber(Long.parseLong(accountNo));
		currentAccount.setBalance(0);
		currentAccount.setCustomerName(customer.getName());
		currentAccount.setCustomerSurname(customer.getSurname());
		currentAccount.setTransferAmount(0);
		currentAccount.setAccountAge(0);
		currentAccountRepo.save(currentAccount);
	}

	private void addSavingsAccount(Customer customer) {
		logger.debug("Add Savings Account for customer "+ customer.getCustomerID());
		SavingsAccount savingsAccount = new SavingsAccount();
		savingsAccount.setCustomerID(customer.getCustomerID());
		String accountNo = "500600" + customer.getCustomerID();
		savingsAccount.setAccountNumber(Long.parseLong(accountNo));
		savingsAccount.setCustomerName(customer.getName());
		savingsAccount.setCustomerSurname(customer.getSurname());
		savingsAccountRepo.save(savingsAccount);
	}

	private Customer createCustomerCurrecyDetail(CurrentAccountDetail currentAccountDetail) {
		Customer customer = new Customer();
		customer.setName(currentAccountDetail.getCustomerName());
		customer.setSurname(currentAccountDetail.getCustomerSurname());
		customer.setIdNumber(Long.parseLong(currentAccountDetail.getCustomerID()));
		customer.setCustomerID(currentAccountDetail.getCustomerID());
		return customer;
	}

	private Customer saveCustomer(CustomerDetail customerDetail) {
		logger.debug("Add customer "+ customerDetail.getCustomerID());
		Customer customer = new Customer();
		int id = this.generateId();
		customer.setName(customerDetail.getName());
		customer.setSurname(customerDetail.getSurname());
		customer.setGender(customerDetail.getGender());
		customer.setCellphone(customerDetail.getCellphone());
		customer.setCustomerID(String.valueOf(((id > 0 ? id : id * (-1)))));
		customer.setAddress(customerDetail.getAddress());
		customer.setIdNumber(customerDetail.getIdNumber());
		customerDetail.setCustomerID(customer.getCustomerID());
		customerRepo.save(customer);
		return customer;
	}

	private void createAudit(Customer customer, String descrition) {
		AuditEntry auditEntry = new AuditEntry();
		auditEntry.setDate(LocalDate.now().toString());
		auditEntry.setCustomerId(customer.getCustomerID());
		auditEntry.setDescription(descrition + customer.getCustomerID());
		auditRepo.save(auditEntry);
		// Send Notificaton to Customer
	}

	private int generateId() {
		int randomId = 0;
		Random rand = new Random();
		for (int j = 0; j < 10; j++) {
			randomId = (int) rand.nextLong();
		}
		return randomId;
	}

}
