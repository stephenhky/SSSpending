package home.kwyho.google.ss.finance;

import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.dublincore.Date;
import com.google.gdata.data.extensions.City;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class WorksheetSpendingDataWrangler {
	private SpreadsheetService service;
	private static int COLUMN_DATE = 2;
	private static int COLUMN_PLACE = 3;
	private static int COLUMN_CATEGORY = 4;
	private static int COLUMN_CITY = 5;
	private static int COLUMN_DEBIT = 6;
	private static int COLUMN_COMMENT = 7;
	private static int COLUMN_INDIVIDUAL = 8;
	private static int COLUMN_PAYMENTMETHOD = 9;

	public SpreadsheetService getService() {
		return service;
	}

	public void setService(SpreadsheetService service) {
		this.service = service;
	}

	public WorksheetSpendingDataWrangler(SpreadsheetService service) {
		super();
		this.service = service;
	}

	public List<SSFinanceDataEntry> getWorksheetSpendingData(WorksheetEntry worksheet) throws IOException, ServiceException {
		URL cellFeedUrl = worksheet.getCellFeedUrl();
		//CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
		int rowCount = worksheet.getRowCount();
		
		List<SSFinanceDataEntry> entries = new ArrayList<SSFinanceDataEntry>();
		for (int idx=3; idx<=rowCount; idx++) {
			CellQuery query = new CellQuery(cellFeedUrl);
			query.setMinimumRow(idx);
			query.setMaximumRow(idx);
			query.setMinimumCol(COLUMN_DATE);
			query.setMaximumCol(COLUMN_PAYMENTMETHOD);
			CellFeed feed = service.query(query, CellFeed.class);
			
			List<CellEntry> cells = feed.getEntries();
			Double debit = cells.get(COLUMN_DEBIT-2).getCell().getDoubleValue();
			Date date = new Date(cells.get(COLUMN_DATE-2).getCell().getValue());
			String place = cells.get(COLUMN_PLACE-2).getCell().getValue();
			String category = cells.get(COLUMN_CATEGORY-2).getCell().getValue();
			City city = new City(cells.get(COLUMN_CITY-2).getCell().getValue());
			String comment = cells.get(COLUMN_COMMENT-2).getCell().getValue();
			String individual = cells.get(COLUMN_INDIVIDUAL-2).getCell().getValue();
			String paymentMethod = cells.get(COLUMN_PAYMENTMETHOD-2).getCell().getValue();
			
			SSFinanceDataEntry entry = new SSFinanceDataEntry(date, place, category, city, debit, comment, individual, paymentMethod);
			entries.add(entry);
		}
		
		return entries;
	}
	
	public List<SSFinanceDataEntry> getWorksheetSpendingDataSlow(WorksheetEntry worksheet) throws IOException, ServiceException {
		URL cellFeedUrl = worksheet.getCellFeedUrl();
		//CellFeed cellFeed = service.getFeed(cellFeedUrl, CellFeed.class);
		int rowCount = worksheet.getRowCount();
		
		List<SSFinanceDataEntry> entries = new ArrayList<SSFinanceDataEntry>();
		for (int idx=3; idx<=rowCount; idx++) {
			String batchId = "R"+idx+"C"+COLUMN_DEBIT;
			URL cellEntryURL = new URL(cellFeedUrl.toString()+"/"+batchId);
			CellEntry cellEntry = service.getEntry(cellEntryURL, CellEntry.class);
			Double debit = cellEntry.getCell().getDoubleValue();
			if (debit.equals(Double.NaN)) {
				break;
			} else {
				batchId = "R"+idx+"C"+COLUMN_DATE;
				cellEntryURL = new URL(cellFeedUrl.toString()+"/"+batchId);
				cellEntry = service.getEntry(cellEntryURL, CellEntry.class);
				Date date = new Date(cellEntry.getCell().getValue());
				
				batchId = "R"+idx+"C"+COLUMN_PLACE;
				cellEntryURL = new URL(cellFeedUrl.toString()+"/"+batchId);
				cellEntry = service.getEntry(cellEntryURL, CellEntry.class);
				String place = cellEntry.getCell().getValue();
				
				batchId = "R"+idx+"C"+COLUMN_CATEGORY;
				cellEntryURL = new URL(cellFeedUrl.toString()+"/"+batchId);
				cellEntry = service.getEntry(cellEntryURL, CellEntry.class);
				String category = cellEntry.getCell().getValue();
				
				batchId = "R"+idx+"C"+COLUMN_CITY;
				cellEntryURL = new URL(cellFeedUrl.toString()+"/"+batchId);
				cellEntry = service.getEntry(cellEntryURL, CellEntry.class);
				City city = new City(cellEntry.getCell().getValue());
				
				batchId = "R"+idx+"C"+COLUMN_COMMENT;
				cellEntryURL = new URL(cellFeedUrl.toString()+"/"+batchId);
				cellEntry = service.getEntry(cellEntryURL, CellEntry.class);
				String comment = cellEntry.getCell().getValue();
				
				batchId = "R"+idx+"C"+COLUMN_INDIVIDUAL;
				cellEntryURL = new URL(cellFeedUrl.toString()+"/"+batchId);
				cellEntry = service.getEntry(cellEntryURL, CellEntry.class);
				String individual = cellEntry.getCell().getValue();
				
				batchId = "R"+idx+"C"+COLUMN_PAYMENTMETHOD;
				cellEntryURL = new URL(cellFeedUrl.toString()+"/"+batchId);
				cellEntry = service.getEntry(cellEntryURL, CellEntry.class);
				String paymentMethod = cellEntry.getCell().getValue();
				
				SSFinanceDataEntry entry = new SSFinanceDataEntry(date, place, category, city, debit, comment, individual, paymentMethod);
				entries.add(entry);
			}
		}
		
		return entries;
	}
}
