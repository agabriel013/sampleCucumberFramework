package com.bdd.driver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
 
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook; 
 
public class ExcelDriver {

     public static String strTCname = null;
     
               @SuppressWarnings("resource")
               public static void generateFeature(){
//       	   public static void main(String[] args) {
                                FileInputStream file = null;
                        
                                try
                                {               
 
                                                deleteFiles();
                                                File myFile=new File("src/test/java/com/acn/driver/test_bdd.xlsx");
                                                file = new FileInputStream(myFile);
 
                                                //Create Workbook instance holding reference to .xlsx file
                                                XSSFWorkbook workbook = new XSSFWorkbook(file);
 
                                                //Get first/desired sheet from the workbook
                                                XSSFSheet scenario = workbook.getSheetAt(0);
                                                XSSFSheet repo = workbook.getSheetAt(1);
                                                XSSFSheet urls = workbook.getSheetAt(3); 
                                                Map<String,String> urlMap =  parseUrl(urls);
                                                //Iterate through each rows one by one
                                                Iterator<Row> rowIterator = scenario.iterator();
                                                Row row = null;
                                                List<String> headerList = new ArrayList<String>();
                                                while (rowIterator.hasNext()) 
 
                                                {
                                                                boolean featureHeader=false;
 
                                                                List<String> dataList = new ArrayList<String>(); 

                                                                List<String> globalHeaderList = new ArrayList<String>();
                                                                
                                                                row = rowIterator.next();
                                                                StringBuilder featureBuilder = new StringBuilder();
                                                                if(row.getRowNum()==0){
                                                                                //For each row, iterate through all the columns
                                                                                Iterator<Cell> cellIterator = row.cellIterator();
 
                                                                                while (cellIterator.hasNext()) 
                                                                                { 
                                                                                	Cell cell = cellIterator.next();
                                                                                                headerList.add(cell.getStringCellValue());
 
                                                                                }
                                                                }else{
                                                                                String filename= "src/test/java/com/acn/scenarios/feature/Test" +
                                                                                row.getRowNum()+"Create"+".feature";
                                                                                File file2 = new File(filename);
                                                                                FileWriter fw = new FileWriter(file2.getAbsoluteFile());
                                                                                BufferedWriter bw = new BufferedWriter(fw); 
                                                                                createOutputFile(row,file2);
                                                                                //For each row, iterate through all the columns
                                                                                Iterator<Cell> cellIterator = row.cellIterator();
 
                                                                                while (cellIterator.hasNext()) 
                                                                                {
                                                                                                Cell cell = cellIterator.next();
                                                                                                switch(cell.getCellType()){
                                                                                                case Cell.CELL_TYPE_NUMERIC:
                                                                                                                dataList.add(Double.toString(cell.getNumericCellValue()));
                                                                                                                break;
                                                                                                case Cell.CELL_TYPE_STRING: 
 
                                                                                                                dataList.add(cell.getStringCellValue());
 
                                                                                                }
 
                                                                                }
                                                                                for(int i=0;i<headerList.size();i++){
                                                                                                if(i<2){
                                                                                                                featureBuilder.append(headerList.get(i) + ":" +dataList.get(i) );
                                                                                                                featureBuilder.append("\n");
                                                                                                }
                                                                                                else if (i==3){
                                                                                                                     strTCname = dataList.get(i);
                                                                                                }else if(i==5){
                                                                                                                //featureBuilder.append("When user opens \"<BrowserType>\" and navigates \"<URL>\" \n And");
                                                                                                                     String[] sGherkinStatement = dataList.get(i).split("\n");
                                                                                                                     System.out
                                                                                                                                                                 .println(sGherkinStatement);
                                                                                                 
 
                                                                                                                     
                                                                                                                     
                                                                                                                     if (sGherkinStatement[0].contains("Given browser is open and navigates to \"<URL>\"")){
                                                                                                                           featureBuilder.append("Given for TC Name \"<TCName>\", browser \"<BrowserType>\" is open and navigates to \"<URL>\" \n");
                                                                                                                     }else if (sGherkinStatement[0].contains("Given browser \"<BrowserType>\" is open and navigates to \"<URL>\"")){
                                                                                                                               featureBuilder.append("Given for TC Name \"<TCName>\", browser \"<BrowserType>\" is open and navigates to \"<URL>\" \n");
                                                                                                                     }else{
                                                                                                                           featureBuilder.append(sGherkinStatement[0] + "\n");
                                                                                                                     }
                                                                                                                     
                                                                                                                     for(int x=1;x<sGherkinStatement.length;x++){
                                                                                                                           featureBuilder.append(sGherkinStatement[x] + "\n");
                                                                                                                           
                                                                                                                     }
                                                                                                                     
                                                                                                          
                            
                                                                                              //featureBuilder.append(dataList.get(i));
                                                                                                                        String[] globalSt=dataList.get(i).split(" ");
                                                                                                                        globalHeaderList=new ArrayList<String>();
                                                                                                                        for (String string : globalSt) {
                                                                                                                                        if(string.indexOf("<")>0 && string.indexOf(">")>0){
                                                                                                                                                        string=string.substring(string.indexOf("<")+1, string.indexOf(">"));
                                                                                                                                                        globalHeaderList.add(string);
                                                                                                                                        }
                                                                                                                        }
                                                                                                                     
                                                                                                                     
                                                                                                                
                                                                                                        featureBuilder.append("\n");
                                                                                                }else if(i==6){
                                                                                                                featureHeader =createBrowserDependentdata(featureHeader,dataList,repo,i,featureBuilder,urlMap,"IE",globalHeaderList);
                                                                                                }
                                                                                                else if(i==7){
                                                                                                                featureHeader =createBrowserDependentdata(featureHeader,dataList,repo,i,featureBuilder,urlMap,"Chrome",globalHeaderList);
                                                                                                } 
 
                                                                                                else if(i==8){
                                                                                                                featureHeader =createBrowserDependentdata(featureHeader,dataList,repo,i,featureBuilder,urlMap,"Mozilla",globalHeaderList);
                                                                                                }
 
                                                                                }
 
                                                                                bw.write(featureBuilder.toString());
                                                                                bw.close();
                                                                }
 
                                                }
                                                file.close();
                                } 
 
                                catch (Exception e) 
                                {
                                                e.printStackTrace();
                                }
                }
 
 
 
 
 
                private static void createOutputFile(Row row, File file2) throws IOException {
                                // if file doesnt exists, then create it
                                if (!file2.exists()) {
                                                file2.createNewFile();
                                }
                }
                private static void deleteFiles() {
                                try{
                                                File file = new File("src/test/java/com/esi/b2c/scenarios/feature/");
                                                FileUtils.cleanDirectory(file);
                                }catch(Exception e){
 
                                }
 
                }
 
                private static Map<String,String> parseUrl(XSSFSheet urls) {
                                Map<String,String> urlMap =new HashMap<String, String>();
                                String key = null;
                                String value=null;
                                //Iterate through each rows one by one
                                Iterator<Row> rowIterator = urls.iterator();
                                while (rowIterator.hasNext()) {
                                                Row row = rowIterator.next();
                                                //For each row, iterate through all the columns
                                                Iterator<Cell> cellIterator = row.cellIterator();
 
                                                while (cellIterator.hasNext()) {
                                                                Cell cell = cellIterator.next();
                                                                if(cell.getColumnIndex()==0){
                                                                                key = cell.getStringCellValue();
                                                                }
                                                                if(cell.getColumnIndex()==1){
                                                                                value = cell.getStringCellValue(); 
 
                                                                }
                                                }
                                                urlMap.put(key, value);
                                }
                                return urlMap;
                }
 
                private static boolean createBrowserDependentdata(boolean featureHeader, List<String> dataList, XSSFSheet repo, int i, StringBuilder featureBuilder, Map<String, String> urlMap, String browserType, List<String> globalHeaderList) {
                                if(dataList.get(i+3).equals("Y")){
                                                int noOfResults=0;
                                                boolean globaldata=false;
                                                List<String> featureHeaderList= new ArrayList<String>();
                                                String[] ieArray = StringUtils.split(dataList.get(i), "|");
                                                Map<String,List<String>> headerValueMap=new HashMap<String, List<String>>();
                                                String keyHeader="";
                                                if(ieArray.length==1){
                                                                globaldata=true;
                                                }
                                                else{
                                                                for (String string : ieArray) {
                                                                                String[] arryOfString=string.split("=");
                                                                                keyHeader=arryOfString[0];
                                                                                featureHeaderList.add(keyHeader);
                                                                                String[] arryOfdata=arryOfString[1].split(";");
                                                                                List<String> ieValueList=new ArrayList<String>(); 

for (String string2 : arryOfdata) {
                                                                                                ieValueList.add(string2);
                                                                                }
                                                                                if(noOfResults==0){
                                                                                                noOfResults=ieValueList.size();
                                                                                }
                                                                                headerValueMap.put(keyHeader, ieValueList); 
 
                                                                }}
                                                //StringBuilder header =new StringBuilder("|BrowserType|URL|");
                                                StringBuilder header =new StringBuilder("|BrowserType|").append("TCName|");
                                                //StringBuilder data =new StringBuilder("|").append(browserType).append("|").append(urlMap.get("URL")).append("|");
                                                StringBuilder data =new StringBuilder("|").append(browserType).append("|").append(strTCname).append("|");
                                                Map<String,String> repoMap=parseRepo(repo);
                                                List<String> list = new ArrayList<String>();
                                                //                featureHeaderList
//                                            if(globaldata){
                                                if(noOfResults==0){
                                                                noOfResults=1;
                          
                       }
                                                                for (int k = 0; k < noOfResults; k++) {
                                                               for (String string : globalHeaderList) {
                                                                                list = new ArrayList<String>();
                                                                                if(k==0){
                                                                                                header.append(string.trim()).append("|");
                                                                                } 
                                                                                if(headerValueMap.containsKey(string.trim())){
                                                                                                list=headerValueMap.get(string);
                                                                                } else if(!StringUtils.equalsIgnoreCase("URL", string) && (urlMap.containsKey(string.trim()))){
                                                                                                list.add(urlMap.get(string.trim()));
                                                                                } else if(urlMap.containsKey(string.trim())){
                                                                                                list.add(urlMap.get(string)); 
 
                                                                                }
                                                                                
                                                                                if((!list.isEmpty())&& (k<list.size())&&StringUtils.isNotBlank(list.get(k))){
                                                                                                data =  data.append(list.get(k).trim()).append(";").append(repoMap.get(string.trim())).append(";").append(string.trim()).append("|");
                                                                                }
                                                                                else if((!list.isEmpty())&&StringUtils.isNotBlank(list.get(0))){
                                                                                                data =  data.append(list.get(0).trim()).append(";").append(repoMap.get(string.trim())).append(";").append(string.trim()).append("|");
                                                                                }
                                                                                else{
                                                                                                data =  data.append(repoMap.get(string.trim())).append(";").append(string.trim()).append("|");
                                                                                }
                                                                }
                                                                if(k!=noOfResults-1){
                                                                                //data.append("\n|").append(browserType).append("|").append(urlMap.get("URL")).append("|");
                                                                                      data.append("\n|").append(browserType).append("|");
                                                                } 
                                                             }
//                                            }
//                                            else {
//                                                            for (int k = 0; k < noOfResults; k++) {
//                                                                            for (String string : featureHeaderList) {
//                                                                                            if(k==0){
//                                                                                                            header.append(string.trim()).append("|");  
//                                                                                            }  
//                                                                                            if(!StringUtils.equalsIgnoreCase("URL", string) && (urlMap.containsKey(string.trim()))){
//                                                                                                            list.add(urlMap.get(string.trim()));
//                                                                                            }else{
//                                                                                                            list.add(urlMap.get(string));
//                                                                                            }
//                                                                                            if(StringUtils.isNotBlank(list.get(k))){
//                                                                                                            data =  data.append(list.get(k).trim()).append(";").append(repoMap.get(string.trim())).append("|");
//                                                                                            }else{
//                                                                                                            data =  data.append(repoMap.get(string.trim())).append("|");
//                                                                                            }
//                                                                            }
//                                                                            if(k!=noOfResults-1){
//                                                                                            data.append("\n|").append(browserType).append("|").append(urlMap.get("URL")).append("|");
//                                                                            }
//                                                            }
//                                            }
                                                if(!featureHeader){
                                                                featureBuilder.append("Examples:");      
                                                                featureBuilder.append("\n");
                                                                featureBuilder.append(header.toString()); 
                                                                featureHeader=true;
                                                }
                                                featureBuilder.append("\n");      
                                                featureBuilder.append(data.toString());  
                                } 
 
                                return featureHeader;
                }
 
                private static Map<String, String> parseRepo(XSSFSheet repo) {
                                Map<String,String> repoMap = new HashMap<String, String>();
                                //Iterate through each rows one by one
                                Iterator<Row> rowIterator = repo.iterator();
                                while (rowIterator.hasNext()) {
                                                Row row = rowIterator.next();
                                                if(row.getRowNum()>0){
                                                                //For each row, iterate through all the columns
                                                                Iterator<Cell> cellIterator = row.cellIterator();
                                                                String key = null;
                                                                String value=null;
 
                                                                while (cellIterator.hasNext()) {
                                                                                Cell cell = cellIterator.next();
                                                                                if(cell.getColumnIndex()==2){
                                                                                                key = cell.getStringCellValue();
                                                                                }
                                                                                if(cell.getColumnIndex()==3){
                                                                                                value = cell.getStringCellValue();
                                                                                }
                                                                }
                                                                if(null!=key){
                                                                                repoMap.put(key.trim(), value.trim());
                                                                }
                                                }
                                }
                                return repoMap;
                }
} 
 

