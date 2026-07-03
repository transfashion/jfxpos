package jfxpos.models;

import jfxpos.Model;
import java.time.LocalDate;

public class Customer extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "CUSTOMER";

		public static final class Columns {
			public static final String ID = "CUSTOMER_ID";
			public static final String NAME = "CUSTOMER_NAME";
			public static final String CUSTOMERTYPE_ID = "CUSTOMERTYPE_ID";
			public static final String CUSTOMERTYPE_NAME = "CUSTOMERTYPE_NAME";
			public static final String GENDER = "CUSTOMER_GENDER";
			public static final String BIRTHDATE = "CUSTOMER_BIRTHDATE";
		}
	}

	private int customerId;
	private String customerName;
	private int customerTypeId;
	private String customerTypeName;
	private int customerGender;
	private LocalDate customerBirthdate;

	public Customer() {
	}

	public Customer(int customerId, String customerName, int customerTypeId, String customerTypeName, int customerGender, LocalDate customerBirthdate) {
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerTypeId = customerTypeId;
		this.customerTypeName = customerTypeName;
		this.customerGender = customerGender;
		this.customerBirthdate = customerBirthdate;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(int customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	public String getCustomerTypeName() {
		return customerTypeName;
	}

	public void setCustomerTypeName(String customerTypeName) {
		this.customerTypeName = customerTypeName;
	}

	public int getCustomerGender() {
		return customerGender;
	}

	public void setCustomerGender(int customerGender) {
		this.customerGender = customerGender;
	}

	public LocalDate getCustomerBirthdate() {
		return customerBirthdate;
	}

	public void setCustomerBirthdate(LocalDate customerBirthdate) {
		this.customerBirthdate = customerBirthdate;
	}
}
