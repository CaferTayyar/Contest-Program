package bilgiYarismasi;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.sound.sampled.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TestFrame extends JFrame implements TestInterface{
	
	private int soruKapasitesi = 40; // En fazla 40 soru yazılabilir.
	private int yarışmacıSayısı;
	
	private JPanel leftPanelTop;
	private JPanel leftPanelMiddle;
	private JPanel leftPanelBottom;
	private JPanel centerPanel;
	private JPanel rightPanelTop;
	private JPanel rightPanelBottom;
	private JPanel rightPanel;
	private JPanel zamanSayacıPaneli;
	private JPanel yarışmacılar;		// Yarışmacıların puan tablosunun olduğu panel.
	private JPanel[] competitorsPanels;	// Her bir yarışmacının cevap seçeneklerinin olduğu panel dizisi. 
	
	// Yeni yarışma eklemeyle ilgili.
	private JPanel yeniYarışmaDoğruCevapPaneli;
	private JRadioButton[] yeniYarışmaDoğruCevapSeçenekleri;
	private ButtonGroup yeniYarışmaDoğruCevapGrubu; 
	
	private Box box;
	private Box leftPanel;
	
	private JTextArea yarışmacınınPuanı = new JTextArea();
	
	private String[] choices = { "A", "B", "C", "D", "Boş" };
	private String[] competitorsNames;
	
	private JLabel[] labelCompetitorsNames;	// Cevap seçeneklerindeki yarışmacı isimleri. 
	private JLabel soruLabel = new JLabel( "Soru 1" );
	private JLabel zamanSayacı;
	
	private JRadioButton[][] competitors;	// Yarışmacıların cevap seçenekleri.
	private ButtonGroup[] radioGroups;
	
	private JButton nextQuestion;
	private JButton correctAnswer;
	private JButton puanGösterGizle;
	private JButton yarışmayaBaşla;
	
	Icon ikon1 = new ImageIcon( getClass().getResource( "accept.png" ) );
	Icon ikon2 = new ImageIcon( getClass().getResource( "cancel.png" ) );
		
	private Color renk;
	
	private String[] question = new String[ soruKapasitesi ]; // 40 tane soru kapasitesi var.
	private String[][] answers = new String[ soruKapasitesi ][ 4 ]; // 40 tane sorunun cevapları.
	
	private String[] müzikler = { "1.wav", "2.wav", "3.wav", "4.wav", "5.wav", "6.wav", "7.wav" };
	
	private int answersNo = 0;
	
	private String sorumucevapmı;
	private String yazı;
	private String müzikHakkında = "Bu programda çeşitli müzikler kullanılmıştır.";	
	
	private int[] doğruCevap = new int[ soruKapasitesi ]; // 40 tane sorunun doğru cevabını tutar.
	
	private int soruNo = 0;	// Yarışma içinde kaçıncı soruda olduğumuzu tutar.
	
	private int soruSayısı = 0;	// Yarışmada toplam kaç tane soru olduğunu tutar.
	
	private int[] yarışmacıPuanları; 
	
	private int[] sıra;
	
	private double oran;
	
	// En fazla 10 yarışmacı olabilir.
	//private String[] maxYarışmacı = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }; 

	//private JComboBox maxYarışmacıComboBox = new JComboBox( maxYarışmacı );  
	
	// Soru süresi ayarlama, değiştirme.
	private int süreDeğişen;
	private int süre;
	//private String[] süreler = { "60", "50", "45", "40", "30", "20", "15" }; 
	//private JComboBox süreComboBox = new JComboBox( süreler );
	
	private Timer ourTimer;
	
	private boolean puanıGöster = false;
	private boolean yarışmacıCevaplarıGöster = true;
	
	private File dosya;
	private boolean dosyaSeçildiMi;	// Yarışma dosyası seçildi mi, seçilmedi mi?
	
	private Scanner file;
	
	private JMenuBar üstMenü;
	
	private JMenu seçenekler;
	private JMenuItem yeniYarışma;
	private JMenuItem kayıtlıYarışma;
	
	private JMenu soruSüresi;
	private JMenuItem soruSüresiDeğiştir;
	
	private JMenu yarışmacılarMenüsü;	
	private JMenuItem yarışmacıCevapları;
	
	private JMenu fontSizeMenu;
	private JSlider fontSizeJSlider;
	
	private JMenuBar altmenü;
	
	private JMenu hakkında;
	private JMenuItem aboutMusic;
	private JMenuItem aboutProgram;
	
	private Clip clip;
	private Clip clip1;
	private Clip clip2;
	private Clip clip3;
	private Clip clip4;
	
	// Soru ekleme ile ilgili

	private JButton soruyuEkle;
	private JButton soruEklemeyiBitir;
	
	private PrintStream yeniDosya; 
	private int sayaç;
	private String soruEkleDoğruCevap;
	private String yeniYarışmaDosyası;
	
	// Soru Değişikliği yapma ile ilgili
	private JButton soruDeğişikliğiKaydet;
	private JButton soruyuSil;
	
	private JComboBox sorularComboBox;
	private String[] kırkSoru;
	private JLabel sorularComboBoxLabel;
	
	private int seçilenSoru;
	
	private PrintStream yarışmayıKaydet; 
	PrintStream puanlarDosyası;
	
	private boolean yarışmaDeğişikliği = false; // Kayıtlı yarışmayı değiştirme işleminde true yaparız.
												// Normal yarışma modunda false'tur.
												// Böylece programdan çıkarken dosyaya kaydetme işlemi yapmaz.

	private ImageJTextArea soru;
	private ImageJTextArea[] cevaplar;
	
	public TestFrame() throws FileNotFoundException {
		super("Bilgi Yarışması");
		
		mainMethod();
		
	}
	
	public void mainMethod() throws FileNotFoundException{
		
		// Ekranın boyutlarını hesaplar.
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		int margin = (int) ( 45 * ( screenWidth / 1366.0 ) );
		oran = Math.sqrt( screenWidth*screenWidth + screenHeight*screenHeight ) / Math.sqrt( 1366*1366 + 768*768 );
		
		// Uygulamayı ekranın oratsında gösterir.
		setLocation( ( screenWidth - 800 ) / 2, ( screenHeight - 600 ) / 2 );
		
		// Başlangıç müziği ayarlanır.
		startMusic();
		
		menüler();	
			
		rightPanel = new JPanel();
		rightPanel.setLayout( new GridLayout( 2, 1, 5, 5 ) );

		rightPanelTop = new JPanel();
		rightPanelTop.setLayout( new GridLayout( 1, 1, 5, 5 ) );
				
		soru = new ImageJTextArea( getClass().getResource( "Resim2.png" ) );
		soru.setColumns( 20 );
		soru.setRows( 5 );
		soru.setFont( new Font( Font.SANS_SERIF, Font.BOLD, (int)(44 * oran ) ));
		soru.setCaretColor( Color.BLACK ); // İmlecin rengini değiştirir.
		soru.setMargin( new Insets( 10, margin , 0, margin));
		soru.setLineWrap( true );	// Soru textArea'ya sığmazsa bir alt satıra geçer.
		soru.setWrapStyleWord( true ); // kelime text area'ya sığmazsa bir alt satıra geçer.
		soru.setForeground( Color.BLACK );
		soru.setEditable( false );
		soru.setVisible( false );
		soru.setMinimumSize( new Dimension( 50, 50));
		rightPanelTop.add( soru );
				
		rightPanelBottom = new JPanel();
		rightPanelBottom.setLayout( new GridLayout(4, 1, 5, 10));
		
		cevaplar = new ImageJTextArea[ 4 ];
		for( int i = 0; i < 4; i++ ){
		
			cevaplar[ i ] = new ImageJTextArea( getClass().getResource( "Resim3.png" ) );
			cevaplar[ i ].setColumns( 5 );
			cevaplar[ i ].setRows( 1 );
			cevaplar[ i ].setFont( new Font( Font.SANS_SERIF, Font.BOLD, (int)(27 * oran) ));
			cevaplar[ i ].setCaretColor( Color.WHITE ); // İmlecin rengini değiştirir.
			cevaplar[ i ].setMargin( new Insets( 3, margin, 0, margin ) );
			cevaplar[ i ].setLineWrap( true );
			cevaplar[ i ].setWrapStyleWord( true );
			cevaplar[ i ].setBackground( Color.CYAN );
			cevaplar[ i ].setForeground( Color.WHITE );
			cevaplar[ i ].setEditable( false );
			cevaplar[ i ].setVisible( false );
			cevaplar[ i ].setMinimumSize( new Dimension( 50, 50));
			rightPanelBottom.add( cevaplar[ i ] );
		}

		rightPanelTop.setBackground( Color.LIGHT_GRAY );
		rightPanelBottom.setBackground( Color.LIGHT_GRAY );
		rightPanel.setBackground( Color.LIGHT_GRAY );
		
		rightPanel.add(rightPanelTop);
		rightPanel.add(rightPanelBottom);
			
		centerPanel = new JPanel();
		
		nextQuestion = new JButton("Sonraki Soru");
		nextQuestion.setEnabled( false );
		nextQuestion.addActionListener(
				new ActionListener() {
					
					public void actionPerformed(ActionEvent event) {
						
						for( int i = 0; i < yarışmacıSayısı; i++){
							labelCompetitorsNames[ i ].setIcon(null);
							radioGroups[ i ].clearSelection();
						}
												
						zamanSayacı.setForeground( Color.BLUE );
						süre = süreDeğişen;
						ourTimer.start();
						centerPanel.setVisible(false);
						yarışmacıCevapları.setText( "Yarışmacı Cevaplarını Göster" );
						yarışmacıCevaplarıGöster = true;
						
						// Yeşil renk kaybolur.
						cevaplar[ doğruCevap[ soruNo ] ].setImage( getClass().getResource( "Resim3.png" ) );
						cevaplar[ doğruCevap[ soruNo ] ].setForeground( Color.WHITE );
						
						/* Bir önceki sorunun cevabını yeşile boyamıştık.
						   Burada arka planı tekrar eski haline döndürüyoruz
						*/
						cevaplar[ doğruCevap[ soruNo ] ].setBackground( Color.CYAN );
						
						// Doğru Cevap düğmesi kullanılabilir.
						correctAnswer.setEnabled( true );
						
						//	Sonraki soru düğmesi artık kullanılamaz.
						nextQuestion.setEnabled( false );
						
						/* array kullandığımız için ilk sorunun indeksi 0.
						 * Böyle olunca soruNo = 0 oluyor. Ama biz birinci sorudayız(Soru 1).
						 * İkinci soruya geçtiğimizde Soru 2'ye geçmiş olacağız.
						 * Bu yüzden soruNo'ya 1 ekledik.
						 */
						soruNo++;
						soruLabel.setText( "Soru " + ( soruNo + 1 ) );						
						
						// Bir sonraki soruyu yazar.
						soru.setText( question[ soruNo ] );
						
						// Bir sonraki sorunun cevaplarını yazar.
						for( int i = 0; i < 4; i++)
							cevaplar[ i ].setText( choices[ i ] + ")" + answers[ soruNo ][ i ] );
						
						// Doğru cevap müziği kapatılır.
						clip4.close();
						
						// müzik çalar.
						music();
					}
				}
		);
		
		correctAnswer = new JButton("Doğru Cevap");
		correctAnswer.setEnabled( false );
		correctAnswer.addActionListener(
			new ActionListener() {
				
				public void actionPerformed(ActionEvent event) {
										
					// Yarışmacıların doğru mu yoksa yanlış mı cevap verdiğini gösterir.
					for( int i = 0; i < yarışmacıSayısı; i++){
						if( competitors[ i ][ doğruCevap[ soruNo ] ].isSelected() ){
							labelCompetitorsNames[ i ].setIcon( ikon1 );
							yarışmacıPuanları[ i ]++; // yarışmacının puanı 1 artırılır.
						}
						else
							labelCompetitorsNames[ i ].setIcon( ikon2 );
					}
					
					// Yarışmacıların puanları dosyaya kaydedilir.
					try {
						puanlar();
					} catch (FileNotFoundException e) {
						
						e.printStackTrace();
					} 
					
					// Yeşil renk görünür.
					cevaplar[ doğruCevap[ soruNo ] ].setImage( getClass().getResource( "Resim4.png" ) );
					cevaplar[ doğruCevap[ soruNo ] ].setForeground( Color.BLACK );
					
					// Doğru cevabın arka plan rengini yeşile boyar.
					cevaplar[ doğruCevap[ soruNo ] ].setBackground( Color.GREEN );
					
					// "Doğru Cevap" düğmesi "Sonraki Soru" Düğmesi basılana kadar kullanılamaz.
					correctAnswer.setEnabled( false );
					
					// Sonraki soru düğmesi aktif hale gelir.
					nextQuestion.setEnabled( true );
						
					// Eğer süre bitmeden doğru cevap görülmek istenirse çalan müzikler kapatılır.
					clip1.close();
					clip2.close();
					clip3.close();
					
					// "Ses efekti olur."
					doğruCevapMüziği();
					
					// Eğer süre bitmeden doğru cevap düğmesine basılırsa süre durdurulur.
					ourTimer.stop();
					
					//
					sıra = puanSıralaması( yarışmacıPuanları );
					yazı = "";
					for( int i = 0; i < yarışmacıSayısı; i++){
						yazı += "    " + ( i + 1 ) + ")   " + competitorsNames[ sıra [ i ] ] 
							 + ": " + yarışmacıPuanları[ sıra [ i ] ] + "    \n";
					}
					yazı += "          PUANLAR          ";
					yarışmacınınPuanı.setText( yazı );
					
					// Sorular bittiğinde aşağıdaki yapılır.
					if( soruNo == 39 || soruNo == soruSayısı - 1 ){
						soru.setText( "Yarışma bitmiştir." );
						nextQuestion.setEnabled( false );
					}
				}
			}
		);
		
		yarışmayaBaşla = new JButton( "Yarışmaya Başla" );		
		yarışmayaBaşla.addActionListener(
			new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					soruSayısı = 0;	// Yarışmada toplam kaç tane soru olduğunu tutar.
					// Yarışma dosyası seçilir.
					dosyaSeçme();
					
					// Yarışma dosyası seçilene kadar bu işlemleri yapmaz.
					if( dosyaSeçildiMi == true ){
					
						try {
							dosyaOkuma();
						
						} catch (FileNotFoundException e) {
							
							e.printStackTrace();
						}
						
						/*	Başlangıç müziği başa alınır.
						 *  Eğer bu olmazsa, ayarlarda iptal butonuna basılınca tekrar soru dosyası seçilir
						 *  aynı anda iki tane başlangıç müziği çalar.
						 */  
						clip.setFramePosition( 0 );
						clip.start();
						
						//
						new yarismaAyarlari();
						//
						
						yarismaAyarlariTamam.addActionListener(
							new ActionListener() {
								
								public void actionPerformed(ActionEvent arg0) {
									//	Yarışmacı sayısı belirlenir
									yarışmacıSayısı = maxYarışmacıComboBox.getSelectedIndex() + 1;	 // index arraydeki sırayı verdiği için 1 ekliyoruz
										
									competitorsNames = new String[ yarışmacıSayısı ];
									yarışmacıPuanları = new int[ yarışmacıSayısı ]; // Yarışmacıların puanları 
									sıra = new int[ yarışmacıSayısı ];
									
									// Yarışmacıların isimleri kaydedilir.
									changeNames();
									
									// Soru süresinin kaç saniye olacağını belirler.
									süreDeğişen = Integer.parseInt( süreler[ süreComboBox.getSelectedIndex() ] );
									süre = süreDeğişen;
									
									sağPanel();
									
									puanTablosu();
									
									// Başlangıç müziği kapatılır.
									clip.close();
									
									//
									ourTimer.start();
									music();
									correctAnswer.setEnabled( true );
									yarışmayaBaşla.setVisible( false );
									soru.setVisible( true );
									yarışmacıCevapları.setEnabled( true );
									yeniYarışma.setEnabled( false );
									kayıtlıYarışma.setEnabled( false );
								
									soru.setText( question[ 0 ] );
									for( int i = 0; i < 4; i++){
										cevaplar[ i ].setText( choices[ i ] + ")" + answers[ soruNo ][ i ] );
										cevaplar[ i ].setVisible( true );
									}
									
								}
							}
						);
					}
				}
			}
		);
		
		leftPanelMiddle = new JPanel();
		leftPanelMiddle.add( nextQuestion );
		leftPanelMiddle.add( correctAnswer );
		leftPanelMiddle.add( yarışmayaBaşla );
				
		zamanSayacı = new JLabel( "Zaman" );
		zamanSayacı.setFont( new Font( Font.SANS_SERIF, Font.BOLD, (int)( 75*oran) ));
		zamanSayacı.setForeground( Color.BLUE );
		
		// 1000 milisanyide bir yani 1 saniyede bir aşağıdaki işlemi yapar.
		ourTimer = new Timer( 1000, 
			new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					if( süre >= 0 ){
						zamanSayacı.setText( "" + süre ); // burada int değil de string kabul ediyor.
						süre--;
						
						if( süre == 9){
							zamanSayacı.setForeground( Color.RED );
							clip2.close();
							clip3.start();
						}
					}	
					else{
						ourTimer.stop();
						centerPanel.setVisible(true);
						yarışmacıCevapları.setText( "Yarışmacı Cevaplarını Gizle" );
						yarışmacıCevaplarıGöster = false;
					}
				}
			}
		);
		
		soruLabel.setFont( new Font(Font.SANS_SERIF, Font.BOLD, (int)( 75*oran ) ));
		
		leftPanelTop = new JPanel();
		leftPanelTop.add( soruLabel );
		
		leftPanel = Box.createVerticalBox();
		
		yarışmacılar = new JPanel( new BorderLayout() );
		
		puanGösterGizle = new JButton( "Puanları Gizle" );
		puanGösterGizle.addActionListener(
				new ActionListener() {
				
					public void actionPerformed(ActionEvent event) {
						if( puanıGöster == false ){
							yarışmacınınPuanı.setVisible( false );
							puanGösterGizle.setText( "Puanları Göster" );
							puanıGöster = true;
						}
						else{
							yarışmacınınPuanı.setVisible( true );
							puanGösterGizle.setText( "Puanları Gizle" );
							puanıGöster = false;
						}
					}
				}
		);
		
		yarışmacılar.add( puanGösterGizle, BorderLayout.NORTH );
		
					
		leftPanelBottom = new JPanel();
		leftPanelBottom.add( yarışmacılar );
		
		// Zaman sayacının olduğu panel yapılır.
		zamanSayacıPaneli = new JPanel();
		zamanSayacıPaneli.add(zamanSayacı);
		
		leftPanel.add( leftPanelTop );
		leftPanel.add( leftPanelMiddle );
		leftPanel.add( zamanSayacıPaneli );
		leftPanel.add( leftPanelBottom );
		
		// Arka plan rengi açık gri renge boyanır.
		leftPanelTop.setBackground( Color.LIGHT_GRAY );
		leftPanelBottom.setBackground( Color.LIGHT_GRAY );
		leftPanelMiddle.setBackground( Color.LIGHT_GRAY );
		zamanSayacıPaneli.setBackground( Color.LIGHT_GRAY );
		
		box = Box.createHorizontalBox();
		box.add(leftPanel);
		box.add(centerPanel);
		box.add(rightPanel);
		
		centerPanel.setVisible(false);
			
		add( üstMenü, BorderLayout.NORTH );
		add( box, BorderLayout.CENTER );
		add( altmenü, BorderLayout.SOUTH );
		
	}
	
	public void yeniYarışmaDoğruCevap(){
		JLabel yeniYarışmaDoğruCevabıİşaretleyiniz = new JLabel( "Doğru cevabı işaretleyiniz." );
		yeniYarışmaDoğruCevapPaneli = new JPanel( new GridLayout( 5, 1 ) );
		yeniYarışmaDoğruCevapSeçenekleri = new JRadioButton[ 4 ];
		yeniYarışmaDoğruCevapGrubu = new ButtonGroup();
		
		yeniYarışmaDoğruCevapPaneli.setBackground( Color.YELLOW );
		yeniYarışmaDoğruCevapPaneli.add( yeniYarışmaDoğruCevabıİşaretleyiniz );
		for( int i = 0; i < 4; i++ ){
			yeniYarışmaDoğruCevapSeçenekleri[ i ] = new JRadioButton( choices[ i ] );
			yeniYarışmaDoğruCevapSeçenekleri[ i ].setBackground( Color.YELLOW );
			yeniYarışmaDoğruCevapGrubu.add( yeniYarışmaDoğruCevapSeçenekleri[ i ] ); 
			yeniYarışmaDoğruCevapPaneli.add( yeniYarışmaDoğruCevapSeçenekleri[ i ] );
		}
	}
	
	public void sağPanel(){
		centerPanel.setLayout( new GridLayout( ( yarışmacıSayısı + 1 ) / 2 , 2, 5, 5) );
		// 8 tane yarışmacı var.
		labelCompetitorsNames = new JLabel[ yarışmacıSayısı ];
		competitorsPanels = new JPanel[ yarışmacıSayısı ];
		competitors = new JRadioButton[ yarışmacıSayısı ][ 5 ];
		radioGroups = new ButtonGroup[ yarışmacıSayısı ];
		for( int i = 0; i < yarışmacıSayısı; i++){
			radioGroups[ i ] = new ButtonGroup();
			competitorsPanels[ i ] = new JPanel();
			competitorsPanels[ i ].setLayout( new GridLayout(6, 1, 5, 5) );
			
			/* 1., 4., 5. ve 8. yarışmacıların cevap yeri sarı renk ile boyanır.
			   indeks 0'dan başladığı için, 0 demek 1. yarışmacı demek, 3 4. yarışmacı demektir.
			*/ 
			if( i == 0 || i == 3 || i == 4 || i == 7 || i == 8 )
				renk = Color.YELLOW;
			else
				renk = Color.WHITE;
			
			competitorsPanels[ i ].setBackground( renk );
			
			labelCompetitorsNames[ i ] = new JLabel( competitorsNames[ i ] );
			competitorsPanels[ i ].add( labelCompetitorsNames[ i ] );
			for( int j = 0; j < 5; j++){
				competitors[ i ][ j ] = new JRadioButton( choices[ j ] );
				competitors[ i ][ j ].setBackground( renk ); 
				radioGroups[ i ].add( competitors[ i ][ j ] );
								
				competitorsPanels[ i ].add( competitors[ i ][ j ]);
			}
			
			centerPanel.add( competitorsPanels[ i ] );	
			centerPanel.setBackground( Color.LIGHT_GRAY );
		}
	}
	
	public void menüler(){
		
		üstMenü = new JMenuBar();
				
		seçenekler = new JMenu( "Seçenekler" );
		üstMenü.add(seçenekler);
			
		yeniYarışma = new JMenuItem( "Yeni Yarışma" );
		yeniYarışma.addActionListener( 
			new ActionListener() {
			
				public void actionPerformed(ActionEvent arg0) {
					try {
						
						// Eğer "kayıtlı yarışmalar" klasörü yoksa oluşturulur.
						File directory = new File("kayitli yarismalar");
						if( !directory.exists() ){
							directory.mkdirs();
						}
						
						// En başta dosya ismi soruluyor.
						yeniYarışmaDosyası = JOptionPane.showInputDialog( null, "Yarışmanın kaydedileceği dosya ismi yazınız." );
						
						String kaydedilmesiİstenenDosya = yeniYarışmaDosyası;
						
						// Yarışma dosyası yazıldıysa
						if( yeniYarışmaDosyası != null ){
							
							if( !yeniYarışmaDosyası.isEmpty() ){
								yeniYarışmaDosyası += ".txt";	// text dosyası olarak kaydedilir.
								
								File newYarışmaFile = new File( "kayitli yarismalar/" + yeniYarışmaDosyası );
		
								if( !newYarışmaFile.exists() ){
									yeniDosya = new PrintStream( newYarışmaFile );
									yeniYarışmaEkle();
								}
																
								else{
									if( JOptionPane.showConfirmDialog( null, "Böyle bir dosya ismi zaten var. Yine de dosyanın isminin \"" + 
																		 kaydedilmesiİstenenDosya + "\" olmasını istiyor musunuz?"	) == 0 ){
										
										yeniDosya = new PrintStream( newYarışmaFile );
										yeniYarışmaEkle();
									}
								}
							}
							
							else{
								JOptionPane.showMessageDialog( null, "Dosya ismi yazmanız gerekiyor." );
							}
						}
						
					} catch (FileNotFoundException e) {
						
						e.printStackTrace();
					}
				}
			}
		);
		
		kayıtlıYarışma = new JMenuItem( "Kayıtlı Yarışmada Değişiklik Yapma" );
		kayıtlıYarışma.addActionListener( 
			new ActionListener() {
			
				public void actionPerformed(ActionEvent arg0) {
					
					try {
						kayıtlıYarışmadaDeğişiklik();
					} catch (FileNotFoundException e) {
						
						e.printStackTrace();
					}
				}
			}
		);
		
		seçenekler.add( yeniYarışma );
		seçenekler.add( kayıtlıYarışma );
		
		yarışmacılarMenüsü = new JMenu( "Yarışmacılar" );
				
		yarışmacıCevapları = new JMenuItem( "Yarışmacı Cevaplarını Göster" );
		yarışmacıCevapları.addActionListener(
			new ActionListener() {
			
				public void actionPerformed(ActionEvent event) {

					if( yarışmacıCevaplarıGöster == false ){
						centerPanel.setVisible( false );
						yarışmacıCevapları.setText( "Yarışmacı Cevaplarını Göster" );
						yarışmacıCevaplarıGöster = true;
					}
					else{
						centerPanel.setVisible( true );
						yarışmacıCevapları.setText( "Yarışmacı Cevaplarını Gizle" );
						yarışmacıCevaplarıGöster = false;
					}
				}
			}
		);
		
		yarışmacıCevapları.setEnabled( false );
		yarışmacılarMenüsü.add( yarışmacıCevapları );
		üstMenü.add( yarışmacılarMenüsü );
		
		soruSüresi = new JMenu( "Soru Süresi" );
		soruSüresiDeğiştir = new JMenuItem( "Soru süresini değiştir" );
		
		soruSüresiDeğiştir.addActionListener( 
			new ActionListener() {
			
				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog( null, süreComboBox, "Soru süresi kaç saniye olsun?", 1 );
					süreDeğişen = Integer.parseInt( süreler[ süreComboBox.getSelectedIndex() ] );
				}
			}
		);
		
		soruSüresi.add( soruSüresiDeğiştir );
		üstMenü.add( soruSüresi );
		
		// Soru ve cevapların yazı büyüklüğünü ayarlayan JSlider üst menüye eklenir.
		fontSizeMenu = new JMenu( "Yazı Büyüklüğü: " + (int)(44 * oran) );
		fontSizeMenu.setEnabled( false );
		üstMenü.add( fontSizeMenu );
		
		fontSizeJSlider = new JSlider( SwingConstants.HORIZONTAL, 0, 9, 5 );
		fontSizeJSlider.setMajorTickSpacing( 1 );
		fontSizeJSlider.setPaintTicks( true );
		fontSizeJSlider.setMaximumSize( new Dimension(200, 100));
		fontSizeJSlider.addChangeListener(
			new ChangeListener() {
			
				public void stateChanged(ChangeEvent arg0) {
					fontSizeMenu.setText( "Yazı Büyüklüğü: " + ((int)(44 * oran) + ( fontSizeJSlider.getValue() - 5 )) );
					soru.setFont( new Font( Font.SANS_SERIF, Font.BOLD, (int)(44 * oran ) + ( fontSizeJSlider.getValue() - 5 ) ));
					
					for( int c = 0; c < 4; c++ ){
						cevaplar[ c ].setFont( new Font( Font.SANS_SERIF, Font.BOLD, (int)(27 * oran) + ( fontSizeJSlider.getValue() - 5 ) ));
					}				
				}
			}
		);
		üstMenü.add( fontSizeJSlider );
		
		altmenü = new JMenuBar();
		hakkında = new JMenu( "Hakkında" );
		aboutMusic = new JMenuItem( "Müzikler" );
		hakkında.add( aboutMusic );
		
		aboutMusic.addActionListener(
				new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						JOptionPane.showMessageDialog( null, müzikHakkında);
					}
				}
		);
		
		aboutProgram = new JMenuItem( "Program" );
		aboutProgram.addActionListener(
				new ActionListener() {
					
					public void actionPerformed(ActionEvent arg0) {
						JOptionPane.showMessageDialog( null, "Elhamdülillah. Copyright Cafer Tayyar YÖRÜK." );
					}
				}
		);
		
		hakkında.add( aboutProgram );
		altmenü.add( hakkında );
		
	}
	
	public void startMusic() {
	//// başlangıç sesi
		try {
			
			AudioInputStream audioIn = AudioSystem.getAudioInputStream( getClass().getResource( "1_000_000.wav" ) );
			
			clip = AudioSystem.getClip();
			
			clip.open(audioIn);
			
		} catch (UnsupportedAudioFileException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch ( LineUnavailableException e){
			e.printStackTrace();
		}
	}
	
	public void music(){

		// Müzik bölümünün başlangıcı
		try {
			
			// ses 1
			
			AudioInputStream audioIn1 = AudioSystem.getAudioInputStream( getClass().getResource( "kmo5.wav" ) );
			
			clip1 = AudioSystem.getClip();
			
			clip1.open(audioIn1);
			clip1.start();
					
			//// ses 2
			
			// 7 tane müzik olduğu için %7 yaptık.
			File soundFile2 = new File( "musics/" + müzikler[ soruNo % 7 ] );
			AudioInputStream audioIn2 = AudioSystem.getAudioInputStream( soundFile2 );
			
			clip2 = AudioSystem.getClip();
			
			clip2.open(audioIn2);

			clip1.addLineListener( 
				new LineListener() {
				
					public void update(LineEvent event) {
						if( !clip1.isRunning() )
							clip2.start();
						
					}
				}
			);
				
			//// ses 3
						
			AudioInputStream audioIn3 = AudioSystem.getAudioInputStream( getClass().getResource( "kmo5(1).wav" ) );
			
			clip3 = AudioSystem.getClip();
			
			clip3.open(audioIn3);
			
			/* ses2 bitince ses3 başlar.
			clip2.addLineListener( 
				new LineListener() {
				
					@Override
					public void update(LineEvent event) {
						if( !clip2.isRunning() )
							clip3.start();
						
					}
				}
			);
			*/		
		} catch (UnsupportedAudioFileException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch ( LineUnavailableException e){
			e.printStackTrace();
		}
		
		/// Müzik bölümünün sonu
	}
	
	public void doğruCevapMüziği(){
	//// ses 4
	try{		
		
		AudioInputStream audioIn4 = AudioSystem.getAudioInputStream( getClass().getResource( "kmo4.wav" ) );
		
		clip4 = AudioSystem.getClip();
		
		clip4.open(audioIn4);
		
		clip4.start();
		
		} catch (UnsupportedAudioFileException e) {
			
			System.out.println("1");
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("2");
			//e.printStackTrace();
		} catch ( LineUnavailableException e){
			System.out.println("3");
			//e.printStackTrace();
		}
	}
	
	public int[] puanSıralaması( int[] yarışmacıPuanları ){
		int[] array = new int[ yarışmacıSayısı ];
		
		for( int i = 0; i < yarışmacıSayısı; i++)
			array[ i ] = yarışmacıPuanları[ i ];
		
		int[] maxNo = new int[ yarışmacıSayısı ];
		int max;
		int i = 0;
		int counter = 0;
		while( counter < yarışmacıSayısı ){
			if( array[ i ] != -1 ){
				max = array[ i ];
				maxNo[ counter ] = i;
				for( int a = 0; a < yarışmacıSayısı; a++){
					if( max < array[ a ] ){
						max = array[ a ];
						maxNo[ counter ] = a;
					}
				}
				array[ maxNo[ counter ] ] = -1;
				counter++;
			}
			else{
				i++;
			}
		}
		
		return maxNo;
	}
	
	public void puanTablosu(){
		// Puan Tablosunu yapar.
		sıra = puanSıralaması( yarışmacıPuanları );
		yazı = "";
		for( int i = 0; i < yarışmacıSayısı; i++ ){
			labelCompetitorsNames[ i ].setText( competitorsNames[ i ] );
		
			yazı += "    " + ( i + 1 ) + ")   " + competitorsNames[ sıra [ i ] ] 
				 + ": " + yarışmacıPuanları[ sıra [ i ] ] + "    \n";
		}
		yazı += "          PUANLAR          ";
		
		yarışmacınınPuanı.setText( yazı );
		yarışmacınınPuanı.setFont( new Font( Font.SANS_SERIF, Font.BOLD, (int)( 24*oran ) ));
		yarışmacınınPuanı.setBackground( Color.BLACK );
		yarışmacınınPuanı.setEnabled( false );
		yarışmacılar.add( yarışmacınınPuanı, BorderLayout.CENTER );
	}
	
	public void changeNames(){
		for( int i = 0; i < yarışmacıSayısı; i++){
					
			competitorsNames[ i ] = yarismaciTextField[ i ].getText();
		}
	}
	
	public void yeniYarışmaEkle() throws FileNotFoundException{
				
		soruyuEkle = new JButton( "Soruyu Ekle" );
		soruEklemeyiBitir = new JButton( "Soru Eklemeyi Bitir" );
		
		yeniYarışma.setEnabled( false );
		correctAnswer.setVisible( false );
		nextQuestion.setVisible( false );
		yarışmayaBaşla.setVisible( false );
		puanGösterGizle.setVisible( false );
		soruSüresiDeğiştir.setEnabled( false );
		kayıtlıYarışma.setEnabled( false );
		
		soru.setVisible( true );
		soru.setEditable( true );
		soru.setText( "Soruyu buraya yazınız." );
		soru.addMouseListener( new YeniYarışmaCevapSoruSilme( soru, -1 ) );
		
		for( int i = 0; i < 4; i++){
			cevaplar[ i ].setVisible( true );
			cevaplar[ i ].setEditable( true );
			cevaplar[ i ].setText( choices[ i ] + " şıkkını buraya yazınız." );
			cevaplar[ i ].addMouseListener( new YeniYarışmaCevapSoruSilme( cevaplar[ i ], i ) );
		}
		
		yeniYarışmaDoğruCevap();
		zamanSayacı.setVisible( false );
		
		zamanSayacıPaneli.add( yeniYarışmaDoğruCevapPaneli );
		
		leftPanelMiddle.add( soruyuEkle );
		leftPanelMiddle.add( soruEklemeyiBitir );
					
		sayaç = 1;
		
		soruyuEkle.addActionListener(
			new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					for( int i = 0; i < 4; i++){
						if( yeniYarışmaDoğruCevapSeçenekleri[ i ].isSelected() ){
							soruEkleDoğruCevap = choices[ i ];
						}
					}
					
					// Doğru cevabın hangi şık olduğu işaretlendiyse bu işlem yapılır.
					if( soruEkleDoğruCevap != null ){
					
						yeniDosya.print( "QuestioN:\n" );
						yeniDosya.print( soru.getText() );
						
						for( int i = 0; i < 4; i++){
							yeniDosya.print( "\nAnsweR:\n" );
							yeniDosya.print( cevaplar[ i ].getText().substring( 2, cevaplar[ i ].getText().length() ) );
						}
						
						yeniDosya.print( "\nCorrecT:\n" );
						yeniDosya.print( soruEkleDoğruCevap + "\n\n");
						
						//
						sayaç++;
						soruLabel.setText( "Soru " + sayaç );
						
						soru.setText( "Soruyu buraya yazınız." );
						soru.addMouseListener( new YeniYarışmaCevapSoruSilme( soru, -1 ) );
						for( int i = 0; i < 4; i++){
							cevaplar[ i ].setText( choices[ i ] + " şıkkını buraya yazınız." );
							cevaplar[ i ].addMouseListener( new YeniYarışmaCevapSoruSilme( cevaplar[ i ], i ) );
						}
						
						yeniYarışmaDoğruCevapGrubu .clearSelection();
						soruEkleDoğruCevap = null;
						
						// Soru sayısı 40'a ulaştığında kullanıcı uyarılır.
						if( sayaç == 40 ){
							soru.setText( "En fazla 40 soru ekleyebilirsiniz. Bundan sonra soru eklenmez." );
						}
					}
					
					// Doğru cevabın hangi şık olduğu işaretlenmediyse bu işlem yapılır.
					else{ 	// soruEkleDoğruCevap == null
						JOptionPane.showMessageDialog( null, "Doğru cevap şıkkını işaretleyiniz." );
					}
				}
			}
		);
				
		soruEklemeyiBitir.addActionListener(
			new ActionListener() {
			
				public void actionPerformed(ActionEvent arg0) {
					JOptionPane.showMessageDialog( null, "Sorularınız \"" + yeniYarışmaDosyası + 
							"\" adındaki dosyaya kaydedilmiştir. Şimdi programdan çıkış yapabilirsiniz." );
				}
			}		
		);
	}
	
	public class YeniYarışmaCevapSoruSilme extends MouseAdapter{
		
		JTextArea text;
		boolean isClicked = false;
		int i;
		public YeniYarışmaCevapSoruSilme( JTextArea text, int i ){
			this.text = text;
			this.i = i;
		}
		
		public void mouseClicked(MouseEvent arg0) {
			
			super.mouseClicked(arg0);
			
			// İlk sefer fare tıklandığında yazı silinir sonraki tıklamalarda yazı silinmez.
			// Böylece yazıda fare ile düzenleme yapabilirsiniz.
			if( isClicked == false ){
				
				if( i != -1 ){
					text.setText( choices[ i ] + ") " );
				}
				else{
					text.setText( "" );
				}
				
				isClicked = true;
			}
		}
	}
	
	// Yarışmacıların puanlarını dosyaya yazar. Bir aksilik olursa puanlara buradan bakılabilir.
	public void puanlar() throws FileNotFoundException{
		puanlarDosyası = new PrintStream( new File( "puanlar.txt" ));
		for( int i = 0; i < yarışmacıSayısı; i++){
			puanlarDosyası.print( competitorsNames[ i ] + ": " + yarışmacıPuanları[ i ] + "		\n");
		}
	}
	
	// Kayıtlı yarışmanın olduğu dosyayı seçmek için.
	public void dosyaSeçme(){
		JFileChooser fileChooser = new JFileChooser();

		fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
		fileChooser.setDialogTitle( "Bir yarışma dosyası seçin" );
		fileChooser.setApproveButtonText( "Seç" );
					
		File fDirectory = new File( "kayitli yarismalar" );
		fileChooser.setCurrentDirectory( fDirectory );
		
		int result = fileChooser.showOpenDialog( this );
		
		if( result == JFileChooser.CANCEL_OPTION){
			dosyaSeçildiMi = false;
		}
		else{
			dosyaSeçildiMi = true;
			File seçilenDosya = fileChooser.getSelectedFile();	
			dosya = new File( seçilenDosya.getAbsolutePath() );
			JOptionPane.showMessageDialog( null, "\"" + dosya.getName() + "\" adlı yarışma ayarlanmıştır.");
		}
	}
	
	public void dosyaOkuma() throws FileNotFoundException{
		boolean sc = false;
		file = new Scanner( dosya );
		while( file.hasNext() ){
			sorumucevapmı = file.nextLine();
			
			if( sorumucevapmı.equals( "QuestioN:" ) ){
				question[ soruSayısı ] = file.nextLine();
				sc = true;
			}
			
			else if( sorumucevapmı.equals( "AnsweR:" ) ){
				answers[ soruSayısı ][ answersNo ] = file.nextLine();
				answersNo++;
				sc = false;
			}
			
			else if( sorumucevapmı.equals( "CorrecT:" ) ){
				
				sorumucevapmı = file.next();
				
				if( sorumucevapmı.equals( "A" ) )
					doğruCevap[ soruSayısı ] = 0;
				
				else if( sorumucevapmı.equals( "B" ) )
					doğruCevap[ soruSayısı ] = 1;
				
				else if( sorumucevapmı.equals( "C" ) )
					doğruCevap[ soruSayısı ] = 2;
				
				else if( sorumucevapmı.equals( "D" ) )
					doğruCevap[ soruSayısı ] = 3;
				
				soruSayısı++;
				answersNo = 0;
				
			}
			
			// Soru veya cevaplardan biri 1 satırdan daha fazla ise bu işlem yapılır.
			else{
				if( sc == true ){
					question[ soruSayısı ] += "\n" + sorumucevapmı;
				}
				else if( sc == false && answersNo > 0 ){
					answers[ soruSayısı ][ ( answersNo - 1 ) ] += "\n" + sorumucevapmı; 
				}
			}
		}
	}
	
	public void kayıtlıYarışmadaDeğişiklik() throws FileNotFoundException{
		yarışmaDeğişikliği = true;
		
		dosyaSeçme();
		dosyaOkuma();
		
		soruSüresiDeğiştir.setEnabled( false );
		yeniYarışma.setEnabled( false );
		kayıtlıYarışma.setEnabled( false );
		
		yarışmayıKaydet = new PrintStream( dosya );
		
		nextQuestion.setVisible( false );
		correctAnswer.setVisible( false );
		yarışmayaBaşla.setVisible( false );
		
		kırkSoru = new String[ 40 ];
		for( int i = 0; i < 40; i++){
			kırkSoru[ i ] = ( i + 1 ) + ". Soru"; 
			
			if( i >= soruSayısı ){
				for( int a = 0; a < 4; a++ ){
					answers[ i ][ a ] = "";
				}
			}
		}
				
		yarışmacılar.setBackground( Color.YELLOW );
		
		sorularComboBoxLabel = new JLabel( "Değiştirmek istediğiniz soruyu seçiniz." );
		yarışmacılar.add( sorularComboBoxLabel, BorderLayout.NORTH );	
		
		sorularComboBox = new JComboBox( kırkSoru );
		puanGösterGizle.setVisible( false );
		yarışmacılar.add( sorularComboBox, BorderLayout.CENTER );
		
		sorularComboBox.addActionListener( 
			new ActionListener() {
	
				public void actionPerformed(ActionEvent arg0) {
					seçilenSoru = sorularComboBox.getSelectedIndex();

					soruLabel.setText( "Soru " + ( seçilenSoru + 1 ) );
					
					soru.setText( question[ seçilenSoru ] );
					yeniYarışmaDoğruCevapSeçenekleri[ doğruCevap[ seçilenSoru ] ].setSelected( true );
					for( int i = 0; i < 4; i++){
						cevaplar[ i ].setText( choices[ i ] + ")" + answers[ seçilenSoru ][ i ] );
					}
					
					// Seçilen sorunun indeksini array'den alıyoruz. Mesela question[0]'ı aldık.
					// Bu 1. soru demektir. soruSayısı 7 olsun. En son sorunun indeksi 6 olur.
					// Eğer seçilenSoru 7 olursa bu question[7] demektir ama bizim en son question[6]'mız vardır.
					// Bu da demektir ki question[7]'ye yeni soruyu yazabilirsiniz.
					if( seçilenSoru > soruSayısı ){
						soru.setText( "Yeni soru eklemek istiyorsanız " + ( soruSayısı + 1 ) + ". soruyu seçiniz." );
						soruDeğişikliğiKaydet.setEnabled( false );
						soruyuSil.setEnabled( false );
					}
					
					else if( seçilenSoru == soruSayısı ){
						soruDeğişikliğiKaydet.setEnabled( true );
						soruDeğişikliğiKaydet.setText( "Bu yeni soruyu ekle" );
						soruyuSil.setEnabled( false );
					}
					
					else{ // seçilenSoru < soruSayısı
						soruDeğişikliğiKaydet.setEnabled( true );
						soruDeğişikliğiKaydet.setText( "Sorudaki değişikliği kaydet" );
						soruyuSil.setEnabled( true );
					}
				}
			}
		);
		
		zamanSayacı.setVisible( false );
		yeniYarışmaDoğruCevap();
		zamanSayacıPaneli.add( yeniYarışmaDoğruCevapPaneli );
		
		soru.setVisible( true );
		soru.setEditable( true );
		
		soru.setText( question[ 0 ] );
		yeniYarışmaDoğruCevapSeçenekleri[ doğruCevap[ 0 ] ].setSelected( true );
		for( int i = 0; i < 4; i++){
			cevaplar[ i ].setText( choices[ i ] + ")" + answers[ 0 ][ i ] );
			cevaplar[ i ].setVisible( true );
			cevaplar[ i ].setEditable( true );
		}
		
		soruDeğişikliğiKaydet = new JButton( "Sorudaki değişikliği kaydet" );
		soruDeğişikliğiKaydet.addActionListener(
			new ActionListener() {
	
				public void actionPerformed(ActionEvent arg0) {
					
					if( seçilenSoru == soruSayısı ) // Yeni soru anlamına gelir. Ayrıca biraz yukarıda olan
					{ 								// sorularComboBox'taki yoruma da bakabilirsiniz.
						soruDeğişikliğiKaydet.setText( "Sorudaki değişikliği kaydet" ); // Öncesinde "Yeni soru ekle" yazıyordu.
						soruyuSil.setEnabled( true );
						soruSayısı++; 
					}
					
					question[ seçilenSoru ] = soru.getText();
					
					for( int i = 0; i < 4; i++){
						answers[ seçilenSoru ][ i ] = cevaplar[ i ].getText().substring( 2, cevaplar[ i ].getText().length() );  
					}
					
					for( int i = 0; i < 4; i++){
						if( yeniYarışmaDoğruCevapSeçenekleri[ i ].isSelected() )
							doğruCevap[ seçilenSoru ] = i; 
					}
					
					JOptionPane.showMessageDialog( null, "Sorunuz kaydedildi." );
				}
			}
		);
		
		leftPanelMiddle.add( soruDeğişikliğiKaydet );
		
		soruyuSil = new JButton( "Soruyu sil" );
		soruyuSil.addActionListener(
			new ActionListener() {
	
				public void actionPerformed(ActionEvent arg0) {
					
					//	Silinen sorunun yerine bir sonraki soru geçer.
					//  Mesela, 1.soruyu sildiysek artık 2.soru 1.soru olur.
					for( int i = seçilenSoru; i < soruSayısı - 1; i++ ){
						question[ i ] = question[ i + 1 ];
						
						for( int a = 0; a < 4; a++ ){
							answers[ i ][ a ] = answers[ i + 1 ][ a ];
						}
						
						doğruCevap[ i ] = doğruCevap[ i + 1 ];
					}
					
					// Son soru artık boş olacak
					question[ soruSayısı - 1 ] = "";
					for( int a = 0; a < 4; a++ ){
						answers[ soruSayısı - 1 ][ a ] = "";
					}
					
					// Eğer soru son soru ise
					if( seçilenSoru + 1 == soruSayısı ){
						question[ seçilenSoru ] = "";
						
						for( int a = 0; a < 4; a++ ){
							answers[ seçilenSoru ][ a ] = "";
						}
						
						soruDeğişikliğiKaydet.setText( "Bu yeni soruyu ekle" );
						soruyuSil.setEnabled( false );
					}
						
					soruSayısı--;	// Toplam soru sayısı 1 eksilir.
					
					//	Ekranda silinen sorunun yerine geçen soru ve sorunun cevapları yazılır.
					soru.setText( question[ seçilenSoru ] );
					for( int a = 0; a < 4; a++ ){
						cevaplar[ a ].setText( choices[ a ] + ")" + answers[ seçilenSoru ][ a ] );
					}
					
					yeniYarışmaDoğruCevapSeçenekleri[ doğruCevap[ seçilenSoru ] ].setSelected( true );
					
					JOptionPane.showMessageDialog( null, "Soru silinmiştir." );
				}
			}
		);
		
		leftPanelMiddle.add( soruyuSil );
	}
	
	public void kayıtlıYarışmayıKaydet(){
		
		if( yarışmaDeğişikliği == true ){
			for( int a = 0; a < soruSayısı; a++ ){
				// Şu anki soru boş değilse kaydedilir.
				if( question[ a ] != null ){
					yarışmayıKaydet.print( "QuestioN:\n" );
					yarışmayıKaydet.print( question[ a ] );
					
					for( int i = 0; i < 4; i++ ){
						yarışmayıKaydet.print( "\nAnsweR:\n" );
						yarışmayıKaydet.print( answers[ a ][ i ] );
					}
					
					yarışmayıKaydet.print( "\nCorrecT:\n" );
					yarışmayıKaydet.print( choices[ doğruCevap[ a ] ] + "\n\n");
				}
			}
		}
	}
}

//Soru ve cevapların arka planına resim yerleştirir.

class ImageJTextArea extends JTextArea{
	URL image;
	public ImageJTextArea( URL image ){
		setOpaque( false );
		this.image = image;
		
	}
	
	public void paintComponent( final Graphics g ){
		try{
			g.drawImage( new ImageIcon( image ).getImage(), 0, 0, getWidth(), getHeight(), this );
			super.paintComponent(g);
		}catch( Exception e ){
			
		}
	}
	
	public void setImage( URL url_image ){
		this.image = url_image;
		repaint();
	}
}

//	Yarışma ayarlarının yapıldığı pencere açılır
class yarismaAyarlari extends JFrame implements TestInterface{
	
	public yarismaAyarlari() {
		
		JButton yarismaAyarlariİptal = new JButton( "İptal" );
		yarismaAyarlariİptal.addActionListener(
			new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					dispose();
				}
			}
		);
		
		yarismaAyarlariTamam.addActionListener(
			new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					dispose();
				}
			}
		);
		
		JLabel süreComboBoxLabel = new JLabel( "Soru Süresini Seçiniz:" );
		JLabel maxYarışmacıComboBoxLabel = new JLabel( "Yarışmacı Sayısını Seçiniz:" );
		
		JPanel yarismaAyarlariÜstPanel= new JPanel();
		JPanel yarismaAyarlariAltPanel = new JPanel();
		
		JPanel yarismaAyarlariÜstSolPanel = new JPanel();
		JPanel yarismaAyarlariÜstSağPanel = new JPanel();
		
		yarismaAyarlariÜstSolPanel.setBorder( new TitledBorder( "Soru Süresi" ) );
		yarismaAyarlariÜstSağPanel.setBorder( new TitledBorder( "Yarışmacı Sayısı" ) );
		
		yarismaAyarlariÜstSolPanel.add( süreComboBoxLabel );
		yarismaAyarlariÜstSolPanel.add( süreComboBox );
		
		yarismaAyarlariÜstSağPanel.add( maxYarışmacıComboBoxLabel );
		yarismaAyarlariÜstSağPanel.add( maxYarışmacıComboBox );
		
		yarismaAyarlariÜstPanel.add( yarismaAyarlariÜstSolPanel );
		yarismaAyarlariÜstPanel.add( yarismaAyarlariÜstSağPanel );
		
		yarismaAyarlariAltPanel.add( yarismaAyarlariTamam );
		yarismaAyarlariAltPanel.add( yarismaAyarlariİptal );
		
		JPanel yarismaciTextFieldsPanel = new JPanel( new GridLayout( 5, 2 ) );
		yarismaciTextFieldsPanel.setBorder( new TitledBorder( "Yarışmacı İsimleri" ) );
		JPanel[] yarismaciLabelTextFieldPanel = new JPanel[ 10 ];
		
		for( int i = 0; i < 10; i++ ){
			yarismaciTextField[ i ] = new JTextField( 10 );
			yarismaciTextField[ i ].setEditable( false );
			yarismaciLabel[ i ] = new JLabel( ( i + 1 ) + ". yarışmacı:" );
			
			yarismaciLabelTextFieldPanel[ i ] = new JPanel( new FlowLayout() );
			
			yarismaciLabelTextFieldPanel[ i ].add( yarismaciLabel[ i ] );
			yarismaciLabelTextFieldPanel[ i ].add( yarismaciTextField[ i ] );
			
			yarismaciTextFieldsPanel.add( yarismaciLabelTextFieldPanel[ i ] ); 
		}
		
		yarismaciTextField[ 0 ].setEditable( true );
		
		maxYarışmacıComboBox.addActionListener(
			new ActionListener() {
				
				public void actionPerformed(ActionEvent arg0) {
					
					for( int a = 0; a < 10; a++ ){
						
						if( a <= maxYarışmacıComboBox.getSelectedIndex() )
							yarismaciTextField[ a ].setEditable( true );
						
						else
							yarismaciTextField[ a ].setEditable( false );
					}
				}
			}
		);
		
		add( yarismaAyarlariÜstPanel, BorderLayout.NORTH );
		add( yarismaciTextFieldsPanel, BorderLayout.CENTER );
		add( yarismaAyarlariAltPanel, BorderLayout.SOUTH );
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				
		setLocation( ( screenSize.width - 500 ) / 2, ( screenSize.height - 300 ) / 2 );
		setTitle( "Yarışma Ayarları");
		setSize( 500, 300 );
		setVisible(true);
	}
}
