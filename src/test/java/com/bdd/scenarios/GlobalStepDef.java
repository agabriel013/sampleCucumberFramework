package com.bdd.scenarios;

import java.util.concurrent.TimeUnit;

import com.bdd.testbase.TestBase;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

public class GlobalStepDef extends TestBase {
	/****************************************************************************************
	 * Step Definition: Given browser <browsertype> is open and nvigates to <URL>
	 * @param sBrowserType - the type of browser to be used e.g. IE, Chrome, Mozilla
	 * @param sUrl - the url to be used
	 * @throws Exception
	 ****************************************************************************************/
	@Given("^for TC Name \"([^\"]*)\", browser \"([^\"]*)\" is open and navigates to \"([^\"]*)\"$")
    public void openBrowserUrl(String sTCName, String sBrowserType, String sUrl) throws Exception{
       System.out.println("Login");
       TCName=sTCName;

		openBrowser(sBrowserType);
		
		//check if it has semicolon/delimiter
		if (sUrl.contains(";")) {
		   String[] parts = sUrl.split(";");
	 	    sUrl = parts[0];
		} 

		driver.get(sUrl);
		
		TimeUnit.SECONDS.sleep(5);
		
		ReportResults("PASS", "Succesfully Navigates and Opens the Browser", true);
	}
	
	/****************************************************************************************
	 * Step Definition: And enter <param>
	 * @param sValObjId - the value to be entered in the text and the xpath delimited by semicolon
	 * @throws Exception
	 ****************************************************************************************/
	@And("^enter \"([^\"]*)\"$")
	public void enterValue(String sValObjId) throws Exception{
		
		if (sValObjId.trim().equals("")){
			System.out.println("Enter Text Value: No Parameters defined");
		}else{ 

			//RegistrationPage oRegistration = PageFactory.initElements(this.driver, RegistrationPage.class);
			this.enterText(sValObjId);
		}
	}
	
	/****************************************************************************************
	 * Step Definition: And select <param>
	 * @param sValObjId - the value to be selected in the Radio Button and the xpath delimited by semicolon
	 * @throws Exception
	 ****************************************************************************************/
	@And("^select \"([^\"]*)\"$")
	public void selectRadioButton(String sValObjId) throws Exception{
		if (sValObjId.trim().equals("")){
			System.out.println("Select Radio Button: No Parameters defined");
		}else{
	 
this.selectRadioBtn(sValObjId);
		}
	}
	
	/****************************************************************************************
	 * Step Definition: And check <param>
	 * @param sValObjId - the value to be checked in the option and the xpath delimited by semicolon
	 * @throws Exception
	 ****************************************************************************************/
	@And("^check \"([^\"]*)\"$")
	public void checkBox(String sValObjId) throws Exception{
		if (sValObjId.trim().equals("")){
			System.out.println("Check box: No Parameters defined");
		}else{

			this.checkBox(sValObjId);

		}
	} 
/****************************************************************************************
	 * Step Definition: And choose <param>
	 * @param sValObjId - the value to be checked in the option and the xpath delimited by semicolon
	 * @throws Exception
	 ****************************************************************************************/
	@And("^choose \"([^\"]*)\"$")
	public void chooseDrop_Down(String sObj) throws Exception{
		if (sObj.trim().equals("")){
			System.out.println("No Parameters defined");
		}else{
			this.chooseDropDown(sObj);
		}
	}
	 

/****************************************************************************************
	 * Step Definition: Then click <param>
	 * @param sValObjId - the object xpath to be clicked
	 * @throws Exception
	 ****************************************************************************************/
	@Then("click \"([^\"]*)\"$")
	public void clickBtn(String sObjId) throws Exception{
		System.out.println("Click");

		if (this.clickButton(sObjId)){
			ReportResults("PASS", "Succesfully clicked the object: "+sObjId, true);
		}else{
			ReportResults("FAIL", "Did not succesfully clicked the object: "+sObjId, true);
		}

	} 

/****************************************************************************************
	 * Step Definition: Then validateURL <param>
	 * @param sObjId - the url expected to be displayed in the applications address bar
	 * @throws Exception
	 ****************************************************************************************/
	@Then("validateURL \"([^\"]*)\"$")
	public void validateLinkURL(String sObjId) throws Exception{
		if (sObjId.trim().equals("")){
			System.out.println("No Parameters defined for Validate Url Link");
		}else{
			boolean bOutcome=this.validateUrl(sObjId);
			
			if (bOutcome){
				ReportResults("PASS", "Succesfully navigated to the expected URL", true);
			}else{
				ReportResults("FAIL", "Did not succesfully navigated to the expected URL.", true);
			}
			
		} 

}
	
	
	/****************************************************************************************
	 * Step Definition: Then verify visibility <param>
	 * @param sObjId - boolean: true if object is expected to be visible, false if not and the xpath
	 * of the object.
	 * @throws Exception
	 ****************************************************************************************/
	@Then("^verify visibility \"([^\"]*)\"$")
	public void verifyifObjectisVisible(String sobjId) throws Exception{
		if (isElementVisible(sobjId)){
			ReportResults("PASS", "Expected visibility of object is correct: "+ sobjId, true);
		}
	}
	 
/****************************************************************************************
	 * Step Definition: Then verify visibility <param>
	 * @param sObjId - boolean: true if object is expected to be visible, false if not and the xpath
	 * of the object.
	 * @throws Exception
	 ****************************************************************************************/
	@Then("^verify text \"([^\"]*)\"$")
	public void verifyTextofanObject(String sobjId) throws Exception{
		if (verifyText(sobjId)){
			ReportResults("PASS", "Message is displayed as expected: "+sobjId,true);
		}else{
			ReportResults("FAIL", "Message is not displayed. "+sobjId,true);
		}
			
	} 


/****************************************************************************************
	 * Step Definition: And navigate <Menu>>><Submenu>
	 * @param sObjId - the xpath of the Menu separated by double Greater than sign
	 * @throws Exception
	 ****************************************************************************************/
	@And("^navigate \"([^\"]*)\"$")
    public void navigateToMenu(String sObj) throws Exception{
           if (sObj.trim().equals("")){
                  System.out.println("Navigate Menu: No Parameters defined");
           }else{

                  boolean bOutcome=this.navigateMenu(sObj);
                  if(bOutcome){
                        System.out.println("Successfully navigated");
                        ReportResults("PASS", "Succesfully navigated to object: "+sObj, true);
                  }
                  else{
                        System.out.println("Unsuccessfully navigated");
                        ReportResults("FAIL", "Unsuccessfully navigated to object: "+sObj, true);
                  }
                  
           }
    }
	 

/****************************************************************************************
	 * Step Definition: And navigate <Menu>>Submenu>
	 * @param sObjId - the xpath of the Menu separated by double Greater than sign
	 * @throws Exception
	 ****************************************************************************************/
	@Then("^verify dropdown contents \"([^\"]*)\"$")
	public void verifyDrowdownContents1(String sobjId) throws Exception{
		String strResult = this.verifyDrowdownContents(sobjId);
		if (strResult.equals("InTheDropdown")){
			System.out.println("The value is in the dropdown");
			ReportResults("PASS", "The value is in the dropdown: "+sobjId, true);
		}else if(strResult.equals("NotInTheDropdown")){
			System.out.println("The value is NOT in the dropdown");
			ReportResults("PASS", "The value is NOT in the dropdown: "+sobjId, true);
		}
		else{
			System.out.println("The object is not found");
			ReportResults("FAIL", "Unsuccessfully find the object: "+sobjId, true);
		}
			
	} 

/****************************************************************************************
	 * Step Definition: And go to URL <Address URL>
	 * @param sObjId - the address URL you want to navigate to
	 * @throws Exception
	 ****************************************************************************************/
	@And("^go to URL \"([^\"]*)\"$")
	public void gotoURL(String sUrl) throws Exception{
		//check if it has semicolon/delimiter
		if (sUrl.contains(";")) {
		   String[] parts = sUrl.split(";");
	 	    sUrl = parts[0];
		}
		
		driver.get(sUrl);
		TimeUnit.SECONDS.sleep(5);
	}
	 

/****************************************************************************************
	 * Step Definition: Then double click <param>
	 * @param sValObjId - the object xpath to be clicked
	 * @throws Exception
	 ****************************************************************************************/
	@Then("double click \"([^\"]*)\"$")
	public void dblclickBtn(String sObjId) throws Exception{
		System.out.println("Double Click");

		if (this.doubleclickButton(sObjId)){
			ReportResults("PASS", "Succesfully double clicked the object: "+sObjId, true);
		}else{
			ReportResults("FAIL", "Did not succesfully double clicked the object: "+sObjId, true);
		}
		
		

	}
	
		
	@And("validate sort \"([^\"]*)\"$")
	public void validate_sorting(String sObjId) throws Exception{
		this.validateUrl(sObjId);
	}

	
	
	
	
	

}
