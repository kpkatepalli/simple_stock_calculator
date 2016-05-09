package uk.co.tech.jpm.stock.service;

/**
 * 
 * @author KrishnaPrasad
 *
 */
public interface SuperSimpleStockService {
	/**
	 * Method to calculate the GBCE All Share Index using the 
	 * geometric mean of prices for all stocks.
	 */
	double calculateGBCEAllShareIndex();
}
