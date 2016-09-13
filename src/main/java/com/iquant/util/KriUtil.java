package com.iquant.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KriUtil {
	private static double treasuryBillRate = 0.04;
	private static double riskFreeInterestRate = 0.0035;

	// 年化收益率
	public double annualizedReturnRate(double pStart, double pEnd, double day) {
		double pow = 250 / day;
		return Math.pow(pEnd / pStart, pow) - 1;
	}

	public double returnRate(double pStart, double pEnd) {
		return pStart / pEnd - 1;
	}

	// 阿尔法
	public double alpha(List<Double> strategyWorth,
			List<Double> benchmarkWorth, List<Double> strategyGain,
			List<Double> benchmarkGain) {
		double annualizedReturn = annualizedReturnRate(strategyWorth.get(0),
				strategyWorth.get(strategyWorth.size() - 1),
				strategyWorth.size());
		double benchmarkReturn = annualizedReturnRate(benchmarkWorth.get(0),
				benchmarkWorth.get(benchmarkWorth.size() - 1),
				benchmarkWorth.size());
		double beta = beta(strategyGain, benchmarkGain);

		return annualizedReturn - treasuryBillRate - beta
				* (benchmarkReturn - treasuryBillRate);
	}

	// 阿尔法
	public double alpha(double beta, double strategyReturnRate,
			double benchmarkReturnRate) {
		return strategyReturnRate - treasuryBillRate - beta
				* (benchmarkReturnRate - treasuryBillRate);
	}

	// 贝塔
	public double beta(List<Double> strategyGain, List<Double> benchmarkGain) {
		double covariance = covariance(strategyGain, benchmarkGain);
		double variance = variance(benchmarkGain);

		return covariance / variance;
	}

	// 夏普比率
	public double sharpeRatio(List<Double> strategyGain,
			List<Double> strategyWorth) {
		double volatility = volatility(strategyGain);
		double strategyReturnRate = annualizedReturnRate(strategyWorth.get(0),
				strategyWorth.get(strategyWorth.size() - 1),
				strategyWorth.size());
		return (strategyReturnRate - riskFreeInterestRate) / volatility;
	}

	public double sharpeRatio(double volatility, double strategyReturnRate) {
		return (strategyReturnRate - riskFreeInterestRate) / volatility;
	}

	// 年化收益波动率
	public double annualizedVolatility(List<Double> strategyGain) {
		double average = average(strategyGain);
		double temp = 0.0;
		for (double item : strategyGain)
			temp += Math.pow(item - average, 2);

		double i = (strategyGain.size() - 1) == 0 ? 0 : (double) 250
				/ (double) (strategyGain.size() - 1);
		return Math.sqrt(i * temp);
	}

	// 收益波动率
	public double volatility(List<Double> strategyGain) {
		double average = average(strategyGain);
		double temp = 0.0;
		for (double item : strategyGain)
			temp += Math.pow(item - average, 2);

		return Math.sqrt(temp);
	}

	// 年化信息比率
	public double annualizedInformationRatio(List<Double> strategyWorth,
			List<Double> benchmarkWorth, List<Double> strategyGain,
			List<Double> benchmarkGain) {
		List<Double> difference = new ArrayList<Double>();
		
		int len = strategyGain.size() > benchmarkGain.size() ? benchmarkGain.size() : strategyGain.size();
		for (int i = 0; i < len; i++)
			difference.add(strategyGain.get(i) - benchmarkGain.get(i));

		double pow = (double) 250 / (double) strategyGain.size();
		double variance = Math.sqrt(variance(difference) * pow);
		double strategyReturnRate = annualizedReturnRate(strategyWorth.get(0),
				strategyWorth.get(strategyWorth.size() - 1),
				strategyWorth.size());
		double benchmarkReturnRate = annualizedReturnRate(
				benchmarkWorth.get(0),
				benchmarkWorth.get(benchmarkWorth.size() - 1),
				benchmarkWorth.size());

		return (strategyReturnRate - benchmarkReturnRate) / variance;
	}

	public double annualizedInformationRatio(double strategyReturnRate,
			double benchmarkReturnRate, List<Double> strategyGain,
			List<Double> benchmarkGain) {
		List<Double> difference = new ArrayList<Double>();
		
		int len = strategyGain.size() > benchmarkGain.size() ? benchmarkGain.size() : strategyGain.size();
		for (int i = 0; i < len; i++)
			difference.add(strategyGain.get(i) - benchmarkGain.get(i));
		double pow = (double) 250 / (double) strategyGain.size();
		double variance = Math.sqrt(variance(difference) * pow);

		return (strategyReturnRate - benchmarkReturnRate) / variance;
	}

	// 信息比率
	public double informationRatio(double strategyReturnRate,
			double benchmarkReturnRate, List<Double> strategyGain,
			List<Double> benchmarkGain) {
		List<Double> difference = new ArrayList<Double>();
		
		int len = strategyGain.size() > benchmarkGain.size() ? benchmarkGain.size() : strategyGain.size();
		for (int i = 0; i < len; i++)
			difference.add(strategyGain.get(i) - benchmarkGain.get(i));
		double variance = Math.sqrt(variance(difference));

		return (strategyReturnRate - benchmarkReturnRate) / variance;
	}

	// 最大回撤
	public double maxDrawdown(List<Double> asset) {
		Collections.sort(asset);
		return 1 - (asset.get(0) / asset.get(asset.size() - 1));
	}

	// 换手率
	public double turnoverRate(double buy, double sell,
			List<Double> accountAsset) {
		double min = Math.min(buy, sell * -1);
		return min / average(accountAsset);
	}

	// 计算方差
	public double variance(List<Double> array) {
		double average = average(array);
		double temp = 0.0;

		for (double item : array)
			temp += Math.pow(item - average, 2);

		return temp / (double) array.size();
	}

	// 计算协方差
	public double covariance(List<Double> array1, List<Double> array2) {
		double average1 = average(array1);
		double average2 = average(array2);

		List<Double> xyArray = new ArrayList<Double>();
		int len = array1.size() > array2.size() ? array2.size() : array1.size();
		for (int i = 0; i < len; i++)
			xyArray.add(array1.get(i) * array2.get(i));
		double averagexy = average(xyArray);

		return averagexy - average1 * average2;
	}

	// 求和
	public double sum(List<Double> array) {
		double sum = 0;
		for (double item : array)
			sum += item;

		return sum;
	}

	// 计算平均值
	public double average(List<Double> array) {
		double sum = 0;
		for (double item : array)
			sum += item;

		return sum / (double) array.size();
	}

}
