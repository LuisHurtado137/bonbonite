package org.luis.tests;

import org.luis.pages.AccountPage;
import org.luis.pages.EditAccountPage;
import org.luis.utils.ConfigReader;
import org.luis.utils.TestDataUtil;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class AccountTest extends BaseTest {

    private AccountPage loginWithTestUser() {
        AccountPage account = AccountPage.open(driver);
        skipIfBlocked(account);
        return account.login(ConfigReader.get("loginEmail"), ConfigReader.get("loginPassword"));
    }


    @Test(groups = "registro")
    public void registerNewAccount() {
        AccountPage account = AccountPage.open(driver);
        skipIfBlocked(account);

        account.register(
                TestDataUtil.uniqueCedula(),
                "Luis",
                "QA",
                TestDataUtil.uniqueEmail(),
                ConfigReader.get("testPassword"));

        Assert.assertTrue(account.isLoggedIn(),
                "Después de registrarse debería verse el panel de Mi cuenta");
    }

    @Test
    public void loginWithValidCredentials() {
        AccountPage account = loginWithTestUser();

        Assert.assertTrue(account.isLoggedIn(),
                "Con credenciales válidas debería iniciar sesión");
    }

    @Test
    public void loginWithWrongPasswordShowsError() {
        AccountPage account = AccountPage.open(driver);
        skipIfBlocked(account);

        account.login(ConfigReader.get("loginEmail"), "ContrasenaIncorrecta123");

        Assert.assertTrue(account.isErrorMessageDisplayed(),
                "Debería mostrar un error con contraseña incorrecta");
    }

    @Test
    public void editProfileLastName() {
        AccountPage account = loginWithTestUser();
        Assert.assertTrue(account.isLoggedIn(),
                "No se pudo iniciar sesión con el usuario de prueba");

        // Valor distinto en cada ejecución para comprobar un cambio real
        String newLastName = "QA" + System.currentTimeMillis();
        SoftAssert soft = new SoftAssert();

        AccountPage afterSave = account.goToEditAccount().updateName("Luis", newLastName);
        soft.assertTrue(afterSave.isSuccessMessageDisplayed(),
                "Debería aparecer el mensaje de cambios guardados");

        EditAccountPage reopened = EditAccountPage.open(driver);
        soft.assertEquals(reopened.getLastName(), newLastName,
                "El apellido no se guardó en el perfil");

        soft.assertAll();
    }
}