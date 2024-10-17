import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Main {
	public static void main(String[] args) {
		// Initialization
		WebDriver driver = new ChromeDriver();
		//Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		System.out.println("Work!");
		// Logic
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='menu-container']")));

	}
}