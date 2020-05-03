package com.kmk.stock.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.annotations.VisibleForTesting;
import com.kmk.stock.dao.TradeDao;
import com.kmk.stock.exception.InvalidValException;
import com.kmk.stock.model.TradeModel;
import com.kmk.stock.service.TradeSer;

@Service
public class TradeSerImpl implements TradeSer {

	private int stockTradesFetchRangeInMins = 15;
	private final TradeDao tradeDao;
	
	@Autowired
    public TradeSerImpl(final TradeDao tradeDao) {
		this.tradeDao = tradeDao;
    }
	
	private static Logger LOGGER = LoggerFactory.getLogger(StockSerImpl.class);
	
	/**
	 * Evaluate volume weighted average stock price of trades happened between now and last 15 minutes.
	 */
	@Override
	public double calculateVolumeWeightedStockPriceInTimeRange() {
		
		List<TradeModel> tradesInLastSpecifiedMins = this.getTradesBetweenDuration(LocalDateTime.now(), stockTradesFetchRangeInMins);
		
		LOGGER.info("Calculating VolumeWeightedStockPrice of trades:{}  InLastSpecifiedMins: {} ", tradesInLastSpecifiedMins, stockTradesFetchRangeInMins);;
		
		int totalQuantity = tradesInLastSpecifiedMins.stream().mapToInt(TradeModel::getQuantity).sum();
		
		double volumeWeigthedStockPrice = tradesInLastSpecifiedMins.stream()
					.mapToDouble( trade -> trade.getQuantity() * trade.getPrice()).sum();
			
		return volumeWeigthedStockPrice / totalQuantity;
	}
	
	@Override
	public void addStockTrade(TradeModel tradeModel) {
		validateStockTrade(tradeModel);
		LOGGER.info("Adding a Stock Trade {} ", tradeModel);
		new TradeDao().addTrade(tradeModel);
	}
	
	public List<TradeModel> getAllTrades() {
		LOGGER.info("Fetching all trades recorded");
		return tradeDao.getAllTrades();
	}
	
	/**
	 * Returns list of trades after filtering between now dateTime and time specified in mins
	 */
	@VisibleForTesting
	List<TradeModel> getTradesBetweenDuration(LocalDateTime dateTime, long durationInMinutes) {
		
		LocalDateTime initialDateTime = dateTime.minusMinutes(durationInMinutes);
		return getAllTrades().stream().filter(trade -> trade.getLocalDateTime()
				.isAfter(initialDateTime) && trade.getLocalDateTime().equals(dateTime))
				.collect(Collectors.toList());
	}
	
	
	/**
	 * To validate a stock trade
	 */
	private void validateStockTrade(TradeModel tradeModel) {
		LOGGER.info("Validating a stock trade details");
		if(tradeModel.getStock() == null) {
			throw new InvalidValException("A valid stock is required for trading");
			
		} 
		if(tradeModel.getQuantity() < 1 ) {
			throw new InvalidValException("A valid quantity of stock is required for trading");
		}
	}
	
}