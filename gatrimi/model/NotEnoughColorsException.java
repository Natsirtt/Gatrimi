package gatrimi.model;

public class NotEnoughColorsException extends Exception {
	public NotEnoughColorsException() {
		super();
	}
	
	public NotEnoughColorsException(String message) {
		super(message);
	}
}
