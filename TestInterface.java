package bilgiYarismasi;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

public interface TestInterface {
	
	JButton yarismaAyarlariTamam = new JButton( "Tamam" );
	
	// En fazla 10 yarışmacı olabilir.
	String[] maxYarışmacı = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }; 
	JComboBox maxYarışmacıComboBox = new JComboBox( maxYarışmacı ); 
	
	String[] süreler = { "60", "50", "45", "40", "30", "20", "15" };
	JComboBox süreComboBox = new JComboBox( süreler );
	
	JTextField[] yarismaciTextField = new JTextField[ 10 ];
	JLabel[] yarismaciLabel = new JLabel[ 10 ];
}
