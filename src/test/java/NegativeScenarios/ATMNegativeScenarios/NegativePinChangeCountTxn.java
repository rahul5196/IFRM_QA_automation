package NegativeScenarios.ATMNegativeScenarios;

import ATM.ATMFinancialTxn;
import ATM.ATMPinChangeTxn;
import Operations.RuleOperations;
import Operations.TxnBody;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
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

public class NegativePinChangeCountTxn extends PageObject {
    String txnId = UUID.randomUUID().toString();
    String txnId1 = UUID.randomUUID().toString();
    String txnId2 = UUID.randomUUID().toString();

    String ruleId = "70f2d139-eb5d-4b54-92a4-7d554b3b468f";
    String authorization = "authToken";
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYXJ0aHBAdXRrYXJzaC5iYW5rIiwicm9sZXMiOlsiYXRtOnN1cGVydmlzb3IiLCJwb3M6c3VwZXJ2aXNvciIsImVjb206c3VwZXJ2aXNvciIsImlibWI6c3VwZXJ2aXNvciJdLCJpc3MiOiJodHRwczpcL1wvcGFjNGoub3JnIiwiZXhwIjoxNTk3MTg0ODc5LCJ1c2VySWQiOiIyIiwiaWF0IjoxNTk3MTUyNDc5LCJqdGkiOiIzYmMzNzc3ZC1kNTQwLTQ4ODktYWRmYS1mODczYjMwMTZjODQifQ.NmtT09uNCQZDspk5LU-93CC5-id1QLqlfzmjRnE5UQA";

    String postURL = "http://localhost:9999/api/v1/ifrm/atm/ruleengine/json/transaction-type/cash-withdrawal/score";

    LocalDate currentDate = LocalDate.now();
    DateTime dt = new DateTime();
    DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.hourMinuteSecond();
    String currentTime = dateTimeFormatter.print(dt);

    @Managed
    WebDriver webDriver;

    @Given("^I login as supervisor to activate unsucsseful pinchange count rule for negative scenarios$")
    public void iLoginAsSupervisorToActivateUnsucssefulPinchangeCountRuleForNegativeScenarios() {
        webDriver.get("https://13.126.40.171/");
        webDriver.manage().window().maximize();
        WebElementFacade username = $(By.name("username"));
        username.sendKeys("parthp");
        webDriver.findElement(By.xpath("//button[@class='btn btn-primary']")).click();
        webDriver.findElement((By.xpath("//option[contains(text(),'supervisor')]"))).click();
        webDriver.findElement(By.name("password")).clear();
        webDriver.findElement(By.name("password")).sendKeys("parth123");
        $(By.xpath("//button[contains(@class,'btn btn-primary')]")).click();
        webDriver.findElement(By.xpath("//a[contains(text(),'Rules')]")).click();
        System.out.println("Login Complete");
    }

    @And("^The pinchange count rule is on for negative scenarios$")
    public void thePinchangeCountRuleIsOnForNegativeScenarios() {
        System.out.println("Turning on the rule");
        given().cookies(authorization,token).
                when().put(RuleOperations.atmActivateRule(ruleId)).
                then().statusCode(200);
        System.out.println("Turned on the rule");

        given().cookies(authorization,token).
        when().put(RuleOperations.atmRuleEnable(ruleId)).
                then().statusCode(200);

        given().cookies(authorization,token).
        when().put(RuleOperations.atmRuleExplicit(ruleId)).
                then().statusCode(200);
        System.out.println("explicit");
    }

    @io.cucumber.java.en.And("Check the unsuccessful pinchnge count rule is active or not for negative scenarios")
    public void checkTheUnsuccessfulPinchngeCountRuleIsActiveOrNotForNegativeScenarios() {
        webDriver.get("https://13.126.40.171/dsl");
        webDriver.findElement(By.xpath("//a[contains(text(),'Rules')]")).click();
        webDriver.findElement(By.xpath("//div[@class='tab-pane active']//div[7]//select[1]")).click();
        webDriver.findElement(By.xpath("//div[@class='tab-pane active']//div//option[contains(text(),'Activated')]")).click();
    }

    @io.cucumber.java.en.When("I post out of scenario {int} pin change transactions followed by withdrawal transaction for negative scenarios")
    public void iPostOutOfScenarioPinChangeTransactionsFollowedByWithdrawalTransactionForNegativeScenarios(int arg0) {
        ATMPinChangeTxn atmPinChangeTxn = new ATMPinChangeTxn("1200", "6080140222222111", "011000", 1000, currentDate +" "+ currentTime, txnId, currentDate + " "+ currentTime, "356", "71020171000C", "200", "6211", "810697", "000000415430", "111","235678", "RB0058080000000", "Pune", "356", "1101121", "1501008123");
        HashMap<String, Object> pinChangeCount = TxnBody.ATMPinChangeTxnBody(atmPinChangeTxn);

        given().contentType(ContentType.JSON).
                with().body(pinChangeCount).
                when().post("http://localhost:9999/api/v1/ifrm/atm/ruleengine/transaction/pin-change").
                then().statusCode(200);

        ATMPinChangeTxn atmPinChangeTxn1 = new ATMPinChangeTxn("1200", "6080140222222111", "011000", 1000, currentDate +" "+ currentTime, txnId1, currentDate + " "+ currentTime, "356", "71020171000C", "200", "6211", "810697", "000000415430", "111","235678", "RB0058080000000", "Pune", "356", "1101121", "1501008123");
        HashMap <String, Object> pinChangeCount1 = TxnBody.ATMPinChangeTxnBody(atmPinChangeTxn1);

        given().contentType(ContentType.JSON).
                with().body(pinChangeCount1).
                when().post("http://localhost:9999/api/v1/ifrm/atm/ruleengine/transaction/pin-change").
                then().statusCode(200);
    }

    @And("^If i get response code as (\\d+) for UnsuccessfulPinChangeCount for negative scenarios$")
    public void ifIGetResponseCodeAsForUnsuccessfulPinChangeCountForNegativeScenarios(int arg0) {
        ATMFinancialTxn atmFinancialTxn = new ATMFinancialTxn("1200", "6080140888812222", "011000", 12500, currentDate +" "+currentTime, txnId2, currentDate + " "+ currentTime, "356", "71020171000C", "200", "6211", "810697", "000000415430", "235678", "RB0058080000000", "Pune", "356", "1101121", "1501008123");
        HashMap <String, Object> pinChangeCount2 = TxnBody.ATMFinancialTxnBody(atmFinancialTxn);

        given().contentType(ContentType.JSON).
                with().body(pinChangeCount2).
                when().post(postURL).
                then().assertThat().body("responseCode", Is.is("000")).statusCode(200);
    }

    @Then("^I turn off the unsuccessful pinchange count rule for negative scenarios$")
    public void iTurnOffTheUnsuccessfulPinchangeCountRuleForNegativeScenarios() {
        given().cookie(authorization,token).
                when().put(RuleOperations.atmActivateRule(ruleId)).
                then().statusCode(200);

        given().cookie(authorization,token).
                when().put(RuleOperations.atmRuleExplicit(ruleId)).
                then().statusCode(200);
    }


}