import sys 
import Util
import datetime
import time
import numpy as np
 
class Account:
	weight = []
        weightmap = {}
        referencePortfolioValue = 0 
        referencePrice = {} 
        valid_secpos = {}

        now = ""
	minute = ""
	current_date = None
	cash = 0
	buycost = 0
	sellcost = 0
	previous_date = ""
        orderRecord = {}
        buyAdjust = []
	log = []
	dayPrice = {}
        minutePrice = {}
        blotter = []

        def __init__(self, universe, capital_base, commission, start, end, freq):
                self.universe = universe
                self.capital_base = capital_base * 1.0
		self.referencePortfolioValue = capital_base * 1.0
		self.buycost = commission["buycost"]
		self.sellcost = commission["sellcost"]
		self.cash = capital_base * 1.0
                self.start = start
                self.end = end
		self.freq = freq
                for stock in universe:
                	self.valid_secpos[stock] = 0

	def get_history(self, time_range):
		result = {}
		stockmap = Util.getPreDayHistoryList(time_range, self.now, self.universe)

		for (stock, item1) in stockmap.items():
			map1 = {}

			end_price = []
			start_price = []
			updown = []
			trade_day = []
			osci_ratio = []
	                max_price = []
        	        min_price = []
                	trade_volume = []
                	trade_amount = []
                	turnover_ratio = []

			for item2 in item1:
				start_price.append(item2["start_price"])
				end_price.append(item2["end_price"])
				updown.append(item2["update_ratio"])
				trade_day.append(item2["trade_day"])
				osci_ratio.append(item2["osci_ratio"])
	                        max_price.append(item2["max_price"])
        	                min_price.append(item2["min_price"])
                	        trade_volume.append(item2["trade_volume"])
                        	trade_amount.append(item2["trade_amount"])
                        	turnover_ratio.append(item2["turnover_ratio"])

			map1["start_price"] = start_price
			map1["end_price"] = end_price
			map1["updown"] = updown
			map1["trade_day"] = trade_day
			map1["osci_ratio"] = osci_ratio
	                map1["max_price"] = max_price
        	        map1["min_price"] = min_price
                	map1["trade_volume"] = trade_volume
                	map1["trade_amount"] = trade_amount
                	map1["turnover_ratio"] = turnover_ratio
			result[stock] = map1

		return result

	def get_symbol_history(self, symbol, time_range):
       		result = {}
                stockmap = Util.getPreDayStockHistoryList(time_range, self.now, symbol)

		end_price = []
                start_price = []
                updown = []
                trade_day = []
		osci_ratio = []
		max_price = []
		min_price = []
		trade_volume = []
		trade_amount = []
		turnover_ratio = []
		for item1 in stockmap:
                        start_price.append(item1["start_price"])
                        end_price.append(item1["end_price"])
                        updown.append(item1["update_ratio"])
                        trade_day.append(item1["trade_day"])
			osci_ratio.append(item1["osci_ratio"])
			max_price.append(item1["max_price"])
			min_price.append(item1["min_price"])
			trade_volume.append(item1["trade_volume"])
			trade_amount.append(item1["trade_amount"])
			turnover_ratio.append(item1["turnover_ratio"])

                result["start_price"] = start_price
                result["end_price"] = end_price
                result["updown"] = updown
                result["trade_day"] = trade_day
		result["osci_ratio"] = osci_ratio
		result["max_price"] = max_price
		result["min_price"] = min_price
		result["trade_volume"] = trade_volume
		result["trade_amount"] = trade_amount
		result["turnover_ratio"] = turnover_ratio

       		return result

        def get_attribute_history(self, attribute, time_range):
		result = {}
                stockmap = Util.getPreDayHistoryList(time_range, self.now, self.universe)

                for (stock, item1) in stockmap.items():
                        hashmap = []

                        for item2 in item1:
                                hashmap.append(item2[attribute])

			if not hashmap:
				for i in range(0, time_range):
					hashmap.append(np.float16(0))
                        result[stock] = hashmap

                return result

if __name__ == '__main__':
	start = '2015-01-01'
	end = '2015-04-01'
	benchmark = '399300'  
	capital_base = 100000                   
	universe = ['000001', '600000']
	commission = {"buycost":0, "sellcost":0}
	freq = "d"

	account = Account(universe,capital_base,commission,start,end,freq);
	account.now = "2015-02-01"
	print account.get_history(3)
