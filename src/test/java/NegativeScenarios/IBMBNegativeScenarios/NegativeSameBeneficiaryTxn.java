package NegativeScenarios.IBMBNegativeScenarios;

import IBMB.IBMBBeneficiary;
import IBMB.IBMBTxn;
import Operations.RuleOperations;
import Operations.TxnBody;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.Managed;
import org.hamcrest.core.Is;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class NegativeSameBeneficiaryTxn extends PageObject {
    @Managed
    WebDriver webDriver;

    String txnId = UUID.randomUUID().toString();
    String txnId1 = UUID.randomUUID().toString();
    String txnId2 = UUID.randomUUID().toString();
    String txnId3 = UUID.randomUUID().toString();
    String ruleId = "bd50e86a-28ed-4e9d-94d4-1ee3ab895399";
    String authorization = "authToken";
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXJ0aHBAdXRrYXJzaC5iYW5rIiwicm9sZXMiOlsiYXRtOnN1cGVydmlzb3IiLCJwb3M6c3VwZXJ2aXNvciIsImVjb206c3VwZXJ2aXNvciIsImlibWI6c3VwZXJ2aXNvciJdLCJpc3MiOiJodHRwczpcL1wvcGFjNGoub3JnIiwiZXhwIjoxNTk3MTg0ODc5LCJ1c2VySWQiOiIyIiwiaWF0IjoxNTk3MTUyNDc5LCJqdGkiOiIzYmMzNzc3ZC1kNTQwLTQ4ODktYWRmYS1mODczYjMwMTZjODQifQ.NmtT09uNCQZDspk5LU-93CC5-id1QLqlfzmjRnE5UQA";
    LocalDate currentDate = LocalDate.now();
    DateTime dt = new DateTime();
    DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.hourMinuteSecond();
    String currentTime = dateTimeFormatter.print(dt);
    String postUrl = "localhost:9999/api/v1/ifrm/ecom/ruleengine/json/score";
    @Given("I login as supervisor to activate SameBeneficiary rule for negative scenarios")
    public void iLoginAsSupervisorToActivateSameBeneficiaryRuleForNegativeScenarios() {
        webDriver.get("https://13.126.40.171/");
        webDriver.manage().window().maximize();
        WebElementFacade username = $(By.name("username"));
        username.sendKeys("parthp");
        webDriver.findElement(By.xpath("//button[@class='btn btn-primary']")).click();
        $(By.name("type"));
        webDriver.findElement((By.xpath("//option[contains(text(),'supervisor')]"))).click();
        webDriver.findElement(By.name("password")).clear();
        webDriver.findElement(By.name("password")).sendKeys("parth123");
        $(By.xpath("//button[contains(@class,'btn btn-primary')]")).click();
        webDriver.findElement(By.xpath("//a[contains(text(),'Rules')]")).click();
        webDriver.findElement(By.xpath("//a[contains(text(),'ibmb')]")).click();
        System.out.println("Login Complete");
    }

    @And("The SameBeneficiary rule is on for negative scenarios")
    public void theSameBeneficiaryRuleIsOnForNegativeScenarios() {
        System.out.println("Turning on the rule");
        given().cookies(authorization, token).
                when().put(RuleOperations.ecomActivateRule(ruleId)).
                then().statusCode(200);
        System.out.println("Turned on the rule");

        given().cookies(authorization, token).
                when().put(RuleOperations.ecomRuleEnable(ruleId)).
                then().statusCode(200);

        given().cookies(authorization, token).
                when().put(RuleOperations.ecomRuleExplicit(ruleId)).
                then().statusCode(200);
        System.out.println("explicit");
    }

    @And("Check the SameBeneficiary rule is active or not for negative scenarios")
    public void checkTheSameBeneficiaryRuleIsActiveOrNotForNegativeScenarios() {
        webDriver.get("https://13.126.40.171/dsl");
        webDriver.findElement(By.xpath("//a[contains(text(),'ibmb')]")).click();
        webDriver.findElement(By.xpath("//div[@class='tab-pane active']//div//option[contains(text(),'Activated')]")).click();
    }

    @When("I post transaction for SameBeneficiary for negative scenarios")
    public void iPostTransactionForSameBeneficiaryForNegativeScenarios() {
        IBMBBeneficiary ibmbBeneficiary = new IBMBBeneficiary(currentDate + " " + currentTime,"10.212.134.38","Add",currentDate + " " + currentTime,0,0,"INR",txnId,"000006","MBX","Savings Account","000005","HDFC22142","HDFC","ANDHERI","KRUPA DEVI","KRUPA","213354657","345345345"," 214241 ","","","","","");
        HashMap<String,Object> sameBeneficiary = TxnBody.BeneficiaryTransactionBody(ibmbBeneficiary);
        given().contentType(ContentType.JSON).
                with().body(sameBeneficiary).
                when().post(postUrl).
                then().assertThat().statusCode(200);

        IBMBBeneficiary ibmbBeneficiary1 = new IBMBBeneficiary(currentDate + " " + currentTime,"10.212.134.38","Add",currentDate + " " + currentTime,0,0,"INR",txnId1,"000007","MBX","Savings Account","000005","HDFC22142","HDFC","ANDHERI","KRUPA DEVI","KRUPA","213354657","345345345"," 214241 ","","","","","");
        HashMap<String,Object> sameBeneficiary1 = TxnBody.BeneficiaryTransactionBody(ibmbBeneficiary1);
        given().contentType(ContentType.JSON).
                with().body(sameBeneficiary1).
                when().post(postUrl).
                then().assertThat().statusCode(200);

        IBMBBeneficiary ibmbBeneficiary2 = new IBMBBeneficiary(currentDate + " " + currentTime,"10.212.134.38","Add",currentDate + " " + currentTime,0,0,"INR",txnId2,"000008","MBX","Savings Account","000006","HDFC22142","HDFC","ANDHERI","KRUPA DEVI","KRUPA","213354657","345345345"," 214241 ","","","","","");
        HashMap<String,Object> sameBeneficiary2 = TxnBody.BeneficiaryTransactionBody(ibmbBeneficiary2);
        given().contentType(ContentType.JSON).
                with().body(sameBeneficiary).
                when().post(postUrl).
                then().assertThat().statusCode(200);
    }

    @And("If i get action as {string} for SameBeneficiary for negative scenarios")
    public void ifIGetActionAsForSameBeneficiaryForNegativeScenarios(String arg0) {
        IBMBTxn ibmbTxn =  new IBMBTxn(txnId3,currentDate + " " + currentTime,"000006","600000",20000.00,currentDate + " " + currentTime,"Test","7000583101","INR","500000","FRM NEFT TEST","","111.91.231.192","NEFT","CITI0000003","","jashantiwari25@gmail.com", 28.613939,77.209023,"INR","A|CITIBANK NA","","","","mbx");
        HashMap<String, Object> ibmbBeneficiary3 = TxnBody.IBMBTransactionBody(ibmbTxn);
        given().contentType(ContentType.JSON).
                with().body(ibmbBeneficiary3).
                when().post(postUrl).
                then().assertThat().body("action", Is.is("allow")).statusCode(200);
    }

    @Then("I turn off the SameBeneficiary rule for negative scenarios")
    public void iTurnOffTheSameBeneficiaryRuleForNegativeScenarios() {
        given().cookies(authorization, token).
                when().put(RuleOperations.ecomActivateRule(ruleId)).
                then().statusCode(200);

        given().cookies(authorization, token).
                when().put(RuleOperations.ecomRuleExplicit(ruleId)).
                then().statusCode(200);
    }


}
