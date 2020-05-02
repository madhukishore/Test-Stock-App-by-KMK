package com.kmk.stock.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.kmk.stock.model.TradeModel;
import com.kmk.stock.model.TradeEnum;


@Repository
public class TradeDao {

	
	private List<TradeModel> tradeModels;

	/**
	 * This method saves a trade in the memory using ArrayList
	 */
	
	public void addTrade(TradeModel tradeModel) {
		if(tradeModels == null) {
			tradeModels = new ArrayList<>();
		}
		tradeModels.add(tradeModel);
	}

	/**
	 * This method retrieve all trades in the memory using ArrayList
	 */
	
	public List<TradeModel> getAllTrades() {
		return tradeModels;
	}
	
	
	/**
	 * This method retrieve all trades from the memory for a particular stock
	 */
	
	public List<TradeModel> getTradesByStockSymbol(String stockSymbol) {
		return tradeModels.stream()
			   .filter((trade) -> trade.getStock().getStockSymbol().equals(stockSymbol))
			   .collect(Collectors.toList());
	}
	
	/**
	 * This method retrieve all trades from the memory of a particular type
	 */
	
	public List<TradeModel> getTradesByTradeType(TradeEnum tradeEnum) {
		return tradeModels.stream()
				.filter((trade) -> trade.getType().equals(tradeEnum))
				.collect(Collectors.toList());
	}
	
	public TradeModel getAnInvalidTrade() {
		return new TradeModel(TradeEnum.BUY, 0, -100.00, LocalDateTime.now(), null);
	}
	
}

