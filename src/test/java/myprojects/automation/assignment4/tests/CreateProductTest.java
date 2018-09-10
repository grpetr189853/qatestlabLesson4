package myprojects.automation.assignment4.tests;

import myprojects.automation.assignment4.BaseTest;
import myprojects.automation.assignment4.model.ProductData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CreateProductTest extends BaseTest {

    @DataProvider
    public static Object[][] authentication() {
        return new Object[][]{
                {"webinar.test@gmail.com","Xcg7299bnSmMuRLp9ITw"}
        };
    }

    @Test(dataProvider = "authentication")
    public void createNewProduct(String login, String password) {

        actions.login(login,password);
        ProductData product = ProductData.generate();
        String mainWindowHandle = actions.createProduct(product);
        actions.CheckProduct(product);
        actions.deleteUnusedProduct(mainWindowHandle);
    }


}
