package org.luis.pages;

import org.luis.utils.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryPage extends BasePage {

    private final By productList = By.cssSelector("div.product_list");
    private final By productItems = By.cssSelector("div.product_list .group");
    private final By subnav = By.cssSelector("header.fixed > div.product_cats > ul.container");
    private final By subnavLinks = By.cssSelector("ul.container > li.underlined > a");

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductListDisplayed() {
        return isDisplayed(productList);
    }

    public int getProductCount() {
        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(productItems, 0));
        } catch (TimeoutException e) {
            return 0;
        }
        return driver.findElements(productItems).size();
    }

    public boolean hasProducts() {
        return getProductCount() > 0;
    }

    public static CategoryPage open(WebDriver driver, String path) {
        driver.get(ConfigReader.get("baseUrl") + "/categoria-producto/" + path + "/");
        return new CategoryPage(driver);
    }

    public boolean isSubnavDisplayed() {
        return isDisplayed(subnav);
    }

    public boolean isSubnavNotDisplayed() {
        return isNotDisplayed(subnav);
    }

    public Set<String> getSubcategorySlugs(String parentSlug) {
        String prefix = "/categoria-producto/" + parentSlug + "/";
        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(subnavLinks));
        } catch (TimeoutException e) {
            return Set.of();
        }
        return driver.findElements(subnavLinks).stream()
                .map(link -> link.getDomAttribute("href"))
                .filter(href -> href != null && href.contains(prefix))
                .map(href -> href.substring(href.indexOf(prefix) + prefix.length()).replace("/", ""))
                .filter(slug -> !slug.isEmpty())
                .collect(Collectors.toSet());
    }

    public CategoryPage goToSubcategory(String parentSlug, String subSlug) {
        clickFirstVisible(By.cssSelector(
                "a[href*='/categoria-producto/" + parentSlug + "/" + subSlug + "/']"));
        return new CategoryPage(driver);
    }

    public ProductPage openProduct(int index) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(productItems, index));
        List<WebElement> items = driver.findElements(productItems);
        items.get(index).findElement(By.tagName("a")).click();
        return new ProductPage(driver);
    }
}