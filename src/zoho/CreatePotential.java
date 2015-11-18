package zoho;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CreatePotential {
	public WebDriver driver;
	Wait<WebDriver> wait;	
	SoftAssert softAssert = new SoftAssert();
	public String accountName = "Hardware";
	String calendarMonthValue;
	String potentialValue = "Potential_LaptopAccessories";
	
	@Test(groups = { "login" }) 
	public void login()
	{
		//load zohocrm url and log in using valid credentials
	}
	
	
	@Test(groups = {" createPotential " }, dependsOnMethods = { "login" } )
	public void createPotential() 
	{
		wait = new WebDriverWait(driver, 3000);
		driver.findElement(By.linkText("Potentials")).click();		
		driver.findElement(By.cssSelector("input[value='Create Potential']")).click();   //created a few potentials and so Create Potential button is not being displayed under Potentials tab. 
																						 //Assuming its value as "Create Potential" based on buttons of other tab 
		//Enter Potential Name
		WebElement potentialName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("property(Potential Name)")));
		potentialName.sendKeys(potentialValue);
				
		
		//Account Name lookup
		String mainWindow = driver.getWindowHandle();
		driver.findElement(By.className("Accounts-small")).click();
		
		Set windowHandlesAccount = driver.getWindowHandles();		
		Iterator ite = windowHandlesAccount.iterator();		
		while(ite.hasNext())
		{
			String popHandle = ite.next().toString();
			if(!popHandle.contains(mainWindow))
			{
				driver.switchTo().window(popHandle);
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(accountName))).click();
			}
		}
		
		driver.switchTo().window(mainWindow);
		
		WebElement actualAccountName = driver.findElement(By.id("Account Name"));
		softAssert.assertEquals(actualAccountName, accountName, "Account Name is incorrect");
		
		//Account Name field clear
		WebElement potentialInformationTable = driver.findElement(By.id("secContentPotential Information"));
		WebElement potentialTableBody = potentialInformationTable.findElement(By.tagName("tbody"));
		List<WebElement> potentialTableRow = potentialTableBody.findElements(By.tagName("tr"));
		for(WebElement potentialRowValue : potentialTableRow)
		{
			WebElement row2 = driver.findElement(By.id("row2_Potential Information"));
			List<WebElement> tdValue = row2.findElements(By.tagName("td"));
			for(WebElement tdVal : tdValue)
			{
				WebElement elem = tdVal.findElement(By.className("element"));
				elem.findElement(By.className("clear_lookupfield")).click();
				String accountNameValue = driver.findElement(By.id("Account Name")).getText();
				softAssert.assertEquals(accountNameValue, "", "Account Name field not cleared");
				
			}
		}
		
		
		
		//Enter Closing Date
		driver.findElement(By.id("uiType_property(Closing Date)")).click();
		Set windowHandlesCalendar = driver.getWindowHandles();
		Iterator ite2 = windowHandlesCalendar.iterator();
		String dateValue="";
		while(ite2.hasNext())
		{
			String popHandle = ite2.next().toString();
			if(!popHandle.contains(mainWindow))
			{
				driver.switchTo().window(popHandle);
				WebElement calendarMonth = driver.findElement(By.className("sCalMon"));
				String calendarMonthValue = calendarMonth.getText();
				WebElement calendarTable = driver.findElement(By.id("calHeader"));
				WebElement calendarTableBody = calendarTable.findElement(By.tagName("tbody"));
				List<WebElement> calendarTableRows = calendarTableBody.findElements(By.tagName("tr"));
				for(WebElement calendarTableRowValue : calendarTableRows)
				{
					List<WebElement> calendarTableRowTd = calendarTableRowValue.findElements(By.tagName("td"));
					for(WebElement calendarRowTdValue : calendarTableRowTd)
					{
						calendarRowTdValue.click();
						dateValue = calendarRowTdValue.getText();
					}
				}
				
			}
		}
		driver.switchTo().window(mainWindow);
		
		String[] monthYear = calendarMonthValue.split(" ");
		String mon = monthYear[0];
		String month = "";
		switch(mon)
		{
			
			case "January" : month = "01";
							break;
			case "February" :month = "02";
							 break;
			case "March" 	: month = "03";
							break;
			case "April"	: month = "04";
							break;
			case "May" 		: month = "05";
						 	break;
			case "June"    : month = "06";
							break;
			case "July" 	: month = "07";
							break;
			case "August" : month = "08";
							break;
			case "September" : month = "09";
							   break;
			case "October" 	: month = "10";
							 break; 
			case "November" : month = "11";
			 				  break;
			case "December" : month = "12";
							  break;
			default: System.out.println("Default calendar month");
					 break;			
							
			
		}
		
		String year = monthYear[1];
		String closingDateValue = month+"/"+dateValue+"/"+year;		
		String actualClosingDate = driver.findElement(By.name("property(Closing Date)")).getText();		
		softAssert.assertEquals(actualClosingDate, closingDateValue, "Incorrect Closing Date");
		
		//Enter value from calculator
		driver.findElement(By.className("calcu")).click();
		Set windowHandlesCalculator = driver.getWindowHandles();
		Iterator ite3 = windowHandlesCalculator.iterator();		
		String amount = "";
		while(ite3.hasNext())
		{
			String popHandle = ite3.next().toString();
			if(!popHandle.contains(mainWindow))
			{
				driver.switchTo().window(popHandle);
				WebElement number6 = driver.findElement(By.name("calc6"));
				amount += number6.getText();
			}
		}
		driver.switchTo().window(mainWindow);
		WebElement amountField = driver.findElement(By.name("uiType_property(Amount)"));
		String amountValue = amountField.getText();
		softAssert.assertEquals(amountValue, amount, "Amount value is incorrect");		
		
		
		
		//Stage Drop down
		WebElement stage = driver.findElement(By.name("property(Stage)"));
		stage.click();
		WebElement needAnalysis = driver.findElement(By.partialLinkText("Needs Analysis"));
		needAnalysis.click();
		softAssert.assertEquals(needAnalysis.getText(), "Needs Analysis" , "Stage Value is incorrect");
		
		//if amount is entered and stage is selected, probability and expected revenue gets populated 
		String probability = driver.findElement(By.id("property(Probability)")).getText();
		String revenue = driver.findElement(By.name("property(Expected Revenue)")).getText();
		if(amountValue != "" && stage.getText() != "-None-")
		{
			if(probability == "")
			{
				System.out.println("probability is not autopopulated");
			}
			if(revenue == "")
			{
				System.out.println("revenue is not autopopulated");
			}
		}

				
		
	    //alert if Potential Name is not entered
		if(potentialName.getText() == "")
		{
			driver.findElement(By.name("Button")).click();			
			Alert alert = driver.switchTo().alert();
			String errorMessage = alert.getText();
			softAssert.assertEquals(errorMessage, "Potential Name cannot be empty", "Potential Name - Incorrect Error Message");
			alert.accept();		
			
		}
		
		//Save Potential after entering all mandatory fields 
		driver.findElement(By.name("Button")).click();		
		String potentialCreated = driver.findElement(By.id("headervalue_Potential Name")).getText();
		softAssert.assertEquals(potentialCreated, potentialValue, "Potential Name created is Incorrect");
		
		
	}
	
		
	@BeforeClass(alwaysRun = true)
	public void beforeMethod()
	{
		driver = new FirefoxDriver();
		
	}
	
	@AfterClass(alwaysRun = true)
	public void afterMethod()
	{
		driver.quit();
	}
	
	

}
