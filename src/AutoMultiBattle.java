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

	public void DoBattle() {
		while (true) {
			try {
				if (driver.findElements(By.className("prt-result-cnt")).size() > 0) {
					return;
				}

				Boolean bSent = ClickButtonByClassName("btn-usual-text");
				if(bSent){
					ClickButtonByClassName("btn-usual-ok");	
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
					Thread.sleep(3000);
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
				System.out.println(e.getMessage());
			}
		}
	}

}
