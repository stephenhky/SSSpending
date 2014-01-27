package home.kwyho.google.ss.finance.console;

import home.kwyho.google.ss.finance.NormalizedWorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.SpendingAnalyzer;
import home.kwyho.google.ss.finance.SpreadsheetSSSpending;
import home.kwyho.google.ss.finance.WorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.dataobj.ClassObj;
import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;
import home.kwyho.google.ss.finance.exceptions.WrongYearException;
import home.kwyho.google.ss.finance.hashid.GoogleSpendingSpreadsheetIDHash;
import home.kwyho.google.ss.finance.misc.DoubleRounder;

import java.io.Console;
import java.io.IOException;
import java.util.List;

import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class SSGoogleSpend {

	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws IOException 
	 * @throws AuthenticationException 
	 * @throws WrongYearException 
	 */
	public static void main(String[] args) throws AuthenticationException, IOException, ServiceException, WrongYearException {
		if (args.length == 0) {
			System.out.println("Argument: <month> [<month> ...])");
			System.exit(1);
		}
		
		Console console = System.console();
		System.out.print("GMail address = ? ");
		String username = console.readLine();
		System.out.print("Password = ? ");
		String password = new String(console.readPassword());
		System.out.print("Year = ? ");
		String year = console.readLine();
		if (!GoogleSpendingSpreadsheetIDHash.YearToKeyHashMap.containsKey(year)) {
			throw new WrongYearException(year);
		}
		
		System.out.println("Initializing connections...");
		SpreadsheetSSSpending ssSpend = new SpreadsheetSSSpending(username, password, year);
		SpreadsheetEntry spreadsheet = ssSpend.retrieveSSSpendingSpreadsheet();
		System.out.println(spreadsheet.getTitle().getPlainText());
		
		System.out.println("Handling category language...");
		NormalizedWorksheetSpendingDataWrangler worksheetWrangler = new NormalizedWorksheetSpendingDataWrangler(ssSpend.getService());
		worksheetWrangler.importAllCategoriesFromData(username, password, year);
		
		for (String month: args) {
			System.out.println("+++ Month: "+month);
			double sum = 0.0;
			WorksheetEntry worksheet = ssSpend.getWorksheet(month);
			if (worksheet != null) {
				List<SSFinanceDataEntry> entries = worksheetWrangler.getWorksheetSpendingData(worksheet);
				List<ClassObj> classTypes = SpendingAnalyzer.getClassifiedSpendings(entries, WorksheetSpendingDataWrangler.COLUMN_CATEGORY);
				for (ClassObj classType: classTypes) {
					System.out.println(classType.getClassType()+" : "+DoubleRounder.round(classType.getTotalAmount(), 2));
					sum += classType.getTotalAmount();
				}
			}
			System.out.println("Total = "+DoubleRounder.round(sum, 2));
		}
	}

}
