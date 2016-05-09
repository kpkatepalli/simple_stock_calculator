package uk.co.tech.jpm.stock.depository;

import java.util.HashMap;
import java.util.Map;

import uk.co.tech.jpm.exceptions.StockExistsException;
import uk.co.tech.jpm.exceptions.StockNotFoundException;
import uk.co.tech.jpm.model.Stock;

/**
 * This {@code StockStoreImpl} provides default implementation of {@code StockStore} interface.
 *  
 * @author KrishnaPrasad
 *
 */
public abstract class StockStoreImpl implements StockStore{

	private Map<String,Stock> stocks = new HashMap<String,Stock>();
	
	public Map<String, Stock> getStocks() {
		return stocks;
	}

	public void addStock(Stock stock) throws StockExistsException{
		if(!stocks.containsKey(stock.getStockSymbol())){
			stocks.put(stock.getStockSymbol(),stock);
		}else{
			throw new StockExistsException("Stock already exists for the symbol " + stock.getStockSymbol());
		}
	}
	
	public void removeStock(String stockSymbol) throws StockNotFoundException{
		if(stocks.containsKey(stockSymbol)){
			stocks.remove(stockSymbol);
		}else{
			throw new StockNotFoundException("Stock not found for the symbol " + stockSymbol);
		}
	}
	
	public Stock retrieveStock(String stockSymbol) throws StockNotFoundException{
		if(stocks.containsKey(stockSymbol)){
			return stocks.get(stockSymbol);
		}else{
			throw new StockNotFoundException("Stock not found for the symbol " + stockSymbol);
		}
	}
}
