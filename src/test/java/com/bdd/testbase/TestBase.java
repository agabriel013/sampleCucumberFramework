package com.bdd.testbase;

//This are all the imports used for the testbase class

import io.github.bonigarcia.wdm.FirefoxDriverManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
 
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys; 
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import com.relevantcodes.extentreports.*;
 
public class TestBase {
     //declare all variables in the fields
     public static WebDriver driver = null;
     public static Properties Config = null;
     public static WebDriverWait wait30 = null;
     public static ExtentReports rpt = null;
     public static ExtentTest rpt_test = null;
     public static String FolderName = null;
     public static Logger logger = null;
     public static boolean isBrowserOpen = false;
     public static String BrowserType = null;;
     public static Actions act = null;
     public static String TCName = null;
     
     public static int iResult = 0; 
 
public void initialize() throws Exception{
           
           driver = null;
           
           Config = new Properties();
           FileInputStream ipEnv = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\java\\com\\acn\\config\\Environment.properties");
           Config.load(ipEnv);
           
           DateFormat screenshotFormat = new SimpleDateFormat("MM-dd-HH-mm-ss");
           Date date = new Date();
           FolderName = "Run_Results_" + TCName + "_" + screenshotFormat.format(date);
           
           //rpt = new ExtentReports(System.getProperty("user.dir")+"/target/"+FolderName+"/Report.html");
           rpt = new ExtentReports(System.getProperty("user.dir")+"\\target\\"+FolderName+"\\Report.html");
           
           rpt_test = rpt.startTest(this.getClass().getSimpleName()+".feature",""); 
           PropertyConfigurator.configure(System.getProperty("user.dir")+"\\src\\test\\java\\com\\acn\\config\\log4j.properties");
           logger =  Logger.getLogger("devpinoyLogger");
           
           
           isBrowserOpen = true;
           
           
           
     }
     
                
/******************************************************************************
      * @author EF9749
      * @param None
      * @return
      * Description: This is global variable in opening the browser
      * @throws Exception  
*****************************************************************************/     
 public void openBrowser(String sBrowserType) throws Exception{
           
           
           initialize();
          
           this.BrowserType = sBrowserType;
           
           try{
                
                if (sBrowserType.equals("")){
                     sBrowserType = this.Config.getProperty("env_browserType");
                }
                
                if (sBrowserType.contains("IE")){
                System.setProperty("webdriver.ie.driver",System.getProperty("user.dir")+"/webdriver/IEDriverServer.exe");
                     driver = new InternetExplorerDriver(); 
} else if(sBrowserType.contains("Chrome")){
                     System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/webdriver/chromedriver.exe");
                     
                     ChromeOptions options = new ChromeOptions();
                     options.addArguments("test-type");
                     options.addArguments("chrome.switches", "--disable-extensions");
                     options.addArguments("start-maximized");
                     
                     driver = new ChromeDriver(options);
                     
                     act = new Actions(driver);
                     
                } else if(sBrowserType.contains("Mozilla")){
                     
                     //FirefoxDriverManager.getInstance().setup();
                     
                     System.setProperty("webdriver.firefox.marionette", System.getProperty("user.dir")+"/webdriver/geckodriver.exe");  
driver = new FirefoxDriver();
                     
                } else{
                     System.out.println("Error Browser Type: "+sBrowserType+"");
                }
                
                
                driver.manage().window().maximize();
                driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
                
                wait30 = new WebDriverWait(driver, 30);
           
           
           } catch (Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured: " + e.getMessage(), true);
                System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           }
            
     
     }
     
/******************************************************************************
      * @author EF9749
      * @param None
      * @return
      * Description: Closes the browser
      * @throws Exception 
      *****************************************************************************/     
     public void closeBrowser() throws Exception{
           try{
                
                driver.quit();
                System.out.println("Browser closed"); 
 
           } catch (Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured in closeBrowser(): " + e.getMessage(), true);
                System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           }
     }
     
/******************************************************************************
      * @author EF9749
      * @param None
      * @return
      * Description: Standard class of reporting Pass or Fail status of every step
      * @throws Exception 
      *****************************************************************************/     
public void ReportResults(String sResult, String sMessage, boolean bTakeScreenshot) throws Exception{
           
           
           DateFormat screenshotFormat = new SimpleDateFormat("MM-dd-HH-mm-ss");
           Date date = new Date();
           
           
           String sScreenshot = this.getClass().getSimpleName() + screenshotFormat.format(date);
           
           try{
           
           
           if (sResult.equalsIgnoreCase("PASS")){
                iResult = 1;
           }else if(sResult.equalsIgnoreCase("FAIL")){
                iResult = 2;
           }else if(sResult.equalsIgnoreCase("DONE")){
                iResult = 3;
           }else if(sResult.equalsIgnoreCase("TESTNAME")){
                iResult = 4; 
}
           
           rpt_test.setDescription(TCName);
           
           
           switch(iResult){
           
           case 1:
                
                Reporter.log("PASS: "+ sMessage+ ";\n", true); 
 
                System.out.println(("PASS: "+ sMessage));
                
                if (bTakeScreenshot){
                     this.takeScreenshot(sScreenshot, FolderName);
                     rpt_test.log(LogStatus.PASS, sMessage + rpt_test.addScreenCapture(sScreenshot+".png"));
                     
                }else{
                     rpt_test.log(LogStatus.PASS, sMessage);
                }
                
                 
break;
           case 2:
                Reporter.log("FAIL: "+ sMessage+ ";\n", true);
                System.out.println("FAIL: "+ sMessage);
                
                if (bTakeScreenshot){
                     this.takeScreenshot(sScreenshot, FolderName);
                     
                     
                     rpt_test.log(LogStatus.FAIL, sMessage + rpt_test.addScreenCapture(sScreenshot+".png"));
                }else{
                     rpt_test.log(LogStatus.FAIL, sMessage);
                }
                
                rpt.endTest(rpt_test);
                rpt.flush();
                Assert.fail("FAIL: "+ sMessage);
                
                break; 
 
           case 3:
                Reporter.log("DONE:"+ sMessage + ";\n", true);
                System.out.println("DONE: "+ sMessage);
                rpt.endTest(rpt_test);
                rpt.flush();
                break;
           case 4:
                Reporter.log("TestName: "+this.getClass().getSimpleName()+"\n",true);
                System.out.println("TestName: "+this.getClass().getSimpleName());
                break;
                
           default: Reporter.log("Please enter correct values to the parameters");
           
           }
            
rpt.flush();
           
           } catch(Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured in ReportResults(): " + e.getMessage(), true);
                System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           }
     }
     
     
/****************************************************************************** 
 
      * @author EF9749
      * @param None
      * @return
      * Description: Check if elements exist in the screen
      * @throws Exception 
      *****************************************************************************/     
     public boolean isElementExist(By by){
           try{
                this.driver.findElement(by);
                System.out.println("Elements Exist");
                return true;
           }catch(Exception e){
                return false;
           }
     } 
 
     
/*******************************************************************************
      * @author EF9749
      * @param fileName - name of the image file
      * @param folderName - folder where the image file will be saved
      * @throws Exception
      */
     public void takeScreenshot(String fileName, String folderName) throws Exception{
           try{
                File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile,  new File(System.getProperty("user.dir")+"/target/"+folderName+"/"+fileName+".png"));
           }catch(Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured in takeScreenshot(): " + e.getMessage(), true);
                System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           } 
                
     }
     
/*******************************************************************************
      * @author EF9749
      * @Description - This gets the title of the current web page
      * @throws Exception
      */
     public String getPageTitle() throws Exception{ 
 
           
           List<WebElement> sPage = driver.findElements(By.xpath("//title"));
           
           String sTitle = null;
           
           try{
           
                if (sPage.size()>0){
                     sTitle = sPage.get(0).getText();
                }
           
           }catch(Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured in takeScreenshot(): " + e.getMessage(), true);
                System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           }
           
           return sTitle;
     }
     
     /*******************************************************************************
      * @author EF9749
      * @Description - This is the generic script for Entering text to a text field
      * @param sValueObjectId - this contains the Value to be filled up in the textbox and the object id/xpath of the textfield; e.g. "Gabriel;//input[@id='firstName']" 
      * @throws Exception
      *******************************************************************************/
     public void enterText(String pValueObjectId) throws Exception{
        
        String sValueObject = pValueObjectId;
        String sValue = null;
        String ObjId = null;
        String frame = null;
        
        try{ 
 
           
        
        if (sValueObject.contains(";")) {
                
                String[] parts = sValueObject.split(";");
              sValue = parts[0];
              ObjId = parts[1]; 
              System.out.println(sValue);
              System.out.println(ObjId);
            
              
           } else {
                ReportResults("FAIL", "Enter Text - The parameter "+ sValueObject +"", true);
           }
        
       switchTo(pValueObjectId);
              
        wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
        
        List<WebElement> oList = driver.findElements(By.xpath(ObjId));
        
        //TimeUnit.SECONDS.sleep(2);
           
                if (oList.size()>0){
                     oList.get(0).click();
                     oList.get(0).clear();
                     oList.get(0).sendKeys(sValue);
                     ReportResults("PASS", "Successfully entered text: "+sValue+" on this object: "+ObjId, true);
                }else{
                     ReportResults("FAIL", "Enter Text - The Object: " + ObjId +" does not exist", true);
                }
                
                driver.switchTo().defaultContent(); 
 
                
                
        }catch(Exception e){
              ReportResults("FAIL", "Unexpected error/exception occured in enterText(): " + e.getMessage(), true);
              System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
        }
     }
     
     
/**********************************************************************************
      * @author EF9749
      * @Description - This is the generic script for clicking an object (button, links, images, labels etc.)
      * @param sValueObjectId - this contains the object id/xpath of the button/links/images/labels etc; e.g. "//button[@id='Submit']" 
      * @throws Exception
      *******************************************************************************/
     public boolean clickButton(String sObjId) throws Exception{
           
           String sValue=null;
           String ObjId=null;
           String frame =null;
           
           boolean bOutcome= false;
           
           try{
                
           
           
           if (sObjId.contains(";")) {
                
                String[] parts = sObjId.split(";");
              sValue = parts[0];
              ObjId = parts[1]; 
              System.out.println(sValue);
              System.out.println(ObjId);
            
               
 
           }else{
                ObjId= sObjId;
           }
           
           //Check if needed to switch to a frame or a window
           switchTo(sObjId);
          
         wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
       wait30.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ObjId)));
            
           List<WebElement> oList = this.driver.findElements(By.xpath(ObjId));
           
           
           if (oList.size()>0){
                //before clicking it, ensure that it is displayed in the screen
//              Actions actions = new Actions(driver);
//              actions.moveToElement(oList.get(0)).click(); 
 
//              actions.perform();
                
                scrollToElement(oList.get(0));
                
                //this.scrollAndClick(oList.get(0));
                this.winHandleBefore = this.driver.getWindowHandle();
                //this.changeWindow();
                oList.get(0).click();
                
                bOutcome = true;
           }else{
                ReportResults("FAIL", "Object - The Object: " + ObjId +" does not exist", true);
           }
           
           
           driver.switchTo().defaultContent();
           
           //TimeUnit.SECONDS.sleep(20);
           
           
           
           }catch(Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured in clickButton(): " + e.getMessage(), true);
                System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           }
           
           return bOutcome;
           
     }
     
 
/**********************************************************************************
      * @author EF9749
      * @Description - This is the generic script for checking if element exists
      * @param sValueObjectId - this contains the object id/xpath of the button/links/images/labels etc; e.g. "//button[@id='Submit']" 
      * @throws Exception
      *******************************************************************************/
     public boolean isElementVisible(String sObjId) throws Exception{
           
           String sValue=null;
           String ObjId=null;
           String frame =null;
           
           boolean bOutcome= false;
           
           try{
            
 
           //count number of semicolons in the parameter
        int iCnt = StringUtils.countMatches(sObjId, ";");
        System.out.println("Number of delimiter: "+iCnt+"");
           
           if (sObjId.contains(";")) {
                
                String[] parts = sObjId.split(";");
              sValue = parts[0];
              ObjId = parts[1]; 
              System.out.println(sValue);
              System.out.println(ObjId);
              
            
           }else{
                ObjId= sObjId;
           }
           
            switchTo(sObjId);
           
          
       //wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
           
           if (sValue.equalsIgnoreCase("true")){
                
                  wait30.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ObjId)));
                
                System.out.println("Object is visible: "+sObjId);
           }else{
           wait30.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(ObjId)));
                System.out.println("Object is not visible: "+sObjId);
           }
            
           List<WebElement> oList = driver.findElements(By.xpath(ObjId));
           
           if (oList.size()>0){
                bOutcome = true;
           }else{
                ReportResults("FAIL", "Object Visibility - The Object: " + ObjId +" does not exist", true);
           }
           
           
           driver.switchTo().defaultContent();
           
           }catch(Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured in isElementVisible(): " + e.getMessage(), true);
                System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           }
           
           return bOutcome;
                   
     }
 
/*******************************************************************************
      * @author EF9749
      * @Description - This is the generic method for verifying a text in the screen
      * @param sValueObjectId - this contains the object id/xpath of the button/links/images/labels etc; e.g. "//button[@id='Submit']" 
      * @throws Exception
      *******************************************************************************/
     public boolean verifyText(String sValueObjectId) throws Exception{
              
           TimeUnit.SECONDS.sleep(5);
           boolean bResult = false;
           
              String string = sValueObjectId;
              String sValue = null;
              String ObjId = null;
              String frame = null; 
              
            //count number of semicolons in the parameter
             int iCnt = StringUtils.countMatches(sValueObjectId, ";");
             System.out.println("Number of delimiter: "+iCnt+"");
             
             try{
             
              
              if (string.contains(";")) {
                   String[] parts = string.split(";");
              sValue = parts[0];
              ObjId = parts[1]; 
              System.out.println(sValue);
              System.out.println(ObjId);
              
                  
              
                } else {
                     
                     ObjId = sValueObjectId;
                     
                }
              
              
              switchTo(sValueObjectId);
              
              
              wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
              
              List<WebElement> oList = driver.findElements(By.xpath(ObjId));
           
                if (oList.size()>0){
                     //added by justin
                     scrollToElement(oList.get(0));
                     String errMsg = oList.get(0).getText();
                     String s= "\n";
                     if (errMsg.contains(s)){
                           errMsg = errMsg.replace("\n", " ");
                     }
                     System.out.println("Actual msg: "+errMsg);
                     System.out.println("Expected msg: "+sValue);
                     
                     //try to change .contains to .equals
                     if (errMsg.equals(sValue)){
                           bResult = true;
                     }else{
                           bResult = false;
                     }
                      
 
                }else{
                     ReportResults("FAIL", "Object does not exist", true);
                }
                
                driver.switchTo().defaultContent();
             }catch(Exception e){
                  ReportResults("FAIL", "Unexpected error/exception occured in verifyText(): " + e.getMessage(), true);
                  System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
             }
                
                return bResult;
 
     }
     
/*******************************************************************************
      * @author EF9749
      * @Description - This is the generic method for selecting a radio button
      * @param sValueObjectId - this contains the object id/xpath of the radio button 
      * @throws Exception
      *******************************************************************************/
     public void selectRadioBtn(String sValueObjectId) throws Exception{
        
        String string = sValueObjectId;
        String sValue = null;
        String ObjId = null;
        String frame = null;
        
       //count number of semicolons in the parameter
        int iCnt = StringUtils.countMatches(sValueObjectId, ";");
        System.out.println("Number of delimiter: "+iCnt+"");
        
        try{
        
        if (string.contains(";")) {
              String[] parts = string.split(";");
         sValue = parts[0];
         ObjId = parts[1]; 
         System.out.println(sValue);
         System.out.println(ObjId);
         
         
      
 
         
           } else {
                ObjId = sValueObjectId;
          }
         
 
       switchTo(sValueObjectId);
       
      wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
        
        List<WebElement> oList = this.driver.findElements(By.xpath(ObjId));
           
                if (oList.size()>0){
                     oList.get(0).click();
                }else{
                     ReportResults("FAIL", "Object does not exist", true);
                }
                
                
                driver.switchTo().defaultContent();
                
        }catch(Exception e){
             ReportResults("FAIL", "Unexpected error/exception occured in selectRadioBtn(): " + e.getMessage(), true);
            System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
        }
                  
}
     
/*******************************************************************************
      * @author EF9749
      * @Description - This is the generic method for choosing an item in the dropdown
      * @param sValueObjectId - this contains the object id/xpath of the dropdown 
      * @throws Exception
      *******************************************************************************/
 
     public void chooseDropDown(String sValueObjectId) throws Exception{
           
            String string = sValueObjectId;
              String sValue = null;
              String ObjId = null;
              String frame= null;
              
              //count number of semicolons in the parameter
              int iCnt = StringUtils.countMatches(sValueObjectId, ";");
              System.out.println("Number of delimiter: "+iCnt+"");
              
              try{
              
              if (string.contains(";")) {
                        String[] parts = string.split(";");
                    sValue = parts[0];
                    ObjId = parts[1]; 
                    System.out.println(sValue);
                    System.out.println(ObjId); 
 
             
              
                } else {
                     //do nothing
                }
              
              switchTo(sValueObjectId);
           
              wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
              
              List<WebElement> oList = this.driver.findElements(By.xpath(ObjId));
                
                if (oList.size()>0){
                     Select select = new Select(oList.get(0));
                     
                     select.selectByVisibleText(sValue);
                }else{
                     ReportResults("FAIL", "Object does not exist", true);
                }
                
                driver.switchTo().defaultContent();
              }catch(Exception e){
                   ReportResults("FAIL", "Unexpected error/exception occured in chooseDropdown(): " + e.getMessage(), true);
                   System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
              }
           
     }
 
/*******************************************************************************
      * @author EF9749
      * @Description - This is the generic method for checking an item in the checkbox
      * @param sValueObjectId - this contains the object id/xpath of the checkbox
      * @throws Exception
      *******************************************************************************/
     public void checkBox(String sValueObjectId) throws Exception{
           
            String string = sValueObjectId;
          String sValue = null;
          String ObjId = null;
          String frame= null;
           
 
          //count number of semicolons in the parameter
             int iCnt = StringUtils.countMatches(sValueObjectId, ";");
             System.out.println("Number of delimiter: "+iCnt+"");
             
             try{
              
              if (string.contains(";")) {
                     String[] parts = string.split(";");
                    sValue = parts[0];
                    ObjId = parts[1]; 
                    System.out.println(sValue);
                    System.out.println(ObjId);
                    
                 
               
                } else {
                     ObjId = sValueObjectId;
                }
              
              switchTo(sValueObjectId);
              
              wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
              
              List<WebElement> oList = this.driver.findElements(By.xpath(ObjId));
                
                if (oList.size()>0){
                     if (! isChecked(oList.get(0))){
                          oList.get(0).click();
                     }
                     
                }else{
                     ReportResults("FAIL", "Object does not exist", true);
                }
                
                driver.switchTo().defaultContent();
                
                oList = null;
                
             }catch(Exception e){
                  ReportResults("FAIL", "Unexpected error/exception occured in checkBox(): " + e.getMessage(), true);
                  System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
             }
     }
     
/*******************************************************************************
      * @author EF9749
      * @Description - Check if a specific object is "Checked"
      * @param oChck - the WebElement object
      * @throws Exception
      *******************************************************************************/
     public boolean isChecked(WebElement oChck) throws Exception{
           
           String isChecked = oChck.getAttribute("value");
           
           boolean bState = false;
           
           try{
           
                if (isChecked.toLowerCase().equals("true")){
                     bState = true;
                
                }else{
                     bState = false;
                }
           }catch(Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured in isChecked(): " + e.getMessage(), true);
                System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           }
           
           return bState;
     }
     
 
/*******************************************************************************
      * @author EF9749
      * @Description - Check if the application has Navigated to the expected URL.
      * @param oChck - the WebElement object
      * @throws Exception
      *******************************************************************************/
     public boolean validateUrl(String sValueObjectId) throws Exception{
           
           String sValue = null;
         String ObjId = null;
         
         boolean bState=false;
           
         try{ 
 
         if (sValueObjectId.contains(";")) {
                String[] parts = sValueObjectId.split(";");
              sValue = parts[0];
              ObjId = parts[1]; 
              System.out.println(sValue);
              System.out.println(ObjId);
 
           } else {
                ObjId = sValueObjectId;
           }
         
         TimeUnit.SECONDS.sleep(25);
         
         String sUrl = driver.getCurrentUrl();
         System.out.println("Actual: " + sUrl);
         System.out.println("Expected: "+sValue);
         
         if (sUrl.contains(sValue)){
         bState = true;
         }else{
         bState = false;
         }
                
         }catch(Exception e){
         ReportResults("FAIL", "Unexpected error/exception occured in isChecked(): " + e.getMessage(), true);
         System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
         }
         
         return bState;
     }
 
     
     
/*******************************************************************************
      * @author EF9749 
      * @Description - Scrolls to the element (pagedown) until it becomes visible 
      * @param oElement - the WebElement object
      * @throws Exception
      *******************************************************************************/
     public void scrollToElement(WebElement oElement) throws Exception {
           
           boolean oVisible = oElement.isDisplayed();
                     
           try{
           
//         while (oVisible=false){
//             action.sendKeys(Keys.PAGE_DOWN);
//         }
           
           TimeUnit.SECONDS.sleep(1);
           Actions actions = new Actions(driver);
           actions.moveToElement(oElement);
           actions.perform();
           
           }catch(Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured in scrollToElement(): " + e.getMessage(), true);
         System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           }
           
     }
     
/*******************************************************************************
      * @author EF9749
      * @Description - Scrolls to the element and clicks on it
      * @param oElement - the WebElement object
      * @throws InterruptedException 
      * @throws Exception
      *******************************************************************************/
     
     public void scrollAndClick(WebElement oElement) throws Exception
     { 
 
        int elementPosition = oElement.getLocation().getY();
        
        try{
        
        String js = String.format("window.scroll(0, %s)", elementPosition);
        ((JavascriptExecutor)driver).executeScript(js);
        oElement.click();
        
        }catch (Exception e){
              ReportResults("FAIL", "Unexpected error/exception occured in isChecked(): " + e.getMessage(), true);
              System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
        }
     }    
     
     
     
/*******************************************************************************
      * @author EF9749
      * @Description - this switches to a frame, a window or an alert 
      * @param sParam - string containing the window or alert or a frame
      * @throws InterruptedException 
      * @throws Exception
      *******************************************************************************/
     
     public void switchTo(String sValueObjectId) throws Exception{
            
              String string = sValueObjectId;
              String sValue = null;
              String ObjId = null;
              String winframealert= null;
              
              //count number of semicolons in the parameter
              int iCnt = StringUtils.countMatches(sValueObjectId, ";");
              System.out.println("Number of delimiter: "+iCnt+"");
              
              
              try{
             
              if (string.contains(";")) {
                     String[] parts = string.split(";");
                    sValue = parts[0];
                    ObjId = parts[1]; 
                    System.out.println(sValue);
                    System.out.println(ObjId);
                    
                  //why 3?? --> because there's an added logic to include the parameter name in the test data in 
                  //in the feature file. if there are 3 semicolon delimitere it means it has a frame or window 
                  if(iCnt==3){
                        
                           winframealert = parts[2];
                           System.out.println(winframealert.substring(6));
                           
                    System.out.println("Object has Window/Frame/Alert: " + ObjId);
                    
                    if (winframealert.toLowerCase().contains("frame")){
                         this.driver.switchTo().frame(winframealert.substring(6));
                                System.out.println("Switch Frame");
                    } else if(winframealert.toLowerCase().contains("window")){ 
 
                                                      
                           // Switch to new window opened
                           for(String winHandle : driver.getWindowHandles()){
                               driver.switchTo().window(winHandle);
                           }
//                         winHandleBefore = this.driver.getWindowHandle();
//                         try{
//                         this.changeWindow();
//                         }catch(Exception e){}
                           
                           System.out.println("Switch Windows");
                           
                    } else {
                           System.out.println("Nothing to switch to");
                    }
                    
                    
                   }
                  
              
                } else {
                     //do nothing
                }
             
              }catch(Exception e){
                   ReportResults("FAIL", "Unexpected error/exception occured in switchTo(): " + e.getMessage(), true);
               System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
              }
     }
     
     
     //Added by Justin
     public String winHandleBefore = null;
     
     public void changeWindow() throws Exception{
                
           
           Thread.sleep(5000);
           Set<String> handle = driver.getWindowHandles();
           
           System.out.println("Parent Window Handle --> " + winHandleBefore);
           
           try{
           
           if(handle.size() > 1){
                System.out.println("Set handle values: "+ handle);
                System.out.println("Size of handle: " + handle.size());
                System.out.println("");
                String strArray[] = new String[handle.size()];
//              handle.remove(winHandleBefore);
                String strWindow = null;
                int iCtr = 0;
                for(String window : handle){    
                     System.out.println("Window Handle --> " + window);
                     strArray[iCtr]=window;
                     iCtr+=1;
//                   if(iCtr==handle.size()-1){
//                         driver.switchTo().window(window);
//                   }
                }//for
                System.out.println("Last Window Open: " + strArray[handle.size()-1]);
                Thread.sleep(3000);
                
                //driver.close();
                //driver.switchTo().window(winHandleBefore);
                
           }//if
           
           }catch(Exception e){
                ReportResults("FAIL", "Unexpected error/exception occured in changedWindow(): " + e.getMessage(), true);
         System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
           }
     
}
     
public boolean navigateMenu(String sValueObjectId) throws Exception{
        
       String string = sValueObjectId;
       String sValue = null;
       String ObjId = null;
       String frame = null;
       boolean blnReturn = false;
       
       //count number of semicolons in the parameter
       int iCnt = StringUtils.countMatches(sValueObjectId, ";");
       System.out.println("Number of delimiter: "+iCnt+"");
       
       try{
       
        if (string.contains(";")) {
         String[] parts = string.split(";");
         sValue = parts[0];
         ObjId = parts[1]; 
         System.out.println(sValue);
         System.out.println(ObjId);
         
                 
         
            } else {
                  ObjId = sValueObjectId;
                  
            }
 
        switchTo(sValueObjectId);
        
         if(ObjId.contains(">>")){
                   
                   String[] strObj = ObjId.split(">>");
                   
                   for(int i = 0; i < strObj.length; i++){
                         
                         wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(strObj[i])));
                         wait30.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(strObj[i])));
                         
                          List<WebElement> oList = this.driver.findElements(By.xpath(strObj[i]));
                         
                          if (oList.size()>0){
                                
                                if(i==strObj.length-1){
                                        Actions act = new Actions(driver);
                                        
                                        //act.moveToElement(oList.get(0)).click().perform();
                                       oList.get(0).click();
                                        //act.moveToElement(oList.get(0));
                                        //act.clickAndHold();
                                        //TimeUnit.SECONDS.sleep(1);
                                        //act.release();
                                        //act.perform();
                                        
                                       blnReturn = true;
                                       
                                       TimeUnit.SECONDS.sleep(1);
                                } 
                                else{
                                       Actions builder = new Actions(driver);
                                       // Move cursor to the Main Menu Element
                                       builder.moveToElement(oList.get(0));
                                       //builder.click();
                                       //builder.clickAndHold();
                                       //builder.release();
                                       builder.perform();
                                       //blnReturn = true;
                                       //TimeUnit.SECONDS.sleep(1);
                                }
                                       
                          }
                         else{
                                 ReportResults("FAIL", "Object does not exist", true);
                          }
                   }//for(int i
                   
            }//if(ObjId
        
            else{
                  wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
                  wait30.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ObjId)));
                  
                   List<WebElement> oList = this.driver.findElements(By.xpath(ObjId));
                  if (oList.size()>0){
                         Actions builder = new Actions(driver);
                         // Move cursor to the Main Menu Element
                         //builder.moveToElement(oList.get(0),10,10);
                         builder.moveToElement(oList.get(0),10,10);
                         builder.clickAndHold();
                         builder.release();
                         builder.perform();
                         //builder.click(oList.get(0)).perform();
                         //System.out.println("This is oList.get(0) value: " + oList.get(0));
                         blnReturn = true;
                  }
                  else{
                          ReportResults("FAIL", "Object does not exist", true);
                   }
           }
           driver.switchTo().defaultContent();
           
       }catch(Exception e){
       ReportResults("FAIL", "Unexpected error/exception occured in navigateMenu(): " + e.getMessage(), true);
            System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
       }
            return blnReturn;                                 
}//navigate
 
     /*******************************************************************************
      * @author EE7658
      * @Description - This is the generic method for verifying an item in the dropdown
      * @param sValueObjectId - this contains the object id/xpath of the dropdown 
      * @throws Exception
      *******************************************************************************/
 
public String verifyDrowdownContents(String sValueObjectId) throws Exception{
    String string = sValueObjectId;
    String sValue = null;
    String ObjId = null;
    String frame= null;
    String blnCheckValue = "NotInTheDropdown";
    //count number of semicolons in the parameter
    int iCnt = StringUtils.countMatches(sValueObjectId, ";");
    System.out.println("Number of delimiter: "+iCnt+"");
    
    try{
        
        if (string.contains(";")) {
                     String[] parts = string.split(";");
                sValue = parts[0];
                ObjId = parts[1]; 
                System.out.println(sValue);
                System.out.println(ObjId);
         
         } 
        else {
       ObjId = sValueObjectId;
        }
 
        switchTo(sValueObjectId);
     
        wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
        
        List<WebElement> oList = this.driver.findElements(By.xpath(ObjId));
        //WebElement mySelectElm = driver.findElement(By.id("mySelectID")); 
        //Select mySelect= new Select(mySelectElm);
        
        
            if (oList.size()>0){
                   //Added by Justin 01102017
                //((JavascriptExecutor)driver).executeScript("window.scrollTo(0,"+oList.get(0).getLocation().y+")");
                   ((JavascriptExecutor)driver).executeScript("window.scrollTo(0,"+oList.get(0).getLocation().x+")");
                   
                   scrollToElement(oList.get(0));
                   //up to this 01102017
                   
                   Select select = new Select(oList.get(0));
                   List<WebElement> options = select.getOptions();
                   oList.get(0).click();
            System.out.println("The value to be checked in the dropdown: " + sValue);                        
                   System.out.println("Dropdown Values: ");
                   //System.out.println(sValue);
                   for (WebElement option : options) {
                         System.out.println(option.getText());
                   }
                   for (WebElement option : options) {
                   if(option.getText().trim().toString().equals(sValue.trim())){
                    //System.out.println("true");
                    blnCheckValue="InTheDrowpdown";
                    break;
                   }
                    } 
 
            }else{
                   ReportResults("FAIL", "Object does not exist", true);
                   blnCheckValue="Failed";
            }
            
 
            
            driver.switchTo().defaultContent();
            
  }catch(Exception e){
    ReportResults("FAIL", "Unexpected error/exception occured in verifyDropdownContents(): " + e.getMessage(), true);
          System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
  }
        return blnCheckValue;
                      
  }//verify a value in a dropdown if existing   
     
     
/*******************************************************************************
      * @author EE7658
      * @Description - This is the generic method for hitting Enter
      * @param  
      * @throws Exception
      *******************************************************************************/
     public void hitSelect() throws Exception{
                try{
                     driver.switchTo().activeElement().sendKeys(Keys.SPACE);
                }catch(Exception e){
                     ReportResults("FAIL", "Unexpected error/exception occured in hitSelect(): " + e.getMessage(), true);
               System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
                }
           
     }
     
     public boolean selectRx(String sObjId) throws Exception{
           
     String sValue=null;
     String ObjId=null;
     String frame =null;
     
     boolean bOutcome= false;
           
     try{ 
           if (sObjId.contains(";")) {
                
                String[] parts = sObjId.split(";");
              sValue = parts[0];
              ObjId = parts[1]; 
              System.out.println(sValue);
              System.out.println(ObjId);
            
              
           }else{
                ObjId= sObjId;
           }
            
 
           //Check if needed to switch to a frame or a window
           switchTo(sObjId);
          
         wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
       wait30.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ObjId)));
            
           List<WebElement> oList = this.driver.findElements(By.xpath(ObjId));
           
           
           if (oList.size()>0){
                //before clicking it, ensure that it is displayed in the screen
//              Actions actions = new Actions(driver);
//              actions.moveToElement(oList.get(0)).click();
//              actions.perform();
                
                scrollToElement(oList.get(0)); 
//this.scrollAndClick(oList.get(0));
                                
                oList.get(0).click();
                
                bOutcome = true;
           }else{
                ReportResults("FAIL", "Object - The Object: " + ObjId +" does not exist", true);
           }
           
           
           driver.switchTo().defaultContent();
           
           //TimeUnit.SECONDS.sleep(20);
     }catch(Exception e){
           ReportResults("FAIL", "Unexpected error/exception occured in selectRx(): " + e.getMessage(), true);
    System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
     }
           return bOutcome;
                   
     }
 
     
/**********************************************************************************
 * @author EF9749
 * @Description - This is the generic script for double clicking an object (button, links, images, labels etc.)
 * @param sValueObjectId - this contains the object id/xpath of the button/links/images/labels etc; e.g. "//button[@id='Submit']" 
 * @throws Exception
 *******************************************************************************/
public boolean doubleclickButton(String sObjId) throws Exception{
     
     String sValue=null;
     String ObjId=null;
     String frame =null;
     
     boolean bOutcome= false;
     
     try{
     
                if (sObjId.contains(";")) {
                     
                     String[] parts = sObjId.split(";");
                    sValue = parts[0];
                    ObjId = parts[1]; 
                    System.out.println(sValue);
                    System.out.println(ObjId);
                  
                    
                }else{
                     ObjId= sObjId;
                }
                
                //Check if needed to switch to a frame or a window
                switchTo(sObjId);
                
               wait30.until(ExpectedConditions.presenceOfElementLocated(By.xpath(ObjId)));
             wait30.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(ObjId)));
                 
                List<WebElement> oList = this.driver.findElements(By.xpath(ObjId)); 
 
                if (oList.size()>0){
                     //before clicking it, ensure that it is displayed in the screen
                     Actions actions = new Actions(driver);
                     actions.moveToElement(oList.get(0)).doubleClick();
                     actions.perform();
                     
                     //scrollToElement(oList.get(0));
                     
                     //this.scrollAndClick(oList.get(0));
                     this.winHandleBefore = this.driver.getWindowHandle();
                     //this.changeWindow();
                     //oList.get(0).click();
                     
                     bOutcome = true;
                }else{
                     ReportResults("FAIL", "Object - The Object: " + ObjId +" does not exist", true);
                }
                
                
                driver.switchTo().defaultContent();
                
                //TimeUnit.SECONDS.sleep(20);
     }catch(Exception e){
           ReportResults("FAIL", "Unexpected error/exception occured in doubleclickButton(): " + e.getMessage(), true);
    System.out.println(e.getMessage()+ "/n" + e.getStackTrace());
     }
     return bOutcome;
              
}
 
	public void validatesorting(String sObjId) throws Exception{
	     
	     
	     
	}
 

}




