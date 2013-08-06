package home.kwyho.google.ss.finance;

import home.kwyho.google.ss.finance.dataobj.ClassObj;
import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.dublincore.Date;
import com.google.gdata.data.extensions.City;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class WorksheetSpendingDataWrangler {
	protected SpreadsheetService service;
	public final static int COLUMN_DATE = 2;
	public final static int COLUMN_PLACE = 3;
	public final static int COLUMN_CATEGORY = 4;
	public final static int COLUMN_CITY = 5;
	public final static int COLUMN_DEBIT = 6;
	public final static int COLUMN_COMMENT = 7;
	public final static int COLUMN_INDIVIDUAL = 8;
	public final static int COLUMN_PAYMENTMETHOD = 9;

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
			
			Double debit = 0.0;
			Date date = new Date(DateTime.now().toString());
			String place = "";
			String category = "";
			City city = new City("Germantown, MD");
			String comment = "";
			String individual = "Stephen";
			String paymentMethod = "Cash";
			boolean nullEntry = true;
			
			List<CellEntry> cells = feed.getEntries();
			for (CellEntry cell: cells) {
				switch(cell.getCell().getCol()) {
				case COLUMN_DATE:
					date = new Date(cell.getCell().getValue());
					break;
				case COLUMN_PLACE:
					place = cell.getCell().getValue();
					break;
				case COLUMN_CATEGORY:
					category = cell.getCell().getValue();
					break;
				case COLUMN_CITY:
					city = new City(cell.getCell().getValue());
					break;
				case COLUMN_COMMENT:
					comment = cell.getCell().getValue();
					break;
				case COLUMN_DEBIT:
					Double tempVal = cell.getCell().getDoubleValue();
					if (!tempVal.equals(Double.NaN)) {
						debit = tempVal;
						nullEntry = false;
					}
					break;
				case COLUMN_INDIVIDUAL:
					individual = cell.getCell().getValue();
					break;
				case COLUMN_PAYMENTMETHOD:
					paymentMethod = cell.getCell().getValue();
					break;
				default:
					System.out.println("Out of range: R"+cell.getCell().getRow()+"C"+cell.getCell().getCol());
				}
			}
			
			if (!nullEntry) {
				SSFinanceDataEntry entry = new SSFinanceDataEntry(date, place, category, city, debit, comment, individual, paymentMethod);
				entries.add(entry);	
			}
		}
		
		return entries;
	}
	
	private HashMap<String, ClassObj> initializeClassTypes(List<SSFinanceDataEntry> entries, int column) {
		HashMap<String, ClassObj> classTypeHash = new HashMap<String, ClassObj>();
		
		for (SSFinanceDataEntry entry: entries) {
			String entryName = null;
			switch(column) {
			case COLUMN_PLACE:
				entryName = entry.getPlace();
				break;
			case COLUMN_CATEGORY:
				entryName = entry.getCategory();
				break;
			case COLUMN_INDIVIDUAL:
				entryName = entry.getIndividual();
				break;
			case COLUMN_PAYMENTMETHOD:
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
	
	public List<ClassObj> getClassifiedSpendings(List<SSFinanceDataEntry> entries, int column) {
		HashMap<String, ClassObj> classTypeHash = initializeClassTypes(entries, column);
		
		for (SSFinanceDataEntry entry: entries) {
			String entryName = null;
			switch(column) {
			case COLUMN_PLACE:
				entryName = entry.getPlace();
				break;
			case COLUMN_CATEGORY:
				entryName = entry.getCategory();
				break;
			case COLUMN_INDIVIDUAL:
				entryName = entry.getIndividual();
				break;
			case COLUMN_PAYMENTMETHOD:
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
	
	public List<ClassObj> getCategorizedSpendings(List<SSFinanceDataEntry> entries) {
		return getClassifiedSpendings(entries, COLUMN_CATEGORY);
	}
	
	@Deprecated
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
