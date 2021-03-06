package home.kwyho.google.ss.finance;

import home.kwyho.google.ss.finance.authenticate.GoogleSpreadsheetAuthentication;
import home.kwyho.google.ss.finance.hashid.GoogleSpendingSpreadsheetIDHash;
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
//	private static String SS_SPEND_ID_2013 = "t-cP5RjsrrdhW6qxupaT_xg";
	private SpreadsheetEntry ssSpendingSpreadsheet;
	private HashMap<String, WorksheetEntry> hashWorksheets;
	private WorksheetEntry summaryWorksheet;
	//public static String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	private String year;
	
	public SpreadsheetSSSpending(FeedURLFactory factory, SpreadsheetFeed feed,
			SpreadsheetService service, String year) {
		super();
		this.factory = factory;
		this.spreadsheetFeed = feed;
		this.service = service;
		this.year = year;
		ssSpendingSpreadsheet = retrieveSSSpendingSpreadsheet();
		computeHashmap();
	}

	public SpreadsheetSSSpending(SpreadsheetService service, String year) throws IOException, ServiceException {
		super();
		this.service = service;
		this.year = year;
		factory = FeedURLFactory.getDefault();
		spreadsheetFeed = this.service.getFeed(factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
		ssSpendingSpreadsheet = retrieveSSSpendingSpreadsheet();
		computeHashmap();
	}
	
	public SpreadsheetSSSpending(String username, String password, String year) throws AuthenticationException, IOException, ServiceException {
		this(GoogleSpreadsheetAuthentication.login(username, password), year);
	}
	
	public SpreadsheetService getService() {
		return service;
	}

	public SpreadsheetEntry retrieveSSSpendingSpreadsheet() {
		List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
		for (SpreadsheetEntry spreadsheet: spreadsheets) {
			if (spreadsheet.getId().indexOf(GoogleSpendingSpreadsheetIDHash.YearToKeyHashMap.get(year)) != -1) {
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
				} else if (sheetName.equals("Summary")) {
					summaryWorksheet = worksheet;
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

	public WorksheetEntry getSummaryWorksheet() {
		return summaryWorksheet;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
}
