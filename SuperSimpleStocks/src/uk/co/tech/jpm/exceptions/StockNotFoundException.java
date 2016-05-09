package uk.co.tech.jpm.exceptions;

public class StockNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2343533235424261882L;

	public StockNotFoundException(String message){
		super(message);
	}
}
