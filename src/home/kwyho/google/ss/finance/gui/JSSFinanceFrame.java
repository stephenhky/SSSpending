package home.kwyho.google.ss.finance.gui;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
		initGUI();
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
				jNovCheckBox.setBounds(388, 251, 78, 19);
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
				jTabbedPane.setBounds(29, 30, 336, 262);
				{
					jCategoryTableModel = new CategorizedSpendingTableModel();
					jCategoryTable = new JTable();
					jTabbedPane.addTab("Spending by Categories", null, jCategoryTable, null);
					jCategoryTable.setModel(jCategoryTableModel);
				}
			}

			pack();
			this.setSize(521, 333);
		} catch (Exception e) {
		    //add your error handling code here
			e.printStackTrace();
		}
	}

}
