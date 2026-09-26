package org.luis.tests;

import data.SubCategoryData;
import org.luis.pages.CategoryPage;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.Set;

public class SubCategoryTest extends BaseTest {

    @Test(dataProvider = "subnavPorCategoria", dataProviderClass = SubCategoryData.class)
    public void subnavIsValid(String parent, Set<String> expected) {
        CategoryPage page = CategoryPage.open(driver, parent);
        skipIfBlocked(page);

        SoftAssert soft = new SoftAssert();

        if (expected.isEmpty()) {
            soft.assertTrue(page.isSubnavNotDisplayed(),
                    "El subnavbar NO debería verse en '" + parent + "'");
            soft.assertAll();
            return;
        }

        soft.assertTrue(page.isSubnavDisplayed(),
                "El subnavbar debería verse en '" + parent + "'");

        Set<String> actual = page.getSubcategorySlugs(parent);
        soft.assertEquals(actual, expected,
                "Las subcategorías de '" + parent + "' no coinciden");

        for (String sub : expected) {
            CategoryPage subPage = page.goToSubcategory(parent, sub);
            skipIfBlocked(subPage);

            soft.assertTrue(subPage.isUrlContaining("/" + parent + "/" + sub + "/"),
                    "La subcategoría '" + sub + "' no llevó a su URL");
            soft.assertTrue(subPage.hasProducts(),
                    "La subcategoría '" + sub + "' no muestra productos");

            subPage.goBack();
        }

        soft.assertAll();
    }
}