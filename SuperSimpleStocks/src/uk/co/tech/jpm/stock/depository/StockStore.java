package uk.co.tech.jpm.stock.depository;

import java.util.Map;

import uk.co.tech.jpm.exceptions.StockExistsException;
import uk.co.tech.jpm.exceptions.StockNotFoundException;
import uk.co.tech.jpm.model.Stock;
/**
 * In memory representation of stock store and operations allowed on it. 
 * All the stocks will be maintained in a HashMap, key being the stock symbol and value will be the
 * stock object.
 *  
 * @author KrishnaPrasad
 *
 */
public interface StockStore {
	/**
	 * Adds a new stock to the stock store.
	 * @param stock Stock
	 * @throws StockExistsException - if a stock exists with the same stock symbol
	 */
	void addStock(Stock stock) throws StockExistsException;
	
	/**
	 * Removes the stock identified by the stock symbol from the stock store.
	 * @param stockSymbol
	 * @throws StockNotFoundException - if a stock does not exists with the given stock symbol
	 */
	void removeStock(String stockSymbol) throws StockNotFoundException;
	
	/**
	 * Returns Stock from the stock store for the given stock symbol.
	 * @param stockSymbol
	 * @return
	 * @throws StockNotFoundException - if a stock does not exists with the given stock symbol
	 */
	Stock retrieveStock(String stockSymbol) throws StockNotFoundException;	
	
	/**
	 * Returns all stocks in memory.
	 * @return
	 */
	Map<String,Stock> getStocks();
}
