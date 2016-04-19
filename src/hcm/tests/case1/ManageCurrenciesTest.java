package hcm.tests.case1;

import static util.ReportLogger.log;
import static util.ReportLogger.logFailure;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.testng.annotations.Test;

import common.BaseTest;
import common.ExcelUtilities;
import common.TaskUtilities;
import hcm.pageobjects.FuseWelcomePage;
import hcm.pageobjects.LoginPage;
import hcm.pageobjects.TaskListManagerTopPage;

public class ManageCurrenciesTest extends BaseTest{
	private static final int MAX_TIME_OUT = 30;
	private String projectName = "Default";
	private int projectRowNum = TestCaseRow;
	
	private String searchData, labelLocator, labelLocatorPath, dataLocator;
	//private int projectCol = 8;
	private int label = 9;
	private int inputs = 10;
	private int colNum = 8;
	private int projectSheetcolNum = 7;
	
	private boolean hasManagedCurrency = false;
	
	@Test
	public void a_test() throws Exception  {
			testReportFormat();
	
	try{	
			
			manageCurrencies();
	  	}
	
        catch (AssertionError ae)
        {
            takeScreenshot();
            logFailure(ae.getMessage());

            throw ae;
        }
        catch (Exception e)
        {
            takeScreenshot();
            logFailure(e.getMessage());

            throw e;
        }
    }
	
	public void manageCurrencies() throws Exception{
		
		LoginPage login = new LoginPage(driver);
		takeScreenshot();
		login.enterUserID(5);
		login.enterPassword(6);
		login.clickSignInButton();
		
		FuseWelcomePage welcome = new FuseWelcomePage(driver);
		takeScreenshot();
		welcome.clickNavigator("More...");
		clickNavigationLink("Setup and Maintenance");
			
		TaskListManagerTopPage task = new TaskListManagerTopPage(driver);
		takeScreenshot();
		
		while(!hasManagedCurrency && !projectName.isEmpty() && !projectName.contentEquals("")){
			projectName = selectProjectName();
			
			if(projectName.contains("*")){
				projectRowNum += 1;
				continue;
			}
			
			hasManagedCurrency = manageProjectCurrencies(task);
		}
		
		Thread.sleep(2500);
		log("Manage Currencies completed.");
		System.out.println("Manage Currencies completed.");
		
	}
	
	private String selectProjectName() throws Exception{
		System.out.println("Setting Project to be edited...RowNum: "+projectRowNum+" vs. "+TestCaseRow);
		final String projectSheetName = "Create Implementation Project";

		XSSFSheet projectSheet = ExcelUtilities.ExcelWBook.getSheet(projectSheetName);
		XSSFCell projectCell;
		String newProjectName ="";
		
		if(projectRowNum <= 0){
			projectRowNum = TestCaseRow;
		}
		
	  	try{	        	   
	  		projectCell = projectSheet.getRow(projectRowNum).getCell(projectSheetcolNum);      	  
	  		projectCell.setCellType(projectCell.CELL_TYPE_STRING);
	  		newProjectName = projectCell.getStringCellValue();
	            
	            }catch (Exception e){
	            	e.printStackTrace();
	            	newProjectName="";
	            }
	  	
		System.out.println("New Project Name is now..."+newProjectName);
				
		return newProjectName;
	}

	private boolean manageProjectCurrencies(TaskListManagerTopPage task) throws Exception{
		boolean caughtInputError = false;
		
		TaskUtilities.customWaitForElementVisibility("//a[text()='Manage Implementation Projects']", MAX_TIME_OUT);
		TaskUtilities.jsFindThenClick("//a[text()='Manage Implementation Projects']");
		TaskUtilities.customWaitForElementVisibility("//h1[text()='Manage Implementation Projects']", MAX_TIME_OUT);
		
		searchData = projectName;
		labelLocator = "Name";
		labelLocatorPath = TaskUtilities.retryingSearchInput(labelLocator);
		
		TaskUtilities.consolidatedInputEncoder(task, labelLocatorPath, searchData);
		TaskUtilities.jsFindThenClick("//button[text()='Search']");
		Thread.sleep(3500);
		TaskUtilities.customWaitForElementVisibility("//a[text()='"+searchData+"']", MAX_TIME_OUT);
		TaskUtilities.jsFindThenClick("//a[text()='"+searchData+"']");

		TaskUtilities.customWaitForElementVisibility("//h1[contains(text(),'"+searchData+"')]", MAX_TIME_OUT);
		TaskUtilities.customWaitForElementVisibility("//div[text()='Workforce Deployment']", MAX_TIME_OUT);
		
		if(is_element_visible("//div[text()='Workforce Deployment']"+"//a[@title='Expand']", "xpath")){
			//TaskUtilities.jsFindThenClick("//div[text()='Workforce Deployment']"+"//a[@title='Expand']");
			TaskUtilities.retryingFindClick(By.xpath("//div[text()='Workforce Deployment']"+"//a[@title='Expand']"));
			TaskUtilities.customWaitForElementVisibility("//div[text()='Workforce Deployment']"+"//a[@title='Collapse']", MAX_TIME_OUT);
		}
		
		TaskUtilities.customWaitForElementVisibility("//div[text()='Define Common Applications Configuration for Human Capital Management']", MAX_TIME_OUT);
		
		if(is_element_visible("//div[text()='Define Common Applications Configuration for Human Capital Management']"+"//a[@title='Expand']", "xpath")){
			//TaskUtilities.jsFindThenClick("//div[text()='Define Common Applications Configuration for Human Capital Management']"+"//a[@title='Expand']");
			TaskUtilities.retryingFindClick(By.xpath("//div[text()='Define Common Applications Configuration for Human Capital Management']"+"//a[@title='Expand']"));
			TaskUtilities.customWaitForElementVisibility("//div[text()='Define Common Applications Configuration for Human Capital Management']"+"//a[@title='Collapse']", MAX_TIME_OUT);
		}
		
		TaskUtilities.customWaitForElementVisibility("//div[text()='Define Currencies and Currency Rates']", MAX_TIME_OUT);
		
		if(is_element_visible("//div[text()='Define Currencies and Currency Rates']"+"//a[@title='Expand']", "xpath")){
			TaskUtilities.retryingFindClick(By.xpath("//div[text()='Define Currencies and Currency Rates']"+"//a[@title='Expand']"));
			TaskUtilities.customWaitForElementVisibility("//div[text()='Define Currencies and Currency Rates']"+"//a[@title='Collapse']", MAX_TIME_OUT);
		}
		
		TaskUtilities.customWaitForElementVisibility("//div[text()='Manage Currencies']", MAX_TIME_OUT);
		TaskUtilities.jsFindThenClick("//div[text()='Manage Currencies']/../..//a[@title='Go to Task']");
		TaskUtilities.customWaitForElementVisibility("//h1[text()='Manage Currencies']", MAX_TIME_OUT);
		
		//Search CurrencyCode:
		outercurrencyloop:
		while(getExcelData(inputs, colNum, "text").length()>0){
			
			labelLocator = getExcelData(label, colNum, "text");
			labelLocatorPath = TaskUtilities.retryingSearchInput(labelLocator);
			searchData = getExcelData(inputs, colNum, "text");
			System.out.println("Now Holding: "+labelLocator+" : "+searchData);
			
			TaskUtilities.consolidatedInputEncoder(task, labelLocatorPath, searchData);
			driver.findElement(By.xpath(labelLocatorPath)).sendKeys(Keys.ENTER);
			Thread.sleep(750);
			
			try{
	
					System.out.println("Waiting search output..");
					TaskUtilities.customWaitForElementVisibility("//span[text()='"+searchData+"']", 10);
					System.out.println("Currency is already present..");
					takeScreenshot();
					
					colNum = 8;
					inputs += 1;
					continue outercurrencyloop;
					
				} catch(TimeoutException e){
					//No results found. Skips...
				}
				
			TaskUtilities.customWaitForElementVisibility("//a/img[@title='New']", MAX_TIME_OUT);
			TaskUtilities.jsFindThenClick("//a/img[@title='New']");
			TaskUtilities.fluentWaitForElementInvisibility("//div", "No results found.", MAX_TIME_OUT);
			
			if(is_element_visible("//a[@title='Collapse Search']", "xpath")){
				//TaskUtilities.retryingFindClick(By.xpath("//a[@title='Collapse Search']"));
				TaskUtilities.jsFindThenClick("//a[@title='Collapse Search']");
				TaskUtilities.customWaitForElementVisibility("//a[@title='Expand Search']", MAX_TIME_OUT);
			}
			
			TaskUtilities.customWaitForElementVisibility("//a[@title='Expand']", MAX_TIME_OUT);
			TaskUtilities.jsFindThenClick("//a[@title='Expand']");
			Thread.sleep(5000);
			driver.findElement(By.xpath("//a[@title='Expand']")).sendKeys(Keys.TAB);

			currencyloop:
			while(getExcelData(label, colNum, "text").length()>0){
				labelLocator = getExcelData(label, colNum, "text");
				labelLocator = TaskUtilities.filterDataLocator(labelLocator);
				if(!caughtInputError) labelLocatorPath = TaskUtilities.retryingSearchInput(labelLocator);
				
				if(labelLocatorPath == null){
					labelLocatorPath = "//tr[@_afrrk=0]//label[text()='"+labelLocator+"']/..//input";
					caughtInputError = true;
				}
				
				String type = TaskUtilities.getdataLocatorType(labelLocator);
				dataLocator = getExcelData(inputs, colNum, type);
				
				if(labelLocator.contentEquals("Enabled")){
					if(dataLocator.equalsIgnoreCase("Yes") && is_element_visible("//input[@checked='']", "xpath")){
							//Skips
						} else if(dataLocator.equalsIgnoreCase("No") && is_element_visible("//input[@checked='']", "xpath")){
								TaskUtilities.jsFindThenClick("//input[@type='checkbox']");
						} else {
								TaskUtilities.jsFindThenClick("//input[@type='checkbox']");
						}
					
					colNum += 1;
					continue currencyloop;
				}
				
				TaskUtilities.consolidatedInputEncoder(task, labelLocatorPath, dataLocator);
				
				colNum += 1;
				System.out.println("Adding other inputs...");
			}
			colNum  = 8;
			inputs += 1;
			takeScreenshot();
			TaskUtilities.customWaitForElementVisibility("//button[text()='ave and Close']", MAX_TIME_OUT);
			TaskUtilities.jsFindThenClick("//button[text()='ave and Close']");
			
			TaskUtilities.customWaitForElementVisibility("//div[text()='Manage Currencies']", MAX_TIME_OUT);
			TaskUtilities.jsFindThenClick("//div[text()='Manage Currencies']/../..//a[@title='Go to Task']");
			TaskUtilities.customWaitForElementVisibility("//h1[text()='Manage Currencies']", MAX_TIME_OUT);
			System.out.println("Adding other listed currencies...");
		}
		
		takeScreenshot();
		TaskUtilities.customWaitForElementVisibility("//button[text()='ave and Close']", MAX_TIME_OUT);
		TaskUtilities.jsFindThenClick("//button[text()='ave and Close']");
		TaskUtilities.customWaitForElementVisibility("//button[text()='D']", MAX_TIME_OUT);
		TaskUtilities.jsFindThenClick("//button[text()='D']");
		colNum = 8;
		inputs = 10;
		return true;
	}
	
}
