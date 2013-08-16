package home.kwyho.google.ss.finance;

import home.kwyho.google.ss.finance.catNLP.CategoryNormalizer;
import home.kwyho.google.ss.finance.dataobj.ClassObj;
import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.dublincore.Date;
import com.google.gdata.data.extensions.City;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class NormalizedWorksheetSpendingDataWrangler extends
		WorksheetSpendingDataWrangler {
	protected CategoryNormalizer normalizer;

	public NormalizedWorksheetSpendingDataWrangler(SpreadsheetService service) {
		super(service);
		try {
			normalizer = new CategoryNormalizer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void importCategories(List<String> unstemmedCategories) {
		normalizer.importAllCategories(unstemmedCategories);
	}
	
	public void importAllCategoriesFromData(String username, String password) {
		try {
			SpreadsheetSSSpending ssSpend = new SpreadsheetSSSpending(username, password);
			List<String> unstemmedCategories = new ArrayList<String>();
			for (String month: SpreadsheetSSSpending.MONTH_NAMES) {
				WorksheetEntry worksheet = ssSpend.getWorksheet(month);
				if (worksheet != null) {
					List<SSFinanceDataEntry> entries = super.getWorksheetSpendingData(worksheet);
					List<ClassObj> classTypes = SpendingAnalyzer.getClassifiedSpendings(entries, WorksheetSpendingDataWrangler.COLUMN_CATEGORY);
					for (ClassObj categoryType: classTypes) {
						unstemmedCategories.add(categoryType.getClassType());
					}
				}
			}
			importCategories(unstemmedCategories);
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
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
					category = normalizer.normalize(cell.getCell().getValue());
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

}
