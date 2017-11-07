package telesync.gui;

public class UserSecurityException extends Exception {
	public UserSecurityException(String msg) {
		super(msg);
	}
	public String toString() {
		return "UserSecurityException: " + super.getMessage();
	}
}

	