package home.kwyho.google.ss.finance;

import home.kwyho.google.ss.finance.authenticate.GoogleSpreadsheetAuthentication;
import home.kwyho.google.ss.finance.misc.CalendarMonths;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class SpreadsheetSSSpending {
	private FeedURLFactory factory;
	private SpreadsheetFeed spreadsheetFeed;
	private SpreadsheetService service;
	private static String SS_SPEND_ID = "t-cP5RjsrrdhW6qxupaT_xg";
	private SpreadsheetEntry ssSpendingSpreadsheet;
	private HashMap<String, WorksheetEntry> hashWorksheets;
	//public static String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	
	public SpreadsheetSSSpending(FeedURLFactory factory, SpreadsheetFeed feed,
			SpreadsheetService service) {
		super();
		this.factory = factory;
		this.spreadsheetFeed = feed;
		this.service = service;
		ssSpendingSpreadsheet = retrieveSSSpendingSpreadsheet();
		computeHashmap();
	}

	public SpreadsheetSSSpending(SpreadsheetService service) throws IOException, ServiceException {
		super();
		this.service = service;
		factory = FeedURLFactory.getDefault();
		spreadsheetFeed = this.service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
		ssSpendingSpreadsheet = retrieveSSSpendingSpreadsheet();
		computeHashmap();
	}
	
	public SpreadsheetSSSpending(String username, String password) throws AuthenticationException, IOException, ServiceException {
		this(GoogleSpreadsheetAuthentication.login(username, password));
	}
	
	public SpreadsheetService getService() {
		return service;
	}

	public SpreadsheetEntry retrieveSSSpendingSpreadsheet() {
		List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
		for (SpreadsheetEntry spreadsheet: spreadsheets) {
			if (spreadsheet.getId().indexOf(SS_SPEND_ID) != -1) {
				return spreadsheet;
			}
		}
		return null;
	}
	
	public SpreadsheetEntry getSSSpendingSpreadsheet() {
		return ssSpendingSpreadsheet;
	}
	
	private void computeHashmap() {
		hashWorksheets = new HashMap<String, WorksheetEntry>();
		List<WorksheetEntry> worksheets;
		try {
			worksheets = ssSpendingSpreadsheet.getWorksheets();
			for (WorksheetEntry worksheet: worksheets) {
				String sheetName = worksheet.getTitle().getPlainText();
				if (Arrays.asList(CalendarMonths.MONTH_NAMES).contains(sheetName)) {
					hashWorksheets.put(sheetName, worksheet);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public WorksheetEntry getWorksheet(String month) {
		return hashWorksheets.get(month);
	}
}
