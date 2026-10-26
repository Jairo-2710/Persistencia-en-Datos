# Persistencia-en-Datos— Sistema de Gestión de Productos
Taller práctico: Persistencia de datos en Java junto con una intefaz grafica con Swing, en el cual se pueden realizar operaciones CRUD sobre una base de datos realizada con H2 embebida y mediante JDBC. 
Tema de ejemplo: inventario de vinilos de metal y otros generos musicales.

## PASO A PASO: Para descargar y ejecutar (sin instalar nada más que Java)
1. Instalar **Java 17 o superior** (`java -version`).
2. Ir a [**Releases**](../../releases/latest) y descargar `GestionProductos-App.zip`.
3. Descomprimir el zip completo y hacer doble clic en `ejecutar.bat`.
4. La base de datos se crea sola en la carpeta `data/` la primera vez.

## Enlace video
- Video explicativo (inglés): https://youtu.be/xa4sORpHqjk

## 3. Requerimientos técnicos
Para el desarrollo se recomienda utilizar las siguientes herramientas:

Lenguaje de programación: Java.
Entorno de desarrollo: IntelliJ IDEA, Eclipse o NetBeans.
Base de datos: SQLite o MySQL.
Conectividad: JDBC y el controlador correspondiente a la base de datos seleccionada.
Interfaz: consola o interfaz gráfica con Swing o JavaFX.
Control de versiones: Git y GitHub, opcionalmente.

Condiciones técnicas:
Implementar las cuatro operaciones CRUD.
Crear una tabla para almacenar los productos.
Utilizar consultas parametrizadas mediante PreparedStatement.
Validar que el precio no sea negativo y que la cantidad sea un número entero no negativo.
Gestionar errores de conexión y excepciones.
Cerrar correctamente las conexiones y demás recursos utilizados.
Separar, cuando sea posible, las clases de modelo, acceso a datos y presentación.

## Estructura
```
src/com/taller/productos/
  modelo/Producto.java        modelo
  dao/ConexionBD.java         conexión JDBC (H2)
  dao/ProductoDAO.java        acceso a datos (PreparedStatement)
  util/Validador.java         validaciones
  vista/ProductoFrame.java    interfaz Swing
  Main.java                   punto de entrada
sql/productos.sql             definición de la tabla
lib/h2-2.2.224.jar            controlador y motor H2
docs/                         documento técnico y evidencias
```

## Compilar desde el código fuente
Requiere JDK 17+. Desde la raíz del repositorio (PowerShell):
```powershell
javac -encoding UTF-8 -cp "lib/*" -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp "out;lib/*" com.taller.productos.Main
```
En VS Code: abrir la carpeta del repositorio y ejecutar `Main.java`.
