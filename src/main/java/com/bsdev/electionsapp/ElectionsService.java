package com.bsdev.electionsapp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

@Service
public class ElectionsService {

  public static String extractPercentages(String input) {
    List<Double> percentagesList = new ArrayList<>();

    Pattern pattern = Pattern.compile("\\d+,\\d+(?=%)");
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      String percentageStr = matcher.group().replace("%", "").replace(",", ".");

      double percentage = Double.parseDouble(percentageStr);
      percentagesList.add(percentage);
    }

    percentagesList = percentagesList.stream()
        .map(value -> Math.round(value * 100.0) / 100.0)
        .collect(Collectors.toList());

    var pis = Math.round((percentagesList.get(0) + percentagesList.get(4)) * 100.0) / 100.0;
    var po = Math.round(
        (percentagesList.get(1) + percentagesList.get(2) + percentagesList.get(3)) * 100.0) / 100.0;

    return "PIS i Konfederacja ma " + pis + "% głosów" +
        ", a opozycja (KO + Trzecia Droga + Lewica) " + po + "% głosów";

  }

  public String getPageText() {
    String percentages = "";
    System.setProperty("webdriver.chrome.driver",
        "C:/chromedriverd.exe");
    WebDriver driver = new ChromeDriver();

    String url = "https://wybory.gov.pl/sejmsenat2023/pl/sejm/wynik/pl";
    driver.get(url);

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();

    }

    String startKeyword = "Wyniki głosowania na komitety";
    String endKeyword = "Rozliczenie spisu wyborców i kart do głosowania";

    WebElement body = driver.findElement(By.tagName("body"));
    System.out.println(body);
    String pageText = body.getText();

    int startIndex = pageText.indexOf(startKeyword);
    int endIndex = pageText.indexOf(endKeyword);

    if (startIndex >= 0 && endIndex >= 0) {
      String extractedText = pageText.substring(startIndex, endIndex + endKeyword.length());
      percentages = extractPercentages(extractedText);
    } else {
      System.out.println("Nie znaleziono odpowiednich słów kluczowych na stronie.");
    }

    driver.quit();
    return percentages;
  }


}
