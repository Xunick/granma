import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class ThreadBase extends Thread {
	WebDriver driver;
	Config config;
	JavascriptExecutor js;

	int nLimit = 10;
	int nCount = 0;

	public ThreadBase(int limit) {
		nLimit = limit;
	}

	public abstract void main () throws Exception;

	public void run() {
		while (nCount < nLimit && !Thread.currentThread().isInterrupted()) {
			try {
				main();
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				RetryConnection();
			}
		}
	}

	public void config(WebDriver d, Config c) {
		driver = d;
		config = c;
		if (driver instanceof JavascriptExecutor) {
			js = (JavascriptExecutor) driver;
		} else {
			throw new IllegalStateException("This driver does not support JavaScript!");
		}
	}

	public Boolean RetryConnection() {
		try {
			driver.navigate().to(config.URL_GAME);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Boolean ClickButtonByClassName(String className){
		List<WebElement> buttons = driver.findElements(By.className(className));
		Boolean bSent = false;
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).isDisplayed()) {
				js.executeScript("$('." + buttons.get(i).getAttribute("class") + "').trigger('tap');");
				bSent = true;
				break;
			}
		}
		return bSent;
	}
	
	public Boolean ClickButtonById(String id){
		List<WebElement> buttons = driver.findElements(By.id(id));
		Boolean bSent = false;
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).isDisplayed()) {
				js.executeScript("$('#" + buttons.get(i).getAttribute("id") + "').trigger('tap');");
				bSent = true;
				break;
			}
		}
		return bSent;
	}
	
	public void DoBattle() {
		int nFailCount = 0;
		while (true) {
			try {
				String url = driver.getCurrentUrl();
				if(!url.contains("#raid_multi")){
					return;
				}
				
				Boolean bSent = ClickButtonByClassName("btn-usual-text");
				if(bSent){
					Thread.sleep(2000);
				}
				
				if(ClickButtonByClassName("btn-usual-ok")){
					Thread.sleep(2000);
				}

				//Proceed to next stage or finish
				if(ClickButtonByClassName("btn-result")){
					Thread.sleep(5000);
					continue;
				}

				// Use summon if possible
				if (driver.findElements(By.cssSelector(".prt-list-top.btn-command-summon.summon-on")).size() > 0) {
					js.executeScript("$('.prt-list-top.btn-command-summon.summon-on').trigger('tap');");
					if (driver.findElements(By.cssSelector(".lis-summon.btn-summon-available.on")).size() > 0) {
						js.executeScript("$('.lis-summon.btn-summon-available.on').trigger('tap');");
					}
					if (driver.findElements(By.cssSelector(".btn-usual-ok.btn-summon-use")).size() > 0) {
						js.executeScript("$('.btn-usual-ok.btn-summon-use').trigger('tap');");
					}
					Thread.sleep(5000);
					continue;
				}

				for (int type = 4; type > 0; type--) {
					//skip recovery skill for now
					if(type==2){
						continue;
					}
					List<WebElement> characters = driver.findElements(By.className("btn-command-character"));
					for (int i = 0; i < characters.size(); i++) {
						if (characters.get(i).isDisplayed()) {
							List<WebElement> abilities = characters.get(i)
									.findElements(By.className("lis-ability-state"));
							for (int j = 0; j < abilities.size(); j++) {
								if (abilities.get(j).getAttribute("state").equals("2") && Integer.parseInt(abilities.get(j).getAttribute("type")) == type) {
									// click character
									if (characters.get(i).isDisplayed()) {
										characters.get(i).click();
									}
									// click skill
									if (driver.findElements(By.cssSelector(".lis-ability.btn-ability-available"))
											.size() > 0) {
										js.executeScript("$('.ability-character-num-"+(i+1)+"-"+(j+1)+"').trigger('tap');");
									}
									// click back
									if (driver.findElements(By.cssSelector(".btn-command-back")).size() > 0) {
										js.executeScript("$('.btn-command-back').trigger('tap');");
										Thread.sleep(3500);
									}
								}
							}
						}
					}
				}

				if (driver.findElements(By.cssSelector(".btn-attack-start.display-on")).size() > 0) {
					js.executeScript("$('.btn-attack-start.display-on').trigger('tap');");
				}

				Thread.sleep(3000);
			} catch (Exception e) {
				nFailCount++;
				System.out.println(e.getMessage());
				if(nFailCount>100){
					return;
				}
			}
		}
	}

}
