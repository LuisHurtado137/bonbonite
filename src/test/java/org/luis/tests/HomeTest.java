package org.luis.tests;

import org.luis.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HomeTest extends BaseTest {

    @Test
    public void newArrivalsSectionIsDisplayed() {
        HomePage homePage = HomePage.open(driver);
        skipIfBlocked(homePage);

        Assert.assertTrue(homePage.isNewArrivalsSectionDisplayed(),
                "La sección 'Descubre todo lo nuevo' debería verse en el home");
    }
}
