package home.kwyho.google.ss.finance.console;

import home.kwyho.google.ss.finance.FastNormalizedWorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.NormalizedWorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.SpendingAnalyzer;
import home.kwyho.google.ss.finance.SpreadsheetSSSpending;
import home.kwyho.google.ss.finance.WorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.dataobj.ClassObj;
import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;

import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class SSGoogleSpendAll {

	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws IOException 
	 * @throws AuthenticationException 
	 */
	public static void main(String[] args) throws AuthenticationException, IOException, ServiceException {
		String username = JOptionPane.showInputDialog("username = ?");
		String password = "";
		JPasswordField pf = new JPasswordField();
		int okCxl = JOptionPane.showConfirmDialog(null, pf, "Enter Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (okCxl == JOptionPane.OK_OPTION) {
		  password = new String(pf.getPassword());
		} else {
			System.exit(1);
		}
		SpreadsheetSSSpending ssSpend = new SpreadsheetSSSpending(username, password);
		SpreadsheetEntry spreadsheet = ssSpend.retrieveSSSpendingSpreadsheet();
		System.out.println(spreadsheet.getTitle().getPlainText());
		
		NormalizedWorksheetSpendingDataWrangler worksheetWrangler = new FastNormalizedWorksheetSpendingDataWrangler(ssSpend.getService());
		worksheetWrangler.importAllCategoriesFromData(username, password);
		
		for (String month: SpreadsheetSSSpending.MONTH_NAMES) {
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
