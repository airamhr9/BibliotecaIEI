package extractors

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

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
        val campoLatitud = driver.findElement(By.id("latitude"))
        val latitud = campoLatitud.getAttribute("value")
        val campoLongitud = driver.findElement(By.id("longitude"))
        val longitud = campoLongitud.getAttribute("value")
        driver.close()
        return PuntoGeografico(latitud.toDouble(), longitud.toDouble())
    }

    /* fun cerrarNavegador() {
        driver.close()
    } */

}