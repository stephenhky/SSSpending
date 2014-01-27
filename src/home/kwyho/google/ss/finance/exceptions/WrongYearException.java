package home.kwyho.google.ss.finance.exceptions;

public class WrongYearException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5431315901078882864L;

	public WrongYearException(String year) {
		super("Wrong year "+year+"!!");
	}
}
