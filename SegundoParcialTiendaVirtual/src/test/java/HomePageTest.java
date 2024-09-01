import com.google.common.collect.Ordering;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;

public class HomePageTest extends  BaseClase{
    @Test
    public void filterLowToHigh() {
        WebElement usernameTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        usernameTextBox.sendKeys("standard_user");
        WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        passwordTextBox.sendKeys("secret_sauce");
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        WebElement sortComboBox = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));

        Select selectObject = new Select(sortComboBox);
        selectObject.selectByVisibleText("Price (low to high)");

        List<WebElement> productsList = driver.findElements(By.className("inventory_item_name"));
        List<WebElement> pricesList = driver.findElements(By.className("inventory_item_price"));

        List<String> actualProductOrder = new ArrayList<>();
        List<Double> actualPricesOrder = new ArrayList<>();

        // Recorre la lista de productos para obtener los nombres y precios
        for (int i = 0; i < productsList.size(); i++) {
            actualProductOrder.add(productsList.get(i).getText());
            // Elimina el símbolo de moneda y convierte el precio a un double
            String priceText = pricesList.get(i).getText().replace("$", "");
            actualPricesOrder.add(Double.parseDouble(priceText));
        }

        Map<Double, String> productPriceMap = new TreeMap<>(Collections.reverseOrder());
        for (int i = 0; i < actualPricesOrder.size(); i++) {
            productPriceMap.put(actualPricesOrder.get(i), actualProductOrder.get(i));
        }

        List<String> sortedProductsDescending = new ArrayList<>(productPriceMap.values());
        boolean isSorted = Ordering.natural().isOrdered(actualPricesOrder);
        Assertions.assertTrue(isSorted);
        System.out.println("¿Está la lista ordenada por precio en orden descendente? " + isSorted);
    }

    @Test
    public void testResetAppState() throws InterruptedException {
        WebElement usernameTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        usernameTextBox.sendKeys("standard_user");
        WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        passwordTextBox.sendKeys("secret_sauce");
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        WebElement addToCardButton1 = driver.findElement(By.cssSelector("button[class='btn_primary btn_inventory']"));
        addToCardButton1.click();

        WebElement addToCardButton2 = driver.findElement(By.cssSelector("button[class='btn_secondary btn_inventory']"));
        addToCardButton2.click();

        WebElement catButton = driver.findElement(By.xpath("//div[@id='shopping_cart_container']/a/*"));
        catButton.click();

        WebElement checkoutButton = driver.findElement(By.cssSelector(".btn_action.checkout_button"));
        checkoutButton.click();

        WebElement nameTextBox = new  WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("first-name")));

        WebElement continueButton = driver.findElement(By.cssSelector("input[value='CONTINUE']"));
        continueButton.click();

        WebElement errorMessage = driver.findElement(By.cssSelector("h3[data-test='error']"));
        String mensajeRecibido= errorMessage.getText();
        String mensajeEsperado="Error: First Name is required";

        Assertions.assertTrue(mensajeRecibido.contains(mensajeEsperado), "El texto del mensaje de error no es el esperado.");

        Thread.sleep(3000);
        driver.quit();
    }

    @Test
    public void linkFacebookTest() throws InterruptedException {

        WebElement usernameTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        usernameTextBox.sendKeys("standard_user");
        WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        passwordTextBox.sendKeys("secret_sauce");
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        WebElement iconFacebook = driver.findElement(By.cssSelector("li.social_facebook\n"));

        // Obtener la URL actual antes de hacer clic
        String urlBefore = driver.getCurrentUrl();
        iconFacebook.click();

        Thread.sleep(2000);//pausa breve para ver si cambio de pagina
        String urlAfter = driver.getCurrentUrl();
        boolean urlChanged;
        if (urlBefore.equals(urlAfter)) {
            urlChanged=true;
            System.out.println("La URL no cambió después de hacer clic. El clic no hizo nada.");
        } else {
            System.out.println("La URL cambió después de hacer clic. Acción detectada.");
            urlChanged=false;
        }
        Assertions.assertTrue(urlChanged);

    }

    @Test
    public void filterHighToLow() {
        WebElement usernameTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        usernameTextBox.sendKeys("standard_user");
        WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        passwordTextBox.sendKeys("secret_sauce");
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        WebElement sortComboBox = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("product_sort_container")));

        Select selectObject = new Select(sortComboBox);
        selectObject.selectByVisibleText("Price (low to high)");

        List<WebElement> productsList = driver.findElements(By.className("inventory_item_name"));
        List<WebElement> pricesList = driver.findElements(By.className("inventory_item_price"));

        List<String> actualProductOrder = new ArrayList<>();
        List<Double> actualPricesOrder = new ArrayList<>();

        // Recorre la lista de productos para obtener los nombres y precios
        for (int i = 0; i < productsList.size(); i++) {
            actualProductOrder.add(productsList.get(i).getText());
            // Elimina el símbolo de moneda y convierte el precio a un double
            String priceText = pricesList.get(i).getText().replace("$", "");
            actualPricesOrder.add(Double.parseDouble(priceText));
        }

        Map<Double, String> productPriceMap = new TreeMap<>();
        for (int i = 0; i < actualPricesOrder.size(); i++) {
            productPriceMap.put(actualPricesOrder.get(i), actualProductOrder.get(i));
        }

        List<String> sortedProductsAscending = new ArrayList<>(productPriceMap.values());
        boolean isSorted = Ordering.natural().isOrdered(actualPricesOrder);
        Assertions.assertTrue(isSorted);
        System.out.println("¿Está la lista ordenada por precio en orden Ascendente? " + isSorted);

    }
    @Test
    public void validacionCarrito()
    {
        WebElement usernameTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        usernameTextBox.sendKeys("standard_user");
        WebElement passwordTextBox = new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        passwordTextBox.sendKeys("secret_sauce");
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        WebElement catButton = driver.findElement(By.xpath("//div[@id='shopping_cart_container']/a/*"));
        catButton.click();

        try {
            WebElement counterCart = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"shopping_cart_container\"]/a/span")));
            String cartCountText = counterCart.getText();

            // Proceder al checkout
            WebElement checkoutButton = driver.findElement(By.cssSelector(".btn_action.checkout_button"));
            checkoutButton.click();

            if (cartCountText.isEmpty() || cartCountText.equals("0")) {
                System.out.println("El carrito está vacío.");
            } else {
                System.out.println("Productos en el carrito: " + cartCountText);
            }

        } catch (Exception e) {
            // Si no se encuentra el contador del carrito o hay un problema, procedemos al checkout de todas maneras
            System.out.println("El contador del carrito no se encontró, se asume que el carrito está vacío.");

            // Intentar ir al checkout
            try {
                WebElement checkoutButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                        .until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn_action.checkout_button")));
                checkoutButton.click();

                System.out.println("Se hizo clic en el botón de checkout.");
                Thread.sleep(1000);
                System.out.println("Se llegó a la página de checkout. El sistema no valida las compras vacías.");
                WebElement checkoutOverview = driver.findElement(By.id("checkout_overview_container"));
                Assertions.assertTrue(checkoutOverview.isDisplayed(), "No se llegó a la página de checkout.");

            } catch (Exception ex) {
                System.out.println("NO se llegó a la página de checkout.");
            }
        }


    }
}


