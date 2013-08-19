package home.kwyho.google.ss.finance.dataobj;

import java.util.Comparator;

public class ClassObjComparator implements Comparator<ClassObj> {

	@Override
	public int compare(ClassObj arg0, ClassObj arg1) {
		return Double.compare(arg0.getTotalAmount(), arg1.getTotalAmount());
	}

}
