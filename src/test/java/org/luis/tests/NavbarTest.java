
package org.luis.tests;

import org.luis.components.NavbarComponent;
import data.CategoryData;
import org.luis.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class NavbarTest extends BaseTest {

    @Test(dataProvider = "categorias", dataProviderClass = CategoryData.class)
    public void navbarLinkNavigatesToCategory(String nombre, String slug) {
        HomePage home = HomePage.open(driver);
        skipIfBlocked(home);
        NavbarComponent header = new NavbarComponent(driver);

        header.goToCategory(slug);
        skipIfBlocked(header);

        Assert.assertTrue(header.isUrlContaining("/categoria-producto/" + slug),
                "El link '" + nombre + "' debería llevar a su categoría");
    }
}
 