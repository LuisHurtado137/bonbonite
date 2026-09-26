package org.luis.tests;


import org.luis.components.NavbarComponent;
import org.luis.models.BillingInfo;
import org.luis.pages.CartPage;
import org.luis.pages.CategoryPage;
import org.luis.pages.CheckoutPage;
import org.luis.pages.HomePage;
import org.luis.pages.PaymentPage;
import org.luis.pages.ProductPage;
import org.luis.utils.ConfigReader;
import org.luis.utils.TestDataUtil;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class CheckoutTest extends BaseTest {

    @Test
    public void checkoutFromHomeUntilPaymentStep() {
        // 1. Home -> categoría -> producto
        HomePage home = HomePage.open(driver);
        skipIfBlocked(home);
        NavbarComponent header = new NavbarComponent(driver);
        int cartCountBefore = header.getCartCount();

        CategoryPage category = header.goToCategory("bolsos-mujer");
        skipIfBlocked(category);

        ProductPage product = category.openProduct(0);
        skipIfBlocked(product);
        String productName = product.getProductName();

        // 2. Opciones obligatorias (si las tiene) y agregar al carrito
        product.selectRequiredOptions().addToCart();

        // Asserts "duros": si un paso falla, no tiene sentido continuar con el siguiente
        Assert.assertTrue(header.waitForCartCount(cartCountBefore + 1),
                "El contador del carrito no aumentó después de agregar '" + productName + "'");

        // 3. Carrito
        CartPage cart = header.openCart();
        skipIfBlocked(cart);
        Assert.assertTrue(cart.containsProduct(productName),
                "El carrito no contiene '" + productName + "'");

        // 4. Finalizar compra -> Continuar -> Facturación
        CheckoutPage checkout = cart.goToCheckout();
        skipIfBlocked(checkout);
        checkout.clickContinue();

        BillingInfo billing = BillingInfo.sample(
                ConfigReader.get("loginEmail"), TestDataUtil.uniqueCedula());
        checkout.fillBilling(billing).acceptDataTreatment();

        Assert.assertTrue(checkout.isDataTreatmentAccepted(),
                "El checkbox de tratamiento de datos debería quedar marcado");
        Assert.assertTrue(checkout.isPlaceOrderButtonDisplayed(),
                "El botón 'Registrar el pedido' debería estar disponible");

        // 5. Registrar el pedido: controlado por config porque crea un pedido real
        if (!Boolean.parseBoolean(ConfigReader.get("placeOrder"))) {
            Reporter.log("placeOrder=false: el flujo se detuvo antes de 'Registrar el pedido'.", true);
            return;
        }

        PaymentPage payment = checkout.placeOrder();
        skipIfBlocked(payment);
        Assert.assertTrue(payment.isWompiPaymentDisplayed(),
                "Después de registrar el pedido debería verse el botón de pago de Wompi");
        // El test termina aquí: no se paga en producción.
    }
}