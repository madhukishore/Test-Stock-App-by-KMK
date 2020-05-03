package com.kmk.stock.service;

import com.kmk.stock.model.TradeModel;

public interface TradeSer {

	/**
	 * Evaluates volume weighted average stock price after fetching trades in last specified mins
	 */
	public double calculateVolumeWeightedStockPriceInTimeRange();
	

	/**
	 * Add a stock trade in the memory
	 */
	public void addStockTrade(TradeModel tradeModel);
}