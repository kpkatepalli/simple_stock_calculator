package uk.co.tech.jpm.stock.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.co.tech.jpm.exceptions.RecordTradeException;
import uk.co.tech.jpm.exceptions.StockNotFoundException;
import uk.co.tech.jpm.exceptions.StockServiceException;
import uk.co.tech.jpm.model.Stock;
import uk.co.tech.jpm.model.Trade;
import uk.co.tech.jpm.stock.depository.StockStore;
import uk.co.tech.jpm.stock.util.StatUtil;
/**
 * 
 * @author KrishnaPrasad
 *
 */
public class SimpleStockServiceImpl implements SimpleStockService{
	private static Logger logger = Logger.getLogger(SimpleStockServiceImpl.class);
	
	private StockStore stockStore;	
	private List<Trade> trades;
	public SimpleStockServiceImpl(StockStore stockStore) {
		this.stockStore = stockStore;
		trades = new ArrayList<Trade>();
	}
	
	/*
	 * (non-Javadoc)
	 * @see uk.co.tech.jpm.stock.service.SimpleStockService#calculateDividendYield(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public BigDecimal calculateDividendYield(String stockSymbol,BigDecimal marketPrice){
		BigDecimal dividendYield = BigDecimal.ZERO;	
		if(marketPrice == null || marketPrice.compareTo(BigDecimal.ZERO) <= 0){
			logger.error("Market price can not be zero or less than zero.");
			return dividendYield;
		}
		Stock stock = stockStore.retrieveStock(stockSymbol);
		try{			
			if(stock != null){
				stock.setMarketPrice(marketPrice);
				if(stock.getStockType() == Stock.StockType.COMMON){
					dividendYield = stock.getLastDividend().divide(marketPrice,2, RoundingMode.HALF_UP);
				}else if(stock.getStockType() == Stock.StockType.PREFERRED){
					dividendYield = (stock.getFixedDividend().multiply(stock.getParValue()))
							.divide(marketPrice,2, RoundingMode.HALF_UP);
				}
			}	
		}catch(Exception e){
				logger.error(e,e);
				throw new StockServiceException(e.getMessage());
		}
		return dividendYield.setScale(2, RoundingMode.HALF_UP);
	}
	/*
	 * (non-Javadoc)
	 * @see uk.co.tech.jpm.stock.service.SimpleStockService#calculatePbyERatio(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	public BigDecimal calculatePbyERatio(String stockSymbol,BigDecimal marketPrice){
		BigDecimal pByERatio = BigDecimal.ZERO;	
		if(marketPrice == null || marketPrice.compareTo(BigDecimal.ZERO) <= 0){
			logger.error("Marketprice can not be zero or less than zero.");
			return pByERatio;
		}
		Stock stock = stockStore.retrieveStock(stockSymbol);
		try{			
			if(stock != null){
				stock.setMarketPrice(marketPrice);	
				if(stock.getLastDividend().compareTo(BigDecimal.ZERO) > 0){
					pByERatio = marketPrice.divide(stock.getLastDividend(),2, RoundingMode.HALF_UP);
				}else{
					throw new Exception("Last dividend value unknown or zero.");
				}
			}		
		}catch(Exception e){
			logger.error(e,e);
			throw new StockServiceException(e.getMessage());
		}
		return pByERatio.setScale(2, RoundingMode.HALF_UP);
	}
	/*
	 * (non-Javadoc)
	 * @see uk.co.tech.jpm.stock.service.SimpleStockService#calculateVolumeWeightedStockPrice(java.lang.String)
	 */
	@Override
	public BigDecimal calculateVolumeWeightedStockPrice(String stockSymbol,int minutes){
		// TODO Auto-generated method stub
		BigDecimal price = BigDecimal.ZERO;
		try {
			List<Trade> trades = getEligibleTrades(stockSymbol, minutes);
			Long totalShares = 0L;
			BigDecimal totalTradePrice = BigDecimal.ZERO;
			for(Trade trade : trades){
				// Calculate the summation of Trade Price x Quantity
				totalTradePrice = totalTradePrice.add(trade.getTradePrice().multiply(new BigDecimal(trade.getQuantityOfShares())));
			
				totalShares += trade.getQuantityOfShares();
			}

			// calculate the stock price
			if(totalShares > 0){
				price = totalTradePrice.divide(new BigDecimal(totalShares),2,RoundingMode.HALF_UP);	
			}
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,e);
			throw new StockServiceException(e.getMessage());
		}
		return price.setScale(2, RoundingMode.HALF_UP);
	}
	

	/**
	 * Returns the trades which are matching the given stock and were
	 * created with in past minutes.
	 * @param stockSymbol
	 * @param minutes
	 * @return
	 */
	private List<Trade> getEligibleTrades(String stockSymbol, int minutes){
		List<Trade> eligibleTrades = new ArrayList<Trade>();
		try{
			Calendar dateRange =  Calendar.getInstance();
			dateRange.add(Calendar.MINUTE, -minutes);		
			for(Trade trade : trades){
				boolean include = trade.getStockSymbol().equals(stockSymbol);
				if(include && dateRange != null){					
					include = trade.getTimestamp().after(dateRange.getTime());
				}
				if(include){
					eligibleTrades.add(trade);
				}
			}
		}catch(Exception e){
			logger.error(e,e);
			throw new StockServiceException(e.getMessage());
		}
		return eligibleTrades;
	}
	
	/*
	 * (non-Javadoc)
	 * @see uk.co.tech.jpm.stock.service.SimpleStockService#recordTrade(uk.co.tech.jpm.model.Trade)
	 *  
	 * @param trade
	 */
	@Override
	public boolean recordTrade(Trade trade){	
		boolean isRecorded = false;
		if(trade==null){
			throw new RecordTradeException("Trade object to record should be a valid object and it's null.");
		}

		if(trade.getStockSymbol()==null || stockStore.retrieveStock(trade.getStockSymbol()) == null){
			throw new StockNotFoundException("Invalid Stock trade");
		}
		
		if(trade.getQuantityOfShares().intValue() <=0){
			throw new RecordTradeException("Quantity should be greater than zero.");
		}

		if(trade.getTradePrice().doubleValue() <=0.0){
			throw new RecordTradeException("Trade price should be greater than zero.");
		}
		if(trade.getTradeIndicator() == null){
			throw new RecordTradeException("Invalid trade indicator. Only B/S are allowed");
		}
		try{
			trade.setTimestamp(new Date());
			logger.debug(trade);
			trades.add(trade);	
			isRecorded = true;
		}catch(Exception e){
			throw new RecordTradeException("Trade not recorded");
		}
		return isRecorded;
	}
	

	/*
	 * (non-Javadoc)
	 * @see uk.co.tech.jpm.stock.service.SimpleStockService#calculateGBCEAllShareIndex()
	 * 
	 * @return
	 */
	@Override
	public double calculateGBCEAllShareIndex() {
		double geoMean = 0.0;
		try{
			Map<String,Stock> stocks = stockStore.getStocks();
			List<BigDecimal> stockPrices = new ArrayList<BigDecimal>();
			for(String stockSymbol : stocks.keySet()){
				Stock stock = stockStore.retrieveStock(stockSymbol);
				if(stock.getMarketPrice() != null && stock.getMarketPrice().compareTo(BigDecimal.ZERO) > 0){
					stockPrices.add(stock.getMarketPrice());
				}
			}	
			double[] stockPricesArray = new double[stockPrices.size()];
			for(int i=0;i<stockPrices.size();i++){
				stockPricesArray[i] = stockPrices.get(i).doubleValue();
			}
			 geoMean = StatUtil.geoMean(stockPricesArray);
		}catch(Exception e){
			logger.error(e,e);
			throw new StockServiceException(e.getMessage());
		}
		return geoMean;
	}
	

}
