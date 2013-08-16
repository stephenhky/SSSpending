package home.kwyho.google.ss.finance.dataobj;

import com.google.gdata.data.dublincore.Date;
import com.google.gdata.data.extensions.City;

public class SSFinanceDataEntry {
	private Date date;
	private String place;
	private String category;
	private City city;
	private Double debit;
	private String comment;
	private String individual;
	private String paymentMethod;
	
	public SSFinanceDataEntry(Date date, String place, String category,
			City city, Double debit, String comment, String individual,
			String paymentMethod) {
		super();
		this.date = date;
		this.place = place;
		this.category = category;
		this.city = city;
		this.debit = debit;
		this.comment = comment;
		this.individual = individual;
		this.paymentMethod = paymentMethod;
	}

	public SSFinanceDataEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}
	
	public void setCity(String cityStr) {
		city = new City(cityStr);
	}

	public Double getDebit() {
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIndividual() {
		return individual;
	}

	public void setIndividual(String individual) {
		this.individual = individual;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	@Override
	public String toString() {
		return "SSFinanceDataEntry [date=" + date + ", place=" + place
				+ ", category=" + category + ", debit=" + debit + "]";
	}
	
	public boolean isEmpty() {
		return (debit==null);
	}
}
