package uk.co.tech.jpm.stock.service;

import java.math.BigDecimal;

import uk.co.tech.jpm.exceptions.StockNotFoundException;
import uk.co.tech.jpm.model.Trade;

/**
 * Generic 
 * @author KrishnaPrasad
 *
 */
public interface SimpleStockService extends SuperSimpleStockService{
	/**
	 * Given the stock symbol and market price calculates the dividend yield.
	 * @param stockSymbol
	 * @param marketPrice
	 * @return
	 * @throws StockNotFoundException
	 */
	BigDecimal calculateDividendYield(String stockSymbol,BigDecimal marketPrice);
	/**
	 * Given the stock symbol and market price calculates the PbyE ratio.
	 * @param stockSymbol
	 * @param marketPrice
	 * @return
	 * @throws StockNotFoundException
	 */
	BigDecimal calculatePbyERatio(String stockSymbol,BigDecimal marketPrice);
	
	/**
	 * Calculates the volume weighted stock price based on the trades
	 * for given the stock symbol and number of minutes.
	 *  
	 * @param stockSymbol
	 * @param minutes
	 * @return
	 * @throws StockNotFoundException
	 */
	BigDecimal calculateVolumeWeightedStockPrice(String stockSymbol,int minutes);	
	
	/**
	 * Records the trade.
	 * @param trade
	 */
	boolean recordTrade(Trade trade);		
}
