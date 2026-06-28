package jfxpos.models;

public final class UserContract {
	public static final String TABLE_NAME = "USERS";

	public static final class Columns {
		public static final String ID = "USER_ID";
		public static final String USERNAME = "USERNAME";
		public static final String PASSWORD = "PASSWORD";
		public static final String ROLE = "ROLE";
		public static final String IS_ACTIVE = "IS_ACTIVE";
		public static final String CREATED_AT = "CREATED_AT";
	}
}
