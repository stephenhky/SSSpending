package home.kwyho.google.ss.finance.console;

import home.kwyho.google.ss.finance.NormalizedWorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.SpendingAnalyzer;
import home.kwyho.google.ss.finance.SpreadsheetSSSpending;
import home.kwyho.google.ss.finance.WorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.dataobj.ClassObj;
import home.kwyho.google.ss.finance.dataobj.ClassObjComparator;
import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;
import home.kwyho.google.ss.finance.misc.CalendarMonths;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class SSAnnualSpendingSummary {

	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws IOException 
	 * @throws AuthenticationException 
	 */
	public static void main(String[] args) throws AuthenticationException, IOException, ServiceException {
		Console console = System.console();
		System.out.print("GMail address = ? ");
		String username = console.readLine();
		System.out.print("Password = ? ");
		String password = new String(console.readPassword());
		
		System.out.println("Initializing connections...");
		SpreadsheetSSSpending ssSpend = new SpreadsheetSSSpending(username, password);
		SpreadsheetEntry spreadsheet = ssSpend.retrieveSSSpendingSpreadsheet();
		System.out.println(spreadsheet.getTitle().getPlainText());
		
		System.out.println("Handling category language...");
		NormalizedWorksheetSpendingDataWrangler worksheetWrangler = new NormalizedWorksheetSpendingDataWrangler(ssSpend.getService());
		worksheetWrangler.importAllCategoriesFromData(username, password);

		System.out.println("Analyzing data...");
		Map<String, List<ClassObj>> monthlySpendingTables = new HashMap<String, List<ClassObj>>();
		for (String month: CalendarMonths.MONTH_NAMES) {
			WorksheetEntry worksheet = ssSpend.getWorksheet(month);
			if (worksheet != null) {
				List<SSFinanceDataEntry> entries = worksheetWrangler.getWorksheetSpendingData(worksheet);
				List<ClassObj> classTypes = SpendingAnalyzer.getClassifiedSpendings(entries, WorksheetSpendingDataWrangler.COLUMN_CATEGORY);
				monthlySpendingTables.put(month, classTypes);
			}
		}
		
		System.out.println("Calculating sum...");
		Map<String, ClassObj> annualSpendingMap = new HashMap<String, ClassObj>();
		for (List<ClassObj> classTypes: monthlySpendingTables.values()) {
			for (ClassObj classType: classTypes) {
				String category = classType.getClassType();
				if (annualSpendingMap.containsKey(category)) {
					annualSpendingMap.get(category).setTotalAmount(annualSpendingMap.get(category).getTotalAmount()+classType.getTotalAmount());
				} else {
					annualSpendingMap.put(category, new ClassObj(category, classType.getTotalAmount()));
				}
			}
		}
		
		List<ClassObj> annualSpendings = new ArrayList<ClassObj>(annualSpendingMap.values());
		Collections.sort(annualSpendings, Collections.reverseOrder(new ClassObjComparator()));
		
		
		for (ClassObj classType: annualSpendings) {
			System.out.println(classType);
		}
		
		System.out.println("Updating summary table...");
		worksheetWrangler.writeAnnualSummary(ssSpend.getSummaryWorksheet(), monthlySpendingTables, annualSpendings);
	}

}
