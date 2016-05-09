package uk.co.tech.jpm.exceptions;

public class RecordTradeException extends RuntimeException{

	private static final long serialVersionUID = -4417354766110020948L;

	public RecordTradeException(String message){
		super(message);
	}
}
