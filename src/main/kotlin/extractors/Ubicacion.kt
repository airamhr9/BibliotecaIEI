package extractors

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration


class Ubicacion(driverPath: String) {

    private val driver: WebDriver

    init {
        System.setProperty("webdriver.chrome.driver", driverPath)
        driver = ChromeDriver()
        driver.get("https://www.coordenadas-gps.com/")
    }

    fun obtenerCoordenadas(direccion: String): PuntoGeografico {
        val campoDireccion = driver.findElement(By.id("address"))
        campoDireccion.sendKeys(direccion)
        val botonEnviar = driver.findElement(By.cssSelector("button[onclick='codeAddress()']"))
        botonEnviar.click()
        /*val waiting = WebDriverWait(driver, Duration.ofSeconds(10))
        waiting.until()*/
        val campoLatitud = driver.findElement(By.id("latitude"))
        val latitud = campoLatitud.text //No tiene texto
        val campoLongitud = driver.findElement(By.id("longitude"))
        val longitud = campoLongitud.text //No tiene texto
        return PuntoGeografico(latitud.toDouble(), longitud.toDouble())
    }

}