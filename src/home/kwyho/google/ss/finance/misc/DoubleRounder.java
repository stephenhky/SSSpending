package home.kwyho.google.ss.finance.misc;

public class DoubleRounder {
	public static Double round(Double num, Integer decimalPlaces) {
		Double f = Math.pow(10, decimalPlaces);
		return Math.round(num*f)/f;
	}
}
