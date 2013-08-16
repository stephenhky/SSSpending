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
import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

public class FastNormalizedWorksheetSpendingDataWrangler extends
		NormalizedWorksheetSpendingDataWrangler {

	public FastNormalizedWorksheetSpendingDataWrangler(
			SpreadsheetService service) {
		super(service);
	}

	@Override
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
				String category = normalizer.normalize(cell.getValue());
				dataEntry.setCategory(category);
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
}
