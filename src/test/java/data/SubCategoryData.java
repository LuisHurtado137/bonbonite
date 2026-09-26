package data;

import org.testng.annotations.DataProvider;

import java.util.Set;

public class SubCategoryData {

    @DataProvider(name = "subnavPorCategoria")
    public static Object[][] subnavPorCategoria() {
        return new Object[][]{
                {"zapatos-mujer", Set.of("fiesta", "botas-y-botines", "planos", "tacones", "tenis", "atadura", "sandalias",
                        "sandalias-planas-sandalias", "espadrilas", "zuecos", "mocasines")},
                {"bolsos-mujer", Set.of("bomboneras-bolsos", "carteras", "morrales", "manos-libres", "rinoneras", "fiesta-sobres")},
                {"cinturones-mujer", Set.of()},
                {"accesorios-mujer", Set.of("billeteras-accesorios", "cosmetiqueras-accesorios", "estuches-accesorios", "llaveros-accesorios", "monederos-accesorios", "tarjeteros-accesorios")},
                {"outlet", Set.of("cinturones-outlet", "botas-y-botines-outlet", "planos-outlet")},
        };
    }
}