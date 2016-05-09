package uk.co.tech.jpm.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * The {@code Trade} class represents Trade entity.
 * 
 * @author KrishnaPrasad
 *
 */
public class Trade {
	
	public enum TradeType {
		BUY,SELL
	}

	private Date timestamp;
	private String stockSymbol;
	private Long quantityOfShares;
	private TradeType tradeIndicator;
	private BigDecimal tradePrice;
	
	public Trade(){
		
	}
	public Trade(Date timestamp, String stockSymbol, Long quantityOfShares, TradeType tradeIndicator,
			BigDecimal tradePrice) {
		super();
		this.timestamp = timestamp;
		this.stockSymbol = stockSymbol;
		this.quantityOfShares = quantityOfShares;
		this.tradeIndicator = tradeIndicator;
		this.tradePrice = tradePrice;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
		
	public String getStockSymbol() {
		return stockSymbol;
	}
	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	public Long getQuantityOfShares() {
		return quantityOfShares;
	}
	public void setQuantityOfShares(Long quantityOfShares) {
		this.quantityOfShares = quantityOfShares;
	}
	
	public TradeType getTradeIndicator() {
		return tradeIndicator;
	}
	public void setTradeIndicator(TradeType tradeIndicator) {
		this.tradeIndicator = tradeIndicator;
	}
	public BigDecimal getTradePrice() {
		return tradePrice;
	}
	public void setTradePrice(BigDecimal tradePrice) {
		this.tradePrice = tradePrice;
	}
	@Override
	public String toString() {
		return "Trade [timestamp=" + timestamp + ", stockSymbol=" + stockSymbol + ", quantityOfShares="
				+ quantityOfShares + ", tradeIndicator=" + tradeIndicator + ", tradePrice=" + tradePrice.setScale(2, RoundingMode.HALF_UP) + "]";
	}
	
}
