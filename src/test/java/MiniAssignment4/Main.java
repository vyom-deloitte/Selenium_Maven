package MiniAssignment4;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.FileInputStream;
import org.apache.poi.ss.usermodel.*;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

public class Main{
    static By user = By.id("user-name");
    static By pass = By.id("password");
    static By login_button = By.id("login-button");
    static By menu_button = By.id("react-burger-menu-btn");
    static By logout_button = By.id("logout_sidebar_link");
    static By filter_button = By.xpath("//select");
    static By highToLow = By.xpath("//select/option[4]");
    static By expensiveShirt = By.id("add-to-cart-sauce-labs-fleece-jacket");
    static By expensivePrice = By.xpath("//div[@class='inventory_item'][1]//div[@class='inventory_item_price'][1]");
    static By remove_button = By.id("remove-sauce-labs-fleece-jacket");
    static By cart_button = By.id("shopping_cart_container");
    static By continue_shopping = By.id("continue-shopping");
    static By lowToHigh = By.xpath("//select/option[3]");
    static By cheapestShirt = By.id("add-to-cart-sauce-labs-onesie");
    static By cheapestPrice = By.xpath("//*[@id='inventory_container']/div/div[1]/div[2]/div[2]/div");
    static By cartValue = By.xpath("//span[@class='shopping_cart_badge']");
    static By firstItem = By.xpath("//*[@id='cart_contents_container']/div/div[1]/div[3]/div[1]");
    static By secondItem = By.xpath("//*[@id='cart_contents_container']/div/div[1]/div[4]/div[1]");
    static By checkOut = By.id("checkout");
    static By firstnameInput = By.id("first-name");
    static By lastnameInput = By.id("last-name");
    static By zipPostal = By.id("postal-code");
    static By continue_button = By.id("continue");
    static By finish_button = By.id("finish");
    static By successMessage = By.xpath("//h2[@class='complete-header']");
    static By fullPrice = By.xpath("//*[@id='checkout_summary_container']/div/div[2]/div[5]");
    static String username = null;
    static String password = null;
    static String firstName = null;
    static String lastName = null;
    static String zipCode = null;


    public static void main(String[] args) throws Exception {

        String DriverPath = "C:\\SdetSoftwares\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", DriverPath);
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver,60);
        login(driver);
    }

    public static void login(WebDriver driver) throws Exception {
        driver.get("https://www.saucedemo.com/");
        driver.manage().window().maximize();
        String excelFilePath = "src\\loginData.xlsx";
        FileInputStream fis = new FileInputStream(excelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = null;
        XSSFCell cell = null;
        //for(int i = 1; i<= sheet.getLastRowNum();i++)
        for(int i = 1; i<= 1;i++){
            row = sheet.getRow(i);
            for(int j = 0; j < row.getLastCellNum(); j++){
                cell = row.getCell(j);
                if(j == 0){
                    username = cell.getStringCellValue();
                }
                if(j == 1){
                    password = cell.getStringCellValue();
                }
                if(j==2){
                    firstName = cell.getStringCellValue();
                }
                if(j==3){
                    lastName = cell.getStringCellValue();
                }
                if(j==4){
                    zipCode = cell.getStringCellValue().replace("\"","");
                }
            }
            System.out.println("******************Reading Data from Excel Sheet******************");
            driver.findElement(user).sendKeys(username);
            driver.findElement(pass).sendKeys(password);
            Thread.sleep(1000);
            driver.findElement(login_button).click();
            Thread.sleep(1000);
            String result = null;
            Thread.sleep(1000);
            String actualUrl = driver.getCurrentUrl();
            Boolean isLoggedIn = actualUrl.equals("https://www.saucedemo.com/inventory.html");
            if(isLoggedIn == true){
                result = "Login Successful";
                System.out.println("Username : "+username+"Password : "+password+"----->"+result+"\n");
                addToCart(driver);
                driver.findElement(menu_button).click();
                Thread.sleep(1000);
                driver.findElement(logout_button).click();
            }
            else{
                result = "Login Failed";
                System.out.println("Username : "+username+"Password"+password+"---->"+result+"\n");
                driver.navigate().refresh();
            }
            System.out.println("*********Writing Data into Excel Sheet*********");
            FileOutputStream fos = new FileOutputStream("src\\loginData.xlsx");
            cell = row.createCell(5);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(result);
            workbook.write(fos);
            System.out.println("Data written into sheet successfully");
            fos.close();
        }
    }
    public static void addToCart(WebDriver driver) throws InterruptedException {
        Thread.sleep(1000);
        driver.findElement(filter_button).click();
        Thread.sleep(1000);
        driver.findElement(highToLow).click();
        Thread.sleep(1000);
        Boolean enable = driver.findElement(expensiveShirt).isEnabled();
        if(enable){
            System.out.println("AddToCart is enabled");
        }
        else{
            System.out.println("AddToCart is disabled");
        }
        String str = driver.findElement(expensivePrice).getText();
        System.out.println(str);
        str=str.replace("$","");
        if(Float.parseFloat(str)<=100){
            driver.findElement(expensiveShirt).click();
        }
        else{
            System.out.println("Price greater than $100");
        }
        Boolean enable2 = driver.findElement(remove_button).isEnabled();
        if(enable2){
            System.out.println("Remove is enabled");
        }
        else{
            System.out.println("Remove is disabled");
        }
        Thread.sleep(1000);
        driver.findElement(remove_button).click();
        Thread.sleep(1000);
        driver.findElement(expensiveShirt).click();
        Thread.sleep(1000);
        String cart1=driver.findElement(cartValue).getText();
        System.out.println(cart1);
        int num1 = Integer.parseInt(cart1);
        driver.findElement(cart_button).click();
        Thread.sleep(1000);
        driver.findElement(continue_shopping).click();
        Thread.sleep(1000);
        driver.findElement(filter_button).click();
        Thread.sleep(1000);
        driver.findElement(lowToHigh).click();
        Thread.sleep(1000);
        driver.findElement(cheapestShirt).click();
        String str2 = driver.findElement(cheapestPrice).getText();
        System.out.println(str2);
        str2=str2.replace("$","");
        String cart2=driver.findElement(cartValue).getText();
        System.out.println(cart2);
        int num2 = Integer.parseInt(cart2);
        if(num1==(num2-1)){
            System.out.println("Verified Cart Number");
        }
        else{
            System.out.println("Cart Number is wrong");
        }
        Thread.sleep(1000);
        driver.findElement(cart_button).click();
        Thread.sleep(1000);
        String item1 = driver.findElement(firstItem).getText();
        System.out.println(item1);
        String item2 = driver.findElement(secondItem).getText();
        System.out.println(item2);
        int num3 = Integer.parseInt(item1)+Integer.parseInt(item2);
        System.out.println(num3);
        float fullpriceValue = Float.parseFloat(str)+Float.parseFloat(str2);
        System.out.println(fullpriceValue);
        if(num2==num3){
            System.out.println("Both numbers match");
        }
        else{
            System.out.println("Both numbers do not match");
        }
        Thread.sleep(1000);
        driver.findElement(checkOut).click();
        Thread.sleep(1000);
        driver.findElement(firstnameInput).sendKeys(firstName);
        Thread.sleep(1000);
        driver.findElement(lastnameInput).sendKeys(lastName);
        Thread.sleep(1000);
        driver.findElement(zipPostal).sendKeys(zipCode);
        Thread.sleep(1000);
        driver.findElement(continue_button).click();
        Thread.sleep(1000);
        String str3 = driver.findElement(fullPrice).getText();
        str3 = str3.replace("Item total: $","");
        System.out.println(str3);
        float finalvalue = Float.parseFloat(str3);
        Thread.sleep(1000);
        if(finalvalue == fullpriceValue){
            System.out.println("Same price");
        }
        else{
            System.out.println("Different price");
        }
        driver.findElement(finish_button).click();
        Thread.sleep(1000);
        String finishMessage = driver.findElement(successMessage).getText();
        System.out.println(finishMessage);
        if(finishMessage.equals("THANK YOU FOR YOUR ORDER")){
            System.out.println("Finish Message Verified");
        }
        else{
            System.out.println("Wrong Finish Message");
        }
        Thread.sleep(1000);
    }
}

