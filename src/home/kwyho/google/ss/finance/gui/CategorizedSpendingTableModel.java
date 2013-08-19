package home.kwyho.google.ss.finance.gui;

import home.kwyho.google.ss.finance.dataobj.ClassObj;
import home.kwyho.google.ss.finance.dataobj.ClassObjComparator;
import home.kwyho.google.ss.finance.misc.DoubleRounder;

import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public class CategorizedSpendingTableModel extends AbstractTableModel {
	private List<ClassObj> categorizedSpendingList;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8512128179667837244L;

	public void importCategorizedSpendingList(List<ClassObj> categorizedSpendingList) {
		this.categorizedSpendingList = categorizedSpendingList;
		Collections.sort(categorizedSpendingList, Collections.reverseOrder(new ClassObjComparator()));
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		if (categorizedSpendingList == null) {
			return 0;
		} else {
			return categorizedSpendingList.size();
		}
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		ClassObj categorizedSpending = categorizedSpendingList.get(arg0);
		if (arg1==0) {
			return categorizedSpending.getClassType();
		} else if (arg1==1) {
			return DoubleRounder.round(categorizedSpending.getTotalAmount(), 2);
		}
		return null;
	}

	@Override
    public String getColumnName(int column) {
		if (column==0) {
			return "Category";
		} else if (column==1) {
			return "Total Amount";
		}
        return null;
    }
	
}
