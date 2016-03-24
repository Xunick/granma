import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	public String URL_GAME = "http://gbf.game.mbga.jp/";
	
	
	
	public void getPropValues(Boolean isSB){
		try {
			InputStream inputStream;
			Properties prop = new Properties();
			String propFileName = "config.properties";

 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			
			URL_GAME = prop.getProperty("GAME_URL");
			
						
			inputStream.close();
 
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	
}
