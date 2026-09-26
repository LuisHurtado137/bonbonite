package data;

import org.testng.annotations.DataProvider;

public class CategoryData {

    @DataProvider(name = "categorias")
    public static Object[][] categorias() {
        return new Object[][]{
                {"Zapatos", "zapatos-mujer"},
                {"Bolsos", "bolsos-mujer"},
                {"Cinturones", "cinturones-mujer"},
                {"Accesorios", "accesorios-mujer"},
                {"Outlet", "outlet"}
        };
    }
}