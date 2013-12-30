package home.kwyho.google.ss.finance;

import home.kwyho.google.ss.finance.dataobj.ClassObj;
import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;
import home.kwyho.google.ss.finance.misc.CalendarMonths;
import home.kwyho.google.ss.finance.misc.DoubleRounder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.dublincore.Date;
import com.google.gdata.data.extensions.City;
import com.google.gdata.data.spreadsheet.Cell;
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
		int rowCount = worksheet.getRowCount();

		CellQuery query = new CellQuery(cellFeedUrl);
		query.setMinimumRow(2);
		query.setMaximumRow(rowCount);
		query.setMinimumCol(COLUMN_DATE);
		query.setMaximumCol(COLUMN_PAYMENTMETHOD);
		CellFeed feed = service.query(query, CellFeed.class);
		List<CellEntry> cellEntries = feed.getEntries();
		
		List<SSFinanceDataEntry> entries = new ArrayList<SSFinanceDataEntry>();
		for (int idx=3; idx<=rowCount; idx++) {
			entries.add(new SSFinanceDataEntry());
		}
		
		for (CellEntry cellEntry: cellEntries) {
			Cell cell = cellEntry.getCell();
			int dataIdx = cell.getRow()-2;
			SSFinanceDataEntry dataEntry = entries.get(dataIdx);
			
			switch (cell.getCol()) {
			case COLUMN_DATE:
				dataEntry.setDate(new Date(cell.getValue()));
				break;
			case COLUMN_PLACE:
				dataEntry.setPlace(cell.getValue());
				break;
			case COLUMN_CATEGORY:
				dataEntry.setCategory(cell.getValue());
				break;
			case COLUMN_CITY:
				dataEntry.setCity(new City(cell.getValue()));
				break;
			case COLUMN_COMMENT:
				dataEntry.setComment(cell.getValue());
				break;
			case COLUMN_DEBIT:
				Double tempVal = cell.getDoubleValue();
				if (!tempVal.equals(Double.NaN)) {
					dataEntry.setDebit(tempVal);
				}
				break;
			case COLUMN_INDIVIDUAL:
				dataEntry.setIndividual(cell.getValue());
				break;
			case COLUMN_PAYMENTMETHOD:
				dataEntry.setIndividual(cell.getValue());
				break;
			default:
				System.out.println("Out of range: R"+cell.getRow()+"C"+cell.getCol());
			}
		}

		int ptr = 0;
		while (ptr < entries.size()) {
			if (entries.get(ptr).isEmpty()) {
				entries.remove(ptr);
			} else {
				ptr++;
			}
		}
		
		return entries;
	}
	
	public void writeAnnualSummary(WorksheetEntry summaryWorksheet, Map<String, List<ClassObj>> monthlySpendingTables, List<ClassObj> annualSpendings) throws IOException, ServiceException {
		URL cellFeedUrl = summaryWorksheet.getCellFeedUrl();
		
		for (int idx=0; idx<annualSpendings.size(); idx++) {
			ClassObj categoryObj = annualSpendings.get(idx);
			CellQuery query = new CellQuery(cellFeedUrl);
			query.setMinimumCol(2);
			query.setMaximumCol(15);
			query.setMinimumRow(idx+3);
			query.setMaximumRow(idx+3);
			CellFeed feed = service.query(query, CellFeed.class);
			
			System.out.println("Category: "+categoryObj.getClassType()+"\t"+feed.getEntries().size());
			
			for (CellEntry cell: feed.getEntries()) {
				int colIdx = cell.getCell().getCol();
				System.out.println(colIdx);
				switch(colIdx) {
				case 2:
					cell.changeInputValueLocal(categoryObj.getClassType());
					cell.update();
					System.out.println("Writing...");
					break;
				case 15:
					cell.changeInputValueLocal(DoubleRounder.round(categoryObj.getTotalAmount(), 2).toString());
					cell.update();
					System.out.println("Writing...");
					break;
				default:
					int monthIdx = colIdx-3;
					String monthName = CalendarMonths.MONTH_NAMES[monthIdx];
					List<ClassObj> classTypes = monthlySpendingTables.get(monthName);
					Double amount = 0.00;
					for (ClassObj classType: classTypes) {
						if (classType.getClassType().equals(categoryObj.getClassType())) {
							amount = classType.getTotalAmount();
							break;
						}
					}
					cell.changeInputValueLocal(DoubleRounder.round(amount, 2).toString());
					cell.update();
				}
				
			}
		}

	}
	
	@Deprecated
	public List<SSFinanceDataEntry> getWorksheetSpendingDataSlow(WorksheetEntry worksheet) throws IOException, ServiceException {
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
	

	
	@Deprecated
	public List<SSFinanceDataEntry> getWorksheetSpendingDataSlow1(WorksheetEntry worksheet) throws IOException, ServiceException {
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
