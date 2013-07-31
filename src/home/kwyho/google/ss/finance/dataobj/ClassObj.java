package home.kwyho.google.ss.finance.dataobj;

public class ClassObj {
	private String classType;
	private Double totalAmount;
	
	public ClassObj() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClassObj(String classType, Double totalAmount) {
		super();
		this.classType = classType;
		this.totalAmount = totalAmount;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString() {
		return "ClassObj [classType=" + classType + ", totalAmount="
				+ totalAmount + "]";
	}
}
