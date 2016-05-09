package uk.co.tech.jpm.exceptions;

public class StockStoreException extends RuntimeException{

	private static final long serialVersionUID = 7967940485956072490L;

	public StockStoreException(String message){
		super(message);
	}
}
