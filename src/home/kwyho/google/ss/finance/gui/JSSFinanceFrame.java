package home.kwyho.google.ss.finance.gui;
import home.kwyho.google.ss.finance.SpendingAnalyzer;
import home.kwyho.google.ss.finance.SpreadsheetSSSpending;
import home.kwyho.google.ss.finance.dataobj.SSFinanceDataEntry;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class JSSFinanceFrame extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6019983120178259453L;
	private JCheckBox jJanCheckBox;
	private JCheckBox jFebCheckBox;
	private JFormattedTextField jTotalAmountField;
	private JLabel jLabel1;
	private JScrollPane jScrollPane1;
	private JButton jAnalyzeButton;
	private JTable jCategoryTable;
	private JTabbedPane jTabbedPane;
	private JCheckBox jMarCheckBox;
	private JCheckBox jSepCheckBox;
	private JCheckBox jDecCheckBox;
	private JCheckBox jNovCheckBox;
	private JCheckBox jOctCheckBox;
	private JCheckBox jAugCheckBox;
	private JCheckBox jJulCheckBox;
	private JCheckBox jJunCheckBox;
	private JCheckBox jMayCheckBox;
	private JCheckBox jAprCheckBox;
	private List<JCheckBox> monthCheckBoxes;
	private CategorizedSpendingTableModel jCategoryTableModel;
	private List<List<SSFinanceDataEntry>> monthEntries;
	//private ChartPanel jPieChartPanel;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JSSFinanceFrame inst = new JSSFinanceFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public JSSFinanceFrame() {
		super();
		monthEntries = new ArrayList<List<SSFinanceDataEntry>>();
		for (int monthIdx=0; monthIdx<SpreadsheetSSSpending.MONTH_NAMES.length; monthIdx++) {
			monthEntries.add(null);
		}
		//jPieChartPanel = new ChartPanel(null);
		initGUI();
	}
	
	public void setEntries(int monthIdx, List<SSFinanceDataEntry> entries) {
		monthEntries.set(monthIdx, entries);
	}
	
	public void setAnalyzedMonthData() {
		List<SSFinanceDataEntry> includedEntries = new ArrayList<SSFinanceDataEntry>();
		for (int monthIdx=0; monthIdx<SpreadsheetSSSpending.MONTH_NAMES.length; monthIdx++) {
			if (monthCheckBoxes.get(monthIdx).isSelected()) {
				List<SSFinanceDataEntry> entries = monthEntries.get(monthIdx);
				if (entries!=null) {
					includedEntries.addAll(entries);
				}
			}
		}
		jCategoryTableModel.importCategorizedSpendingList(SpendingAnalyzer.getCategorizedSpendings(includedEntries));
		jTotalAmountField.setValue(SpendingAnalyzer.getTotalSpending(includedEntries));
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			monthCheckBoxes = new ArrayList<JCheckBox>();
			{
				jJanCheckBox = new JCheckBox();
				getContentPane().add(jJanCheckBox, "Center");
				jJanCheckBox.setText("January");
				jJanCheckBox.setBounds(388, 30, 112, 19);
				monthCheckBoxes.add(jJanCheckBox);
			}
			{
				jFebCheckBox = new JCheckBox();
				getContentPane().add(jFebCheckBox);
				jFebCheckBox.setText("February");
				jFebCheckBox.setBounds(388, 50, 112, 22);
				monthCheckBoxes.add(jFebCheckBox);
			}
			{
				jMarCheckBox = new JCheckBox();
				getContentPane().add(jMarCheckBox);
				jMarCheckBox.setText("March");
				jMarCheckBox.setBounds(388, 75, 112, 19);
				monthCheckBoxes.add(jMarCheckBox);
			}
			{
				jAprCheckBox = new JCheckBox();
				getContentPane().add(jAprCheckBox);
				jAprCheckBox.setText("April");
				jAprCheckBox.setBounds(388, 97, 112, 19);
				monthCheckBoxes.add(jAprCheckBox);
			}
			{
				jMayCheckBox = new JCheckBox();
				getContentPane().add(jMayCheckBox);
				jMayCheckBox.setText("May");
				jMayCheckBox.setBounds(388, 119, 112, 19);
				monthCheckBoxes.add(jMayCheckBox);
			}
			{
				jJunCheckBox = new JCheckBox();
				getContentPane().add(jJunCheckBox);
				jJunCheckBox.setText("June");
				jJunCheckBox.setBounds(388, 141, 112, 19);
				monthCheckBoxes.add(jJunCheckBox);
			}
			{
				jJulCheckBox = new JCheckBox();
				getContentPane().add(jJulCheckBox);
				jJulCheckBox.setText("July");
				jJulCheckBox.setBounds(388, 163, 112, 19);
				monthCheckBoxes.add(jJulCheckBox);
			}
			{
				jAugCheckBox = new JCheckBox();
				getContentPane().add(jAugCheckBox);
				jAugCheckBox.setText("August");
				jAugCheckBox.setBounds(388, 185, 112, 19);
				monthCheckBoxes.add(jAugCheckBox);
			}
			{
				jSepCheckBox = new JCheckBox();
				getContentPane().add(jSepCheckBox);
				jSepCheckBox.setText("September");
				jSepCheckBox.setBounds(388, 207, 122, 19);
				monthCheckBoxes.add(jSepCheckBox);
			}
			{
				jOctCheckBox = new JCheckBox();
				getContentPane().add(jOctCheckBox);
				jOctCheckBox.setText("October");
				jOctCheckBox.setBounds(388, 229, 112, 19);
				monthCheckBoxes.add(jOctCheckBox);
			}
			{
				jNovCheckBox = new JCheckBox();
				getContentPane().add(jNovCheckBox);
				jNovCheckBox.setText("November");
				jNovCheckBox.setBounds(388, 251, 112, 19);
				monthCheckBoxes.add(jNovCheckBox);
			}
			{
				jDecCheckBox = new JCheckBox();
				getContentPane().add(jDecCheckBox);
				jDecCheckBox.setText("December");
				jDecCheckBox.setBounds(388, 273, 112, 19);
				monthCheckBoxes.add(jDecCheckBox);
			}
			{
				jTabbedPane = new JTabbedPane();
				getContentPane().add(jTabbedPane);
				jTabbedPane.setBounds(12, 12, 360, 287);
				{
					jScrollPane1 = new JScrollPane();
					jTabbedPane.addTab("Categorized Spendings", null, jScrollPane1, null);
					jScrollPane1.setPreferredSize(new java.awt.Dimension(355, 233));
					{
						jCategoryTableModel = new CategorizedSpendingTableModel();
						jCategoryTable = new JTable();
						jScrollPane1.setViewportView(jCategoryTable);
						jCategoryTable.setModel(jCategoryTableModel);
					}
					//jTabbedPane.addTab("Pie Chart", null, jPieChartPanel, null);
				}
			}
			{
				jAnalyzeButton = new JButton();
				getContentPane().add(jAnalyzeButton);
				jAnalyzeButton.setText("Analyze");
				jAnalyzeButton.setBounds(418, 8, 89, 22);
				jAnalyzeButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						System.out.println("jAnalyzeButton.actionPerformed, event="+evt);
						setAnalyzedMonthData();
						jCategoryTable.updateUI();
					}
				});
			}
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1);
				jLabel1.setText("Total Spending");
				jLabel1.setBounds(12, 314, 132, 15);
			}
			{
				jTotalAmountField = new JFormattedTextField(NumberFormat.getCurrencyInstance());
				getContentPane().add(jTotalAmountField);
				jTotalAmountField.setText("$0.00");
				jTotalAmountField.setBounds(156, 311, 159, 22);
				jTotalAmountField.setEditable(false);
			}

			// check the current month
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int monthIdx = cal.get(Calendar.MONTH);
			monthCheckBoxes.get(monthIdx).setSelected(true);
			
			pack();
			this.setSize(521, 362);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}
