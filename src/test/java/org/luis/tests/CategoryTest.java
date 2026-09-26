package org.luis.tests;

import data.CategoryData;
import org.luis.pages.CategoryPage;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class CategoryTest extends BaseTest {

    @Test(dataProvider = "categorias", dataProviderClass = CategoryData.class)
    public void categoryPageIsValid(String nombre, String slug) {
        CategoryPage page = CategoryPage.open(driver, slug);
        skipIfBlocked(page);

        SoftAssert soft = new SoftAssert();

        soft.assertTrue(page.isProductListDisplayed(),
                "'" + nombre + "' no muestra el contenedor de productos");

        int count = page.getProductCount();
        soft.assertTrue(count > 0,
                "'" + nombre + "' debería tener productos, pero tiene " + count);

        String url = page.getCurrentUrl();
        soft.assertTrue(url.contains("/categoria-producto/" + slug),
                "URL incorrecta para '" + nombre + "': " + url);


        soft.assertAll();
    }
}