package uk.co.tech.jpm.exceptions;

public class StockServiceException extends RuntimeException{

	private static final long serialVersionUID = 7967940485956072490L;

	public StockServiceException(String message){
		super(message);
	}
}
