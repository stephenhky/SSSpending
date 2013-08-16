package home.kwyho.google.ss.finance;

import home.kwyho.google.ss.finance.dataobj.ClassObj;
import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpendingAnalyzer {
	private static HashMap<String, ClassObj> initializeClassTypes(List<SSFinanceDataEntry> entries, int column) {
		HashMap<String, ClassObj> classTypeHash = new HashMap<String, ClassObj>();
		
		for (SSFinanceDataEntry entry: entries) {
			String entryName = null;
			switch(column) {
			case WorksheetSpendingDataWrangler.COLUMN_PLACE:
				entryName = entry.getPlace();
				break;
			case WorksheetSpendingDataWrangler.COLUMN_CATEGORY:
				entryName = entry.getCategory();
				break;
			case WorksheetSpendingDataWrangler.COLUMN_INDIVIDUAL:
				entryName = entry.getIndividual();
				break;
			case WorksheetSpendingDataWrangler.COLUMN_PAYMENTMETHOD:
				entryName = entry.getPaymentMethod();
				break;
			default:
				return null;
			}
			if (!classTypeHash.containsKey(entryName)) {
				ClassObj obj = new ClassObj(entryName, 0.0);
				classTypeHash.put(entryName, obj);
			}
		}
		
		return classTypeHash;
	}
	
	public static List<ClassObj> getClassifiedSpendings(List<SSFinanceDataEntry> entries, int column) {
		HashMap<String, ClassObj> classTypeHash = initializeClassTypes(entries, column);
		
		for (SSFinanceDataEntry entry: entries) {
			String entryName = null;
			switch(column) {
			case WorksheetSpendingDataWrangler.COLUMN_PLACE:
				entryName = entry.getPlace();
				break;
			case WorksheetSpendingDataWrangler.COLUMN_CATEGORY:
				entryName = entry.getCategory();
				break;
			case WorksheetSpendingDataWrangler.COLUMN_INDIVIDUAL:
				entryName = entry.getIndividual();
				break;
			case WorksheetSpendingDataWrangler.COLUMN_PAYMENTMETHOD:
				entryName = entry.getPaymentMethod();
				break;
			default:
				return null;
			}
			Double total = classTypeHash.get(entryName).getTotalAmount() + entry.getDebit();
			classTypeHash.get(entryName).setTotalAmount(total);
		}
		
		List<ClassObj> classTypes = new ArrayList<ClassObj>(classTypeHash.values());
		
		return classTypes;
	}
	
	public static List<ClassObj> getCategorizedSpendings(List<SSFinanceDataEntry> entries) {
		return getClassifiedSpendings(entries, WorksheetSpendingDataWrangler.COLUMN_CATEGORY);
	}
}
