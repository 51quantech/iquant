#!/usr/bin/python
# -*- coding: utf-8 -*-
import sys
import datetime
import time
import types
import urllib2
import json

import os
sys.path.append(os.path.split(os.path.realpath(__file__))[0] + "/../..")

import Account as Acc
import Util
import DataAPI

ISOTIMEFORMAT = '%Y-%m-%d %X'
freq = 'd'

def Commission(buycost=0, sellcost=0):
        hashmap = {}
        hashmap["buycost"] = buycost
        hashmap["sellcost"] = sellcost

        return hashmap

def set_universe(symbol, date="2016-01-01"):
        url = "http://10.13.85.179:8831/?key=cacheSock_Code&reqsh=" + symbol
        jsonData = Util.registerUrl(url)
        value = json.loads(jsonData)
        datavalue = value["data"]
        data = []

        for item in datavalue:
                data.append(item["stock_code"].encode("utf-8"))

        return data

REPLACE

def order(stock, num):
        now = account.now
        num = int(num) / 100 * 100
        
	if account.dayPrice[stock].has_key(now):
        	if num > 0:
                        hashmap = {}
                        hashmap["symbol"] = stock
                        hashmap["order_amount"] = num
                        hashmap["order_time"] = now + " 09:30"
                        hashmap["direction"] = 1
                        account.blotter.append(hashmap)
                elif num < 0:
                        num = num * -1
                        realNum = num if account.valid_secpos[stock] > num else account.valid_secpos[stock]
                        hashmap = {}
                        hashmap["symbol"] = stock
                        hashmap["order_amount"] = realNum
                        hashmap["order_time"] = now + " 09:30"
                        hashmap["direction"] = -1
                        account.blotter.append(hashmap)
                else:
                        pass
        else:
                pass

def order_to(stock, num):
        num = num - account.valid_secpos[stock]
        order(stock, num)

def order_pct(stock, pct):
        orderPrice = account.referencePortfolioValue * pct
        num = orderPrice / account.referencePrice[stock]
        order(stock, num)

def complete_trade(account):
        for trade in account.blotter:
                day = account.now
                stock = trade["symbol"]
                num = trade["order_amount"]
                price = account.dayPrice[stock][day]["open"]
                stockPrice = price * num

                if trade["direction"] == 1:
                	if account.cash < stockPrice:
                        	num = int(account.cash / price) / 100 * 100
                                stockPrice = price * num
                        hashmap = account.orderRecord[day] if account.orderRecord.has_key(day) else {}
                        hashmap[stock] = num
                        account.orderRecord[day] = hashmap

                        account.cash -= stockPrice + account.buycost
                        account.valid_secpos[stock] += num

                elif trade["direction"] == -1:
                        hashmap = account.orderRecord[day] if account.orderRecord.has_key(day) else {}
                        hashmap[stock] = num * -1
                        account.orderRecord[day] = hashmap
                        account.cash += stockPrice - account.sellcost
                        account.valid_secpos[stock] -= num


        account.referencePortfolioValue = 0
        day = account.now
        for (stock,num) in account.valid_secpos.items():
        	if account.dayPrice[stock].has_key(day):
                        account.referencePortfolioValue += num * account.dayPrice[stock][day]["open"]
                else:
                        account.referencePortfolioValue += num * account.referencePrice[stock]
        account.referencePortfolioValue += account.cash

if __name__ == "__main__":
        if not "commission" in locals().keys():
                commission = {"buycost":0 ,"sellcost":0}
        account = Acc.Account(universe,capital_base,commission,start,end,freq)

        initialize(account)

        benchmarkList = Util.getBenchmarkHistory(start, end, benchmark)
        dateList = Util.getDateList(start, end)

        i = 0
        j = 0
        accountReturnList = []
        accountWorthList = []
        accountOrderMap = []
        while i < len(dateList):
                date = dateList[i]
                account.now = date
                account.current_date = datetime.datetime.strptime(account.now, "%Y-%m-%d").date()
                account.blotter = []
                account.orderRecord[date] = {}
		
		if i % 20 == 0:
                        lastNum = len(dateList) - 1 if i + 20 >= len(dateList) else i + 19
                        account.dayPrice = Util.getHistory(dateList[i], dateList[lastNum], account.universe)

		dateMap = {}
                if i % refresh_rate == 0:
                	for stock in account.universe:
                               if i % 20 == 0:
                                        account.referencePrice[stock] = Util.getLastEndprice(date, stock)
                               else :
                                        yesterday = dateList[i - 1]
                                        if account.dayPrice[stock].has_key(yesterday):
                                               	account.referencePrice[stock] = account.dayPrice[stock][yesterday]["close"]
                                                break

                        handle_data(account)
                        complete_trade(account)

                worth = account.referencePortfolioValue
                hashmap = {}
                for stock in account.universe:
                        if account.orderRecord.has_key(date):
                                if account.orderRecord[date].has_key(stock):
                                        stockPrice = account.dayPrice[stock][date]["open"]
                                        orderPrice = account.orderRecord[date][stock] * stockPrice
                                        hashmap[stock] = str(account.orderRecord[date][stock])  + "," + str(stockPrice) + "," + str(orderPrice)
                                else:
                                        pass
                        else:
                                pass

                if hashmap:
                       	dateMap[date] = {}
                        dateMap[date]["9:30"] = hashmap
                        accountOrderMap.append(dateMap)
                accountWorthList.append(date + ":" + str(worth) + "," + str(account.cash) + "," + str(worth - account.cash))
                accountReturnList.append(date + ":" + str((worth / capital_base - 1) * 100))

                if (i+1) % 20 == 0 and i != 0:
			nowtime = time.strftime(ISOTIMEFORMAT, time.localtime())
			account.log.append("========= " + str(nowtime) + " the " + str((i+1)/20) + " time print =========")
			result = {}
                        result["account"] = accountReturnList
                        result["benchmark"] = benchmarkList[0 : i]
                        result["worth"] = accountWorthList
                        result["order"] = accountOrderMap
                        result["start"] = start
                        result["end"] = end
                        result["log"] = account.log

                        print "[C0001]:" + str(result)
                        j += 1

                i += 1

	nowtime = time.strftime(ISOTIMEFORMAT, time.localtime())
	account.log.append("========= " + str(nowtime) + " the last time print =========")
        result = {}
        result["account"] = accountReturnList
        result["benchmark"] = benchmarkList
        result["worth"] = accountWorthList
        result["order"] = accountOrderMap
        result["start"] = start
        result["end"] = end
        result["log"] = account.log

        print "[C0001]:" + str(result)

        time.sleep(1) 
