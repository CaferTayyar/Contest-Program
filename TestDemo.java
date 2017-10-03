package bilgiYarismasi;


import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TestDemo {
	
	static TestFrame test;

	public static void main( String[] args ) throws FileNotFoundException{
		
		javax.swing.SwingUtilities.invokeLater( 
			new Runnable() {
			
				public void run() {
					try {
						test = new TestFrame();
						test.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
						test.setSize(800, 600);
						test.setVisible(true);
						test.addWindowListener( new WindowAction() );
					} catch (FileNotFoundException exception) {
						exception.printStackTrace();
					}					
				}
			}
		);
	}
	
	// Program yanlışlıkla kapatılmasın diye "Kapatılsın mı?" diye doğrulama sorusu çıkar.
	public static class WindowAction extends WindowAdapter{

		public void windowClosing(WindowEvent event) {
			
			super.windowClosing( event );
			
			// yes'e basıldığında 0 dönüyor. "no" da 1, "cancel" da 2 dönüyor.
			if( JOptionPane.showConfirmDialog(null, "Kapatılsın mı?") == 0 ){
				
				test.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				
				// Yarışmada bir değişiklik yapıldıysa programdan çıkarken bunlar kaydedilir.
				test.kayıtlıYarışmayıKaydet(); 
			}
		}
	}
}
