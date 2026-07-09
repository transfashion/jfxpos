package jfxpos.models;

import jfxpos.Model;
import java.time.LocalDateTime;

public class Salesperson extends Model {
	public static final class Contract {
		public static final String TABLE_NAME = "SALESPERSON";

		public static final class Columns {
			public static final String ID = "SALESPERSON_ID";
			public static final String NIK = "SALESPERSON_NIK";
			public static final String NAME = "SALESPERSON_NAME";
			public static final String ISDISABLED = "SALESPERSON_ISDISABLED";
			public static final String BRAND_ID = "BRAND_ID";
			public static final String SITE_ID = "SITE_ID";
			public static final String DATATIMESTAMP = "DATATIMESTAMP";
			public static final String MD5HASH = "MD5HASH";
		}
	}

	private int salespersonId;
	private String salespersonNik;
	private String salespersonName;
	private boolean salespersonIsDisabled;
	private Integer brandId;
	private Integer siteId;
	private LocalDateTime dataTimestamp;
	private String md5Hash;

	public Salesperson() {
	}

	public int getSalespersonId() {
		return salespersonId;
	}

	public void setSalespersonId(int salespersonId) {
		this.salespersonId = salespersonId;
	}

	public String getSalespersonNik() {
		return salespersonNik;
	}

	public void setSalespersonNik(String salespersonNik) {
		this.salespersonNik = salespersonNik;
	}

	public String getSalespersonName() {
		return salespersonName;
	}

	public void setSalespersonName(String salespersonName) {
		this.salespersonName = salespersonName;
	}

	public boolean isSalespersonIsDisabled() {
		return salespersonIsDisabled;
	}

	public void setSalespersonIsDisabled(boolean salespersonIsDisabled) {
		this.salespersonIsDisabled = salespersonIsDisabled;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public LocalDateTime getDataTimestamp() {
		return dataTimestamp;
	}

	public void setDataTimestamp(LocalDateTime dataTimestamp) {
		this.dataTimestamp = dataTimestamp;
	}

	public String getMd5Hash() {
		return md5Hash;
	}

	public void setMd5Hash(String md5Hash) {
		this.md5Hash = md5Hash;
	}
}
