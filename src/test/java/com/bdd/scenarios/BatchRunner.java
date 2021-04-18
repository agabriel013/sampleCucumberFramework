package com.bdd.scenarios;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import cucumber.api.testng.AbstractTestNGCucumberTests;

//This is a test comment
//@RunWith(Cucumber.class)
//@cucumber.api.CucumberOptions(
//		format ={"pretty", "html:cucumber_reports/html/","json:cucumber_reports/json/output.json",
//				 "junit:cucumber_reports/xml/output.xml"},
//		monochrome=true,
//		features = { 
//				//"src/test/java/com/esi/b2c/scenarios/feature/US32124_ChangeRegisterNowLinkToGoToNewRegPage.feature"}
//				"src/test/java/com/esi/b2c/scenarios/feature/Test"}
//				//"src/test/java/com/esi/b2c/scenarios/feature/TestCreateb.feature"}
//				//"src/test/java/com/esi/b2c/scenarios/feature/US33135_FailedRegistrationDueToMissingFirstName.feature"}
//		,glue={"com.esi.b2c.scenarios"})

//@RunWith(Cucumber.class) 
//@CucumberOptions(
//		format ={"pretty", "html:cucumber_reports/html/","json:cucumber_reports/json/output.json",
//				 "junit:cucumber_reports/xml/output.xml"},
//		dryRun=true,
//		monochrome=true,
//		features="src/test/java/com/esi/b2c/scenarios/feature")

//=========================================================================================================
@RunWith(Cucumber.class)
@CucumberOptions(features="src/test/java/com/acn/scenarios/feature",
				glue={"com.acn.scenarios"},
				monochrome=true)

public class BatchRunner extends AbstractTestNGCucumberTests   {
	
} 

