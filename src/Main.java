import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Main extends JFrame implements ActionListener{
	static Config config;
	static WebDriver driver;
	static ThreadBase session;
	static Thread mainThread;
	
	JButton btAutoMulti;
	JButton btBattleTest;
	
	JTextPane  labelCommon;
	
	JCheckBox chkDanisenItemUse;
	
	
	public Main(){
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		JPanel panel = new JPanel();
		add(panel);
		btAutoMulti = new JButton("Auto Multi Battle");
		panel.add(btAutoMulti);
		btAutoMulti.addActionListener(this);
		
		panel = new JPanel();
		add(panel);
		btBattleTest = new JButton("Battle Test");
		panel.add(btBattleTest);
		btBattleTest.addActionListener(this);

		setTitle("gran bot"); // "super" Frame sets title
		pack();
	    //setSize(350, 100);           // "super" Frame sets initial size
	    setVisible(true);            // "super" Frame shows
	    
	    addWindowListener(new WindowAdapter(){
    	  public void windowClosing(WindowEvent we){
    		dispose();
    		if(driver!=null){
    			driver.close();
    		}
    	    System.exit(0);
    	  }
    	});
	}
	
	public static void main(String[] args) {
		new Main();
		
		config = new Config();
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
		
		Map<String, String> mobileEmulation = new HashMap<String, String>();
		mobileEmulation.put("deviceName", "Google Nexus 5");
		
		List<String> options = new ArrayList<String>();
		options.add("user-data-dir=C:\\Users\\" + System.getProperty("user.name") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\");
		
		Map<String, Object> chromeOptions = new HashMap<String, Object>();
		chromeOptions.put("mobileEmulation", mobileEmulation);
		chromeOptions.put("args", options);
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		driver = new ChromeDriver(capabilities);
		
		//Puts an Implicit wait, Will wait for 5 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		  
		driver.navigate().to(config.URL_GAME);
	      
		//Maximize the browser
		driver.manage().window().maximize();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btAutoMulti){
			session = new AutoMultiBattle(1000);
			session.config(driver,  config);
			mainThread = new Thread(session);
			mainThread.start();
		}
		if(e.getSource() == btBattleTest){
			session = new AutoMultiBattle(1000);
			session.config(driver,  config);
			((AutoMultiBattle)session).DoBattle();
		}
	}
}



