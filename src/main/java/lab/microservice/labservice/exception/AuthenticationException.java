package lab.microservice.labservice.exception;

public class AuthenticationException extends Exception {
	
	private static final long serialVersionUID = -1849949962894177069L;

	public AuthenticationException(String description) {
		super(description, null);
	}
}