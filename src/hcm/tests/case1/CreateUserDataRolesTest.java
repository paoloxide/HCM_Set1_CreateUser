package hcm.tests.case1;

import static util.ReportLogger.log;
import static util.ReportLogger.logFailure;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import common.BaseTest;
import common.ExcelUtilities;
import common.TaskUtilities;
import hcm.pageobjects.FuseWelcomePage;
import hcm.pageobjects.LoginPage;
import hcm.pageobjects.TaskListManagerTopPage;

public class CreateUserDataRolesTest extends BaseTest{
	private static final int MAX_TIME_OUT = 30;
	private String searchData, labelLocator, labelLocatorPath, labelTag, dataLocator, type;
	private String groupLabelLocator, nextLabelLocator, nextLabelLocatorPath;
	private boolean hasCreatedDataRoles = false;
	
	private static final int defaultcolNum = 8;
	private static final int defaultinputs = 10;
	private static final int grouplabel = 8;
	
	private String projectName = "Default";
	
	private int projectCol = 8;
	private int label = 9;
	private int inputs = defaultinputs;
	private int colNum = defaultcolNum;
	

	private int projectRowNum = TestCaseRow;
	private int projectSheetcolNum = 7;
	
	@Test
	public void a_test() throws Exception  {
		testReportFormat();
	
	try{	
			
			createUserDataRole();
	  
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

	public void createUserDataRole() throws Exception{
		
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
		
		
		while(!hasCreatedDataRoles && !projectName.isEmpty() && !projectName.contentEquals("")){
			projectName = selectProjectName();
			
			if(projectName.contains("*")){
				projectRowNum += 1;
				continue;
			}
			
			hasCreatedDataRoles = createDataRoles(task);
		}
		
		log("Data Roles has been created..");
		System.out.println("User Data Roles has been created..");
		
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
	
	private boolean createDataRoles(TaskListManagerTopPage task) throws Exception{
		
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
		
		TaskUtilities.customWaitForElementVisibility("//div[text()='Define Implementation Users']", MAX_TIME_OUT);
		
		if(is_element_visible("//div[text()='Define Implementation Users']"+"//a[@title='Expand']", "xpath")){
			//TaskUtilities.jsFindThenClick("//div[text()='Define Implementation Users']"+"//a[@title='Expand']");
			TaskUtilities.retryingFindClick(By.xpath("//div[text()='Define Implementation Users']"+"//a[@title='Expand']"));
			TaskUtilities.customWaitForElementVisibility("//div[text()='Define Implementation Users']"+"//a[@title='Collapse']", MAX_TIME_OUT);
		}
		
		TaskUtilities.customWaitForElementVisibility("//div[text()='Create Data Role for Implementation Users']", MAX_TIME_OUT);
		TaskUtilities.jsFindThenClick("//div[text()='Create Data Role for Implementation Users']/../..//a[@title='Go to Task']");
		
		TaskUtilities.customWaitForElementVisibility("//h1[text()='Manage Data Roles and Security Profiles']", MAX_TIME_OUT);
		
		groupLabelLocator = getExcelData(grouplabel, colNum, "text");
		enterDataRoleData(task);
		return true;
	}
	
	public void enterDataRoleData(TaskListManagerTopPage task) throws Exception{
		
		TaskUtilities.jsFindThenClick("//a/span[text()=' Create']/..");
		TaskUtilities.customWaitForElementVisibility("//h1[contains(text(),'Create Data Role')]", MAX_TIME_OUT);
		
		labelLocator = getExcelData(label, colNum, "text");
		labelLocator = TaskUtilities.filterDataLocator(labelLocator);
		labelLocatorPath = TaskUtilities.retryingSearchInput(labelLocator);
		
		type = TaskUtilities.getdataLocatorType(labelLocator);
		dataLocator = getExcelData(inputs, colNum, type);
		
		labelTag = driver.findElement(By.xpath(labelLocatorPath)).getTagName();
		
		if(!labelTag.contentEquals("select")){
				TaskUtilities.consolidatedInputEncoder(task, labelLocatorPath, dataLocator);
			} else{
				TaskUtilities.consolidatedInputSelector(labelLocatorPath, dataLocator);
			}
		
		nextLabelLocator = getExcelData(label, colNum+1, "text");
		nextLabelLocator = TaskUtilities.filterDataLocator(nextLabelLocator);
		nextLabelLocatorPath = TaskUtilities.retryingSearchInput(nextLabelLocator);
		
		String newgroupLabelLocator = getExcelData(grouplabel, colNum+1, "text");
		if(!newgroupLabelLocator.isEmpty() || !newgroupLabelLocator.contains("")){
			groupLabelLocator = newgroupLabelLocator;
		}
		
		if(nextLabelLocatorPath == null || nextLabelLocatorPath.isEmpty() || nextLabelLocatorPath.contains("")){
			TaskUtilities.jsFindThenClick("//button[text()='Next']");
			TaskUtilities.customWaitForElementVisibility("//h1[contains(text(),'"+groupLabelLocator+"')]", MAX_TIME_OUT);
		}
	}
}
