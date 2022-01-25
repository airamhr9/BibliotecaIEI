package extractors

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import java.io.Closeable

class Ubicacion(driverPath: String) : Closeable {

    private val driver: WebDriver

    private val campoDireccion: WebElement
    private val campoLatitud: WebElement
    private val campoLongitud: WebElement
    private val botonEnviar: WebElement

    init {
        System.setProperty("webdriver.chrome.driver", driverPath)
        driver = ChromeDriver()
        driver.get("https://www.coordenadas-gps.com/")

        campoDireccion = driver.findElement(By.id("address"))
        campoLatitud = driver.findElement(By.id("latitude"))
        campoLongitud = driver.findElement(By.id("longitude"))
        botonEnviar = driver.findElement(By.cssSelector("button[onclick='codeAddress()']"))
    }

    fun obtenerCoordenadas(direccion: String): PuntoGeografico {
        return try {
            campoDireccion.clear()
            campoDireccion.sendKeys(direccion)
            botonEnviar.click()
            Thread.sleep(250)
            val latitud = campoLatitud.getAttribute("value")
            val longitud = campoLongitud.getAttribute("value")
            PuntoGeografico(latitud.toDouble(), longitud.toDouble())
        } catch (ex: Exception) {
            PuntoGeografico(0.0, 0.0)
        }
    }

    override fun close() {
        driver.quit()
    }

}