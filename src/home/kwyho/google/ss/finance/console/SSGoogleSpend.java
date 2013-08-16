package home.kwyho.google.ss.finance.console;

import home.kwyho.google.ss.finance.FastNormalizedWorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.NormalizedWorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.SpendingAnalyzer;
import home.kwyho.google.ss.finance.SpreadsheetSSSpending;
import home.kwyho.google.ss.finance.WorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.dataobj.ClassObj;
import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;

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
	 */
	public static void main(String[] args) throws AuthenticationException, IOException, ServiceException {
		if (args.length == 0) {
			System.out.println("Argument: <month> [<month> ...])");
			System.exit(1);
		}
		
		Console console = System.console();
		System.out.print("GMail address = ? ");
		String username = console.readLine();
		System.out.print("Password = ? ");
		String password = new String(console.readPassword());
		
		SpreadsheetSSSpending ssSpend = new SpreadsheetSSSpending(username, password);
		SpreadsheetEntry spreadsheet = ssSpend.retrieveSSSpendingSpreadsheet();
		System.out.println(spreadsheet.getTitle().getPlainText());
		
		NormalizedWorksheetSpendingDataWrangler worksheetWrangler = new FastNormalizedWorksheetSpendingDataWrangler(ssSpend.getService());
		worksheetWrangler.importAllCategoriesFromData(username, password);
		
		for (String month: args) {
			WorksheetEntry worksheet = ssSpend.getWorksheet(month);
			if (worksheet != null) {
				System.out.println(month+" : "+worksheet.getTitle().getPlainText());
				List<SSFinanceDataEntry> entries = worksheetWrangler.getWorksheetSpendingData(worksheet);
				List<ClassObj> classTypes = SpendingAnalyzer.getClassifiedSpendings(entries, WorksheetSpendingDataWrangler.COLUMN_CATEGORY);
				for (ClassObj classType: classTypes) {
					System.out.println(classType);
				}
			}
		}
	}

}
