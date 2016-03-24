import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

public class AutoMultiBattle extends ThreadBase {

	public AutoMultiBattle(int limit) {
		super(limit);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void main() throws Exception {
		// TODO Auto-generated method stub
		driver.navigate().to(config.URL_GAME + "#quest/assist");
		
		ClickButtonById("tab-event");

		WebElement currentBp = driver.findElement(By.className("prt-user-bp-value"));
		int bp = Integer.parseInt(currentBp.getAttribute("title"));
		if (bp < 5) {
			
			String currentAp = driver.findElement(By.className("txt-stamina-value")).getAttribute("title");
			int ap = Integer.parseInt(currentAp.split("/")[0]);
			if(ap>25){
				driver.navigate().to(config.URL_GAME + "#event/teamraid019/supporter/708931/1");
			}
			else{
				Thread.sleep(1000 * 60 * 10); // sleep 10 mins
				return;
			}
		}
		else{
			List<WebElement> raids = driver.findElement(By.id("prt-assist-event")).findElements(By.className("btn-multi-raid"));
			for (int i = 0; i < raids.size(); i++) {
				if (raids.get(i).isDisplayed()) {
					raids.get(i).click();
					break;
				}
			}
		}
		
		List<WebElement> buttons = driver.findElements(By.className("btn-supporter"));
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).isDisplayed()) {
				buttons.get(i+1).click();
				break;
			}
		}

		if (driver.findElements(By.cssSelector(".btn-usual-ok.se-quest-start")).size() > 0) {
			js.executeScript("$('.btn-usual-ok.se-quest-start').trigger('tap');");
		}

		Thread.sleep(1000 * 10);

		DoBattle();
	}
}
