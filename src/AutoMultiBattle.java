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
			Thread.sleep(1000 * 60 * 10); // sleep 10 mins
			return;
		}

		driver.findElement(By.className("btn-multi-raid")).click();

		List<WebElement> supports = driver.findElements(By.className("btn-supporter"));
		for (int i = 0; i < supports.size(); i++) {
			if (supports.get(i).isDisplayed()) {
				supports.get(i + 1).click();
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
