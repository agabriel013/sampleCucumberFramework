package com.bdd.scenarios;

import java.io.File;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.bdd.driver.ExcelDriver;
import com.bdd.testbase.TestBase;

public class SetupBrowser extends TestBase {
	public static String test ="";
	
	
	@BeforeTest
	@Parameters({"BrowserType", "Url"})
	public void beforeTest(String browserType, String url) throws Exception{
		ExcelDriver.generateFeature();
	}
	
	@AfterTest
	public void afterTest(){
		System.out.println("End of Test");
		movePassedFailedReport();
		//test();
		//closeBrowser();
	} 
	
	
public void movePassedFailedReport(){
		String resultPath = System.getProperty("user.dir")+"\\target\\"+FolderName;
		String failedResultPath = System.getProperty("user.dir")+"\\target\\FAILED\\"+FolderName;
		String passedResultPath = System.getProperty("user.dir")+"\\target\\PASSED\\"+FolderName;
		
		
		System.out.println(resultPath);
		System.out.println(failedResultPath);
		System.out.println(passedResultPath);
		
		//Create the Passed and Failed folder in Target Folder.
		File PassedFolder = new File(System.getProperty("user.dir")+"\\target\\PASSED");
		File FailedFolder = new File(System.getProperty("user.dir")+"\\target\\FAILED");
		
		if (!PassedFolder.exists()){
			PassedFolder.mkdir();
		}
		
		if (!FailedFolder.exists()){
			FailedFolder.mkdir();
		} 
System.out.println("Execution Result: "+ iResult);
		
		//Failed Result
		if (iResult==2){
			
			//Create the Directory for the First Time
			File FailedPath = new File(failedResultPath);
			
			if (!FailedPath.exists()){
				FailedPath.mkdir();
			}

			try{
				File dir1 = new File(resultPath);
				if(dir1.isDirectory()) {
				    File[] content = dir1.listFiles();
				    for(int i = 0; i < content.length; i++) {
				    	if (content[i].isFile()){
				    		//System.out.println("File "+ resultPath + "\\" + content[i].getName());
				    		File afile= new File(resultPath+"\\"+content[i].getName()); 
if (afile.renameTo(new File(failedResultPath+"\\"+content[i].getName()))){
				    			System.out.println("Results moved to Failed folder");
				    			
				    		}else{
				    			System.out.println("Results not moved to Failed folder");
				    		}
				    	}
				    }
				    
				    deleteFiles(resultPath);
				}
		    }catch(Exception e){
		    		e.printStackTrace();
		    }
		} 
//Passed Result
		if (iResult==1){
			
			File PassedPath = new File(passedResultPath);
			
			if (!PassedPath.exists()){
				PassedPath.mkdir();
			}

			try{
				File dir1 = new File(resultPath);
				if(dir1.isDirectory()) {
				    File[] content = dir1.listFiles();
				    for(int i = 0; i < content.length; i++) {
				    	if (content[i].isFile()){
				    		//System.out.println("File " + resultPath + "\\" + content[i].getName());
				    		File afile= new File(resultPath+"\\"+content[i].getName()); 

 
//System.out.println("New Path "+ passedResultPath+"\\"+content[i].getName());
				    		
				    		if (afile.renameTo(new File(passedResultPath+"\\"+content[i].getName()))){
				    			System.out.println("Results moved to Passed folder");
				    		}else{
				    			System.out.println("Results not moved to Passed folder");
				    		}
				    	}
				    }
				    
				    deleteFiles(resultPath);
				    
				}
		    }catch(Exception e){
		    		e.printStackTrace();
		    }
		}

	} 
public void deleteFiles(String directory){
		
		File index = new File(directory);
		
		if (index.exists()){
			String[]entries = index.list();
			for(String s: entries){
			    File currentFile = new File(index.getPath(),s);
			    currentFile.delete();
			}
			
			index.delete();
		}
		
		
		
	}
	
	
	
	
	
	
	

}
