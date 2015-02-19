package home.kwyho.google.ss.finance.gui;

import home.kwyho.google.ss.finance.NormalizedWorksheetSpendingDataWrangler;
import home.kwyho.google.ss.finance.SpreadsheetSSSpending;
import home.kwyho.google.ss.finance.misc.CalendarMonths;

import java.io.IOException;

import javax.swing.JOptionPane;

import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class SSFinanceAnalyzer {

	/**
	 * @param args
	 * @throws ServiceException 
	 * @throws IOException 
	 * @throws AuthenticationException 
	 */
	public static void main(String[] args) throws AuthenticationException, IOException, ServiceException {
		// Getting authentication information...
		JAuthenticationPanel panel = new JAuthenticationPanel();
		int option = JOptionPane.showConfirmDialog(null, panel, "Authentication", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (!(option == JOptionPane.OK_OPTION)) {
			System.exit(1);
		}
		String username = panel.getGmailAddr();
		String password = panel.getPassword();
		String year = "2015";
		
		// Initializing connections....
		SpreadsheetSSSpending ssSpend = new SpreadsheetSSSpending(username, password, year);
		SpreadsheetEntry spreadsheet = ssSpend.retrieveSSSpendingSpreadsheet();
		System.out.println(spreadsheet.getTitle().getPlainText());
		
		// Handling category language...
		NormalizedWorksheetSpendingDataWrangler worksheetWrangler = new NormalizedWorksheetSpendingDataWrangler(ssSpend.getService());
		worksheetWrangler.importAllCategoriesFromData(username, password, year);
		
		// Setting up GUI
		JSSFinanceFrame jFrame = new JSSFinanceFrame();
		for (int monthIdx=0; monthIdx < CalendarMonths.MONTH_NAMES.length; monthIdx++) {
			String month = CalendarMonths.MONTH_NAMES[monthIdx];
			WorksheetEntry worksheet = ssSpend.getWorksheet(month);
			if (worksheet != null) {
				jFrame.setEntries(monthIdx, worksheetWrangler.getWorksheetSpendingData(worksheet));
			}
		}
		jFrame.setLocationRelativeTo(null);
		jFrame.setVisible(true);
	}

}
