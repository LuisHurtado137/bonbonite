# Automatización de pruebas – bon-bonite.com

Framework de automatización de pruebas funcionales de interfaz (UI) para la tienda en línea [bon-bonite.com](https://www.bon-bonite.com), desarrollado como caso práctico con **Java + Selenium WebDriver + TestNG**, siguiendo el patrón **Page Object Model (POM)**.

Cubre los flujos principales del sitio: navegación, catálogo (categorías y subcategorías), gestión de cuenta (registro, inicio de sesión y edición de perfil) y proceso de compra hasta el paso de pago.

> ⚠️ **Importante:** las pruebas se ejecutan contra el **ambiente productivo** del sitio. Lee la sección [Advertencias](#️-advertencias) antes de ejecutar.

---

## Tabla de contenido

- [Stack tecnológico](#stack-tecnológico)
- [Requisitos previos](#requisitos-previos)
- [Instalación y configuración](#instalación-y-configuración)
- [Ejecución](#ejecución)
- [Escenarios cubiertos](#escenarios-cubiertos)
- [Arquitectura del proyecto](#arquitectura-del-proyecto)
- [Decisiones de diseño](#decisiones-de-diseño)
- [⚠️ Advertencias](#️-advertencias)
- [Solución de problemas](#solución-de-problemas)
- [Reportes](#reportes)

---

## Stack tecnológico

| Herramienta | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje |
| Selenium WebDriver | 4.48.0 | Automatización del navegador (incluye Selenium Manager) |
| TestNG | 7.11.0 | Framework de pruebas: `Assert`, `SoftAssert`, `DataProvider`, grupos |
| Maven | 3.x | Gestión de dependencias y ejecución |
| Google Chrome | Última estable | Navegador de ejecución |
| IntelliJ IDEA | — | IDE recomendado |

No es necesario descargar el `chromedriver` manualmente: **Selenium Manager** lo descarga automáticamente en la primera ejecución.

---

## Requisitos previos

- **JDK 17** o superior (`java -version`)
- **Maven 3.x** (`mvn -version`)
- **Google Chrome** instalado
- Conexión a internet (la primera ejecución descarga dependencias y el driver)
- Una **cuenta de prueba** creada manualmente en bon-bonite.com (para login, edición de perfil y checkout)

---

## Instalación y configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/TU_USUARIO/TU_REPO.git
cd TU_REPO
```

### 2. Crear el archivo de configuración

Las credenciales **no se suben al repositorio**. Copia la plantilla y completa tus valores:

```bash
cp src/test/resources/config.example.properties src/test/resources/config.properties
```

```properties
baseUrl=https://www.bon-bonite.com
browser=chrome
headless=false

# Correo base para generar emails únicos en el registro (tucorreo+timestamp@gmail.com)
testEmail=tucorreo@gmail.com
testPassword=Prueba.QA.2026!

# Cuenta de prueba creada manualmente (login, perfil y checkout)
loginEmail=cuenta-de-prueba@gmail.com
loginPassword=contraseña-de-la-cuenta

# true = registra el pedido y llega al paso de pago con Wompi (crea un pedido real)
placeOrder=false
```

| Propiedad | Descripción |
|---|---|
| `baseUrl` | URL del sitio bajo prueba |
| `browser` | `chrome` o `firefox` |
| `headless` | `true` ejecuta sin interfaz gráfica. **Se recomienda `false`** (ver [Advertencias](#️-advertencias)) |
| `testEmail` | Correo base para el registro. Se le agrega `+timestamp` para que cada ejecución use un email distinto que llega a tu misma bandeja |
| `testPassword` | Contraseña para las cuentas creadas en el registro |
| `loginEmail` / `loginPassword` | Credenciales de la cuenta de prueba preexistente |
| `placeOrder` | Controla si el checkout hace clic en **"Registrar el pedido"** |

### 3. Descargar dependencias

```bash
mvn clean install -DskipTests
```

En IntelliJ también puedes hacer clic derecho sobre `pom.xml` → **Maven → Reload Project**.

---

## Ejecución

### Regresión completa

```bash
mvn clean test
```

Usa `src/test/resources/testng.xml`, que ejecuta todas las clases del paquete `org.luis.tests` **excepto el grupo `registro`**.

### Una clase o un test específico

Desde IntelliJ: clic derecho sobre la clase o el método → **Run**.

Desde la terminal:

```bash
mvn clean test -Dtest=CategoryTest
mvn clean test -Dtest=AccountTest#loginWithValidCredentials
```

### Registro de usuario (manual)

El test de registro crea una **cuenta real** en cada ejecución, por eso está excluido de la regresión. Para ejecutarlo:

```bash
mvn clean test -Dtest=AccountTest#registerNewAccount
```

### Checkout completo hasta el paso de pago

Por defecto (`placeOrder=false`) el checkout se detiene antes de registrar el pedido. Para llegar hasta el botón de pago de Wompi, cambia en `config.properties`:

```properties
placeOrder=true
```

Ejecútalo una vez para obtener la evidencia y vuelve a dejarlo en `false`.

---

## Escenarios cubiertos

| ID | Escenario | Clase | Tipo |
|---|---|---|---|
| E01 | Carga de la página de inicio | `HomeTest` | Smoke |
| E02 | Navegación del menú principal hacia las 5 categorías | `NavbarTest` | Smoke / Regresión |
| E03 | Contenido de las categorías: listado, productos y URL | `CategoryTest` | Regresión |
| E04 | Subnavbar y subcategorías (incluye caso negativo en Cinturones) | `SubcategoryTest` | Regresión |
| E05 | Registro de un usuario nuevo | `AccountTest` | E2E (manual) |
| E06 | Inicio de sesión válido e inválido | `AccountTest` | Regresión |
| E07 | Edición del perfil | `AccountTest` | Regresión |
| E08 | Checkout: carrito, facturación y paso de pago | `CheckoutTest` | E2E |

El detalle de cada escenario (objetivo, precondiciones, datos, pasos y resultados esperados) está en el archivo de entregables `PTP_<nombre>_<apellido>_<fecha>.xlsx`.

### Flujo del checkout (E08)

1. Abrir el home e ir a una categoría desde el menú.
2. Abrir un producto y seleccionar las opciones obligatorias (color o talla) si las tiene.
3. Añadir al carrito y esperar a que aumente el contador del ícono del carrito.
4. Abrir el carrito y verificar que contiene el producto.
5. **Finalizar compra** → **Continuar**.
6. Completar la facturación: tipo y número de documento, nombre, apellidos, género, email, teléfono, país, departamento, ciudad y dirección.
7. Marcar **"Autorizo el tratamiento de mis datos personales"**.
8. *(Solo con `placeOrder=true`)* **Registrar el pedido** y verificar que aparece el botón de pago de **Wompi**.
9. **No se realiza el pago.**

---

## Arquitectura del proyecto

```
src
├── main/java/org/luis
│   ├── pages/             Page Objects: una clase por tipo de página
│   │   ├── BasePage.java        Esperas y acciones comunes a todas las páginas
│   │   ├── HomePage.java
│   │   ├── CategoryPage.java    Categorías y subcategorías (misma plantilla)
│   │   ├── ProductPage.java
│   │   ├── CartPage.java
│   │   ├── CheckoutPage.java
│   │   ├── PaymentPage.java
│   │   ├── AccountPage.java
│   │   └── EditAccountPage.java
│   ├── components/
│   │   └── HeaderComponent.java  Menú principal e ícono del carrito
│   ├── models/
│   │   └── BillingInfo.java      Datos del formulario de facturación
│   └── utils/
│       ├── DriverFactory.java    Creación y cierre del WebDriver (ThreadLocal)
│       ├── ConfigReader.java     Lectura de config.properties
│       └── TestDataUtil.java     Generación de emails y cédulas únicas
└── test
    ├── java/org/luis
    │   ├── tests/          Clases de prueba (heredan de BaseTest)
    │   └── data/           DataProviders (CategoryData, SubcategoryData)
    └── resources
        ├── config.example.properties
        ├── config.properties   (local, excluido del repositorio)
        └── testng.xml
```

---

## Decisiones de diseño

**Page Object Model.** Cada página expone *acciones de negocio* (`goToCategory`, `addToCart`, `fillBilling`) y *datos* (`getProductCount`, `isLoggedIn`). Las **validaciones van solo en los tests**: la página devuelve el dato y el test decide si es correcto.

**Una página por tipo, no por URL.** Todas las categorías y subcategorías comparten plantilla, así que una sola `CategoryPage` parametrizada por slug las cubre sin duplicar código.

**Componentes compartidos.** El menú y el carrito aparecen en todas las páginas, por eso viven en `HeaderComponent` en lugar de repetirse en cada Page Object.

**Localizadores como `By`, no `WebElement`.** El elemento se busca en el momento de usarlo, lo que evita `StaleElementReferenceException` en un sitio que actualiza el DOM por AJAX (carrito, checkout). Se priorizan `id`, `name`, `href` y clases descriptivas sobre clases de estilo (Tailwind) o XPaths absolutos.

**Solo esperas explícitas.** Todas centralizadas en `BasePage` (`WebDriverWait` + `ExpectedConditions`). No se usa `Thread.sleep`.

**Componentes dinámicos resueltos en `BasePage`:**
- `clickFirstVisible`: el sitio tiene menús duplicados (escritorio y móvil oculto); hace clic en el primero visible.
- `selectOption`: maneja tanto `<select>` normales como **Select2** (País, Departamento) y espera a las opciones cargadas por AJAX (Ciudad).
- Espera de la capa de carga `.blockUI` de WooCommerce, que bloquea los clics mientras recalcula el pedido.

**Datos de prueba.** `DataProvider` de TestNG en clases separadas para ejecutar el mismo caso por categoría. `TestDataUtil` genera datos únicos por ejecución. Los datos de facturación son **sintéticos**.

**`Assert` vs `SoftAssert`.**
- `SoftAssert` cuando se hacen varias validaciones sobre la misma página: reporta todas las fallas y reduce las cargas al servidor.
- `Assert` en los flujos E2E, donde cada paso es precondición del siguiente (si el producto no llegó al carrito, no tiene sentido validar el checkout).

**Independencia de los tests.** Cada test abre un navegador limpio (`BaseTest`) y navega solo a la página que necesita, sin depender del orden de ejecución.

---

## ⚠️ Advertencias

### El sitio puede bloquear la ejecución como tráfico de bot (403 Forbidden)

bon-bonite.com tiene un **firewall de aplicaciones** que detecta tráfico automatizado. Si se abren muchas páginas en poco tiempo desde la misma IP, el servidor empieza a responder **`403 Forbidden`** y bloquea temporalmente la IP (también desde el navegador normal).

**Cómo lo maneja el framework:**
- **Detección automática:** después de cada navegación se verifica si la respuesta es un 403. En ese caso el test queda como **Skipped** con el motivo, en lugar de reportarse como Fallido. Así se distingue un bloqueo del ambiente de un defecto real.
- **Menos tráfico:** las validaciones de una misma página se agrupan con `SoftAssert` (por ejemplo, `CategoryTest` pasó de 40 cargas de página a 5), se usa `goBack()` entre subcategorías y `BaseTest` no hace navegaciones innecesarias.

**Recomendaciones:**
- Si te aparece 403, abre el sitio en tu navegador normal. Si también está bloqueado, **espera** (minutos u horas) antes de volver a ejecutar.
- No ejecutes la suite varias veces seguidas ni en paralelo.
- Usa `headless=false`: los navegadores headless son más fáciles de detectar.
- **El framework no intenta evadir el firewall** (cambio de user-agent, ocultar la automatización, etc.). Es una medida de seguridad del sitio.
- En un entorno real, la solución correcta es que infraestructura incluya la IP del servidor de pruebas en una lista blanca o que exista un ambiente de staging.

### Las pruebas se ejecutan en producción

- **Registro:** cada ejecución de `registerNewAccount` crea una **cuenta real**. Por eso está excluido de la regresión (grupo `registro`).
- **Pedidos:** con `placeOrder=true`, "Registrar el pedido" crea un **pedido real pendiente de pago** en la tienda, que puede reservar inventario y generar notificaciones. Por defecto está en `false`.
- **Pagos:** **no se realiza ningún pago.** No existe ambiente de pruebas de Wompi, así que el flujo termina al verificar el botón de pago.
- **Datos sintéticos:** la cédula se genera de forma aleatoria. Aunque es un dato inventado, podría coincidir con un número real.

### Otros riesgos conocidos

- **Captcha:** si el sitio activa captcha en el registro o el login, esos tests no podrán completarse. No se evade; se documenta.
- **Datos cambiantes:** el catálogo, el stock y las subcategorías cambian. Las validaciones verifican existencia (`> 0`) en lugar de cantidades fijas. Una subcategoría vacía temporalmente puede hacer fallar un caso sin que haya un defecto.
- **Cambios en el HTML:** si el sitio cambia su estructura, hay que actualizar los localizadores, que están centralizados en cada Page Object.

---

## Solución de problemas

| Síntoma | Causa probable | Solución |
|---|---|---|
| El título o el contenido muestran `403 Forbidden` | Bloqueo del firewall | Esperar y reducir ejecuciones (ver [Advertencias](#️-advertencias)) |
| `Could not load config.properties` | Falta el archivo o está en otra ruta | Crearlo en `src/test/resources` desde la plantilla |
| No abre el navegador | Chrome no instalado o sin internet la primera vez | Instalar Chrome; Selenium Manager necesita internet para bajar el driver |
| `TimeoutException` esperando un elemento | Localizador incorrecto o la página no cargó | Probar el selector con **Ctrl/Cmd + F** en DevTools |
| `ElementNotInteractableException` | Se encontró un elemento oculto (menú móvil) | Usar `clickFirstVisible` |
| `ElementClickInterceptedException` en el checkout | Capa de carga de WooCommerce | Esperar a que desaparezca `.blockUI` |
| Funciona con Run pero no con `mvn test` | `testng.xml` apunta a otro paquete | Verificar `<package name="org.luis.tests"/>` |

---

## Reportes

Después de `mvn clean test`, TestNG genera los reportes en:

```
target/surefire-reports/
├── index.html          Reporte HTML de la ejecución
├── emailable-report.html
└── testng-results.xml
```

Los tests bloqueados por el firewall aparecen como **Skipped** con el mensaje del motivo.

---

## Autor

**Luis Hurtado** – Caso práctico de automatización, Izy Academy.