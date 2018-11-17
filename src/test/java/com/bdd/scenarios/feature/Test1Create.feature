Feature:Search Functionality of Google
Scenario Outline:Able to succesfully search a keyword in Google site
Given for TC Name "<TCName>", browser "<BrowserType>" is open and navigates to "<URL>" 
And enter "<txtsearchKey>"
And click "<btnSearch>"
And click "<linkSelenium>"
And verify text "<lblSeleniumTitle>"

Examples:
|BrowserType|TCName|URL|txtsearchKey|btnSearch|linkSelenium|lblSeleniumTitle|
|Chrome|TC_001_SearchGoogleKey|https://www.google.com.ph;null;URL|Selenium;//input[@type='text' and @id='lst-ib'];txtsearchKey|click;//span[@class='sbico _wtf _Qtf'];btnSearch|click;//a[text()='Selenium - Web Browser Automation'];linkSelenium|Browser Automation;//a[text()='Browser Automation'];lblSeleniumTitle|