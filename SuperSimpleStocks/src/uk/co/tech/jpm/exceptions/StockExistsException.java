package uk.co.tech.jpm.exceptions;

public class StockExistsException extends RuntimeException {

	private static final long serialVersionUID = 2343533235424261882L;

	public StockExistsException(String message){
		super(message);
	}
}
