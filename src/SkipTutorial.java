
public class SkipTutorial extends ThreadBase{

	public SkipTutorial(int limit) {
		super(limit);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void main() throws Exception {
		// TODO Auto-generated method stub
		for(int i=3;i<=32;i++){
			if(i!=13 && i!=21 && i!=22){
				driver.navigate().to(config.URL_GAME + "#tutorial/" + i);
				Thread.sleep(1000);
			}
		}
		nCount++;
	}

}
