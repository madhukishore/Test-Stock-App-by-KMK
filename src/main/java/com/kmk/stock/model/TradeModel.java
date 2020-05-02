package com.kmk.stock.model;

import java.time.LocalDateTime;

public class TradeModel {

	private final TradeEnum type;
	private final Integer quantity;
	private final Double price;
	private final LocalDateTime localDateTime;
	private final StockModel stockModel;
	
	public TradeModel(final TradeEnum type, final Integer quantity, final Double price, final LocalDateTime localDateTime, final StockModel stockModel) {
		super();
		this.type = type;
		this.quantity = quantity;
		this.price = price;
		this.localDateTime = localDateTime;
		this.stockModel = stockModel;
	}
	public TradeEnum getType() {
		return type;
	}
	
	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}
	
	public Integer getQuantity() {
		return quantity;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public StockModel getStock() {
		return stockModel;
	}
		
}