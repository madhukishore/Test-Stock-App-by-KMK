package com.kmk.stock.service;

import com.kmk.stock.model.TradeModel;

public interface TradeSer {

	/**
	 * this method evaluate volume weighted average stock price 
	 * after fetching trades in last specified mins
	 */
	public double calculateVolumeWeightedStockPriceInTimeRange();
	

	/**
	 * @param tradeModel
	 * this method add a stock trade in the memory
	 */
	public void addStockTrade(TradeModel tradeModel);
}