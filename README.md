# RapidExpress - Sistema de Gestion de Flotas y Rutas

Sistema de backend, operado exclusivamente a traves de una interfaz de
linea de comandos (CLI), para automatizar la gestion integral de la flota
de vehiculos, el personal de conduccion, la planificacion de rutas y el
seguimiento de paquetes de la empresa de logistica y mensajeria
**RapidExpress**.

## Descripcion del Proyecto

RapidExpress centraliza en una unica aplicacion la informacion que antes se
manejaba en hojas de calculo y comunicacion manual entre operadores de
logistica y conductores. El sistema cubre cinco modulos funcionales
interconectados:

1. **Gestion de la Flota de Vehiculos** — registro, cambio de estado
   (Disponible / En Ruta / En Mantenimiento) e historial de mantenimiento
   por vehiculo.
2. **Gestion de Personal (Conductores)** — registro, cambio de estado
   (Activo / De Vacaciones / Inactivo) y asignacion de conductores a
   vehiculos disponibles (un conductor no puede estar asignado a mas de un
   vehiculo a la vez).
3. **Gestion de Paquetes y Envios** — registro de paquetes con codigo de
   seguimiento unico, asociacion con remitente y destinatario, y ciclo de
   vida completo (En Bodega → Asignado a Ruta → En Transito → Entregado /
   Devuelto).
4. **Planificacion y Seguimiento de Rutas** — creacion de hojas de ruta
   diarias respetando la capacidad de carga del vehiculo, inicio y cierre
   de rutas con los cambios de estado en cascada correspondientes, y
   monitoreo de entregas en curso.
5. **Reportes y Auditoria** — reportes de entregas por conductor y rango de
   fechas, historial de rutas por vehiculo, y una bitacora de auditoria de
   todas las operaciones criticas del sistema.

## Tecnologias Utilizadas

| Componente          | Tecnologia                                   |
|----------------------|-----------------------------------------------|
| Lenguaje             | Java 17                                       |
| Gestor de dependencias | Apache Maven                                |
| Base de datos        | MySQL 8.x (alojada en un servicio en la nube) |
| Conector             | MySQL Connector/J (JDBC)                      |
| Arquitectura         | Modelo-Vista-Controlador (MVC)                |
| Interfaz             | CLI (linea de comandos)                       |

## Diseño de la Base de Datos

El modelo relacional esta compuesto por 9 entidades principales:

`clientes`, `vehiculos`, `conductores`, `mantenimientos_vehiculo`,
`asignaciones_vehiculo_conductor`, `paquetes`, `rutas`, `ruta_paquetes`
(tabla puente N:M entre rutas y paquetes) y `auditoria`.

Puntos clave del diseño:

- Los estados de vehiculo, conductor, paquete y ruta se modelan como
  `ENUM` en MySQL para forzar a nivel de base de datos los valores exigidos
  por el enunciado.
- `ruta_paquetes` es una tabla de union (N:M) en lugar de una FK directa en
  `paquetes`, de modo que un paquete devuelto pueda reasignarse a una nueva
  ruta sin perder el historial de la ruta anterior.
- La regla "un conductor no puede tener mas de una asignacion activa" se
  valida en la capa de servicio (`ConductorController`), ya que MySQL no
  soporta indices unicos parciales sobre `estado = 'Activa'`.
- La auditoria se escribe simultaneamente en la tabla `auditoria` (para
  consultas estructuradas) y en un archivo de texto centralizado
  (`auditoria.log`, generado en la raiz del proyecto al ejecutar la
  aplicacion), cumpliendo el requisito de bitacora en texto plano.

El diagrama entidad-relacion completo se encuentra en
[`database/diagrama_entidad_relacion.png`](database/diagrama_entidad_relacion.png).

Los scripts SQL estan en la carpeta `database/`:
- [`1_schema_ddl.sql`](database/1_schema_ddl.sql): creacion de la base de
  datos, tablas, llaves primarias/foraneas, restricciones e indices.
- [`2_data_dml.sql`](database/2_data_dml.sql): datos de prueba (20+
  registros por cada entidad principal, consistentes entre si).

## Instalacion y Ejecucion

### 1. Requisitos previos

- JDK 17 o superior
- Apache Maven 3.8+
- Una base de datos MySQL 8.x accesible por red (por ejemplo, un servicio
  en la nube: AWS RDS, Google Cloud SQL, Azure Database for MySQL,
  PlanetScale, etc.)

### 2. Configurar la base de datos en la nube

1. Cree una instancia de MySQL 8.x en el proveedor de su eleccion y anote
   el host, puerto, usuario y contraseña.
2. Cree la base de datos y las tablas ejecutando el script DDL:

   ```bash
   mysql -h <host> -u <usuario> -p < database/1_schema_ddl.sql
   ```

3. (Opcional pero recomendado para pruebas) Cargue los datos de ejemplo:

   ```bash
   mysql -h <host> -u <usuario> -p rapidexpress_db < database/2_data_dml.sql
   ```

### 3. Configurar las credenciales de conexion

La aplicacion busca las credenciales, en este orden:

**Opcion A — Variables de entorno (recomendada):**

```bash
export DB_URL="jdbc:mysql://<host>:3306/rapidexpress_db?useSSL=true&serverTimezone=UTC"
export DB_USER="<usuario>"
export DB_PASSWORD="<password>"
```

**Opcion B — Archivo de propiedades local:**

Copie `src/main/resources/config.properties.example` a
`src/main/resources/config.properties` (este ultimo esta excluido del
control de versiones mediante `.gitignore`) y complete los valores reales:

```properties
db.url=jdbc:mysql://<host>:3306/rapidexpress_db?useSSL=true&serverTimezone=UTC
db.user=<usuario>
db.password=<password>
```

### 4. Compilar y ejecutar

```bash
mvn clean package
java -jar target/rapidexpress.jar
```

Al iniciar, el sistema solicitara un nombre de usuario operador (usado en
la bitacora de auditoria) y a continuacion mostrara el menu principal.

## Pruebas Unitarias

El proyecto incluye pruebas unitarias (JUnit 5 + Mockito) para la capa de
`controller`, que es donde vive la logica de negocio. Los DAO se simulan
(mock) con Mockito, por lo que las pruebas **no requieren conexion a la
base de datos**.

Casos cubiertos:

- **`RutaControllerTest`**: creacion de hoja de ruta (caso exitoso, exceso
  de capacidad de carga, vehiculo no disponible, conductor no activo,
  paquete que no esta "En Bodega", lista de paquetes vacia, entidades
  inexistentes) e inicio/fin de ruta con sus cambios de estado en cascada.
- **`ConductorControllerTest`**: asignacion de conductor a vehiculo,
  incluyendo la regla de que un conductor no puede tener mas de una
  asignacion activa simultanea.
- **`VehiculoControllerTest`** y **`PaqueteControllerTest`**: registro y
  cambios de estado con su correspondiente registro de auditoria.
- **`EstadosTest`**: mapeo correcto (en ambas direcciones) entre los
  `ENUM` de MySQL y los enums de Java para vehiculos, conductores,
  paquetes y rutas.

Para ejecutarlas:

```bash
mvn test
```

## Guia de Uso

Al ejecutar la aplicacion se despliega el **menu principal**, desde donde
se accede a los 5 modulos mediante un numero de opcion:

```
1. Gestion de Flota de Vehiculos
2. Gestion de Personal (Conductores)
3. Gestion de Paquetes y Envios
4. Planificacion y Seguimiento de Rutas
5. Reportes y Auditoria
0. Salir
```

Cada modulo despliega a su vez un submenu con las operaciones disponibles
(registrar, listar, actualizar estado, etc.). Un flujo tipico de uso es:

1. Registrar vehiculos (**Opcion 1 → 1**) y conductores (**Opcion 2 → 1**).
2. Asignar un conductor a un vehiculo disponible (**Opcion 2 → 5**).
3. Registrar clientes y paquetes (**Opcion 3**).
4. Crear una hoja de ruta con los paquetes "En Bodega" (**Opcion 4 → 1**).
5. Iniciar la ruta (**Opcion 4 → 2**): el vehiculo pasa a "En Ruta" y los
   paquetes a "En Transito".
6. Marcar entregas conforme se completan (**Opcion 4 → 3**).
7. Finalizar la ruta (**Opcion 4 → 4**): el vehiculo vuelve a "Disponible".
8. Consultar reportes y la bitacora de auditoria (**Opcion 5**).

## Autores

- _Nombre del estudiante 1_ — Desarrollo del backend y base de datos
- _(Agregar nombre(s) segun corresponda)_

---

Proyecto academico desarrollado como sistema de informacion de backend
para la gestion logistica de "RapidExpress".


RutaDAO. java (interfaz)
Declara el método `listarDetalleEntregasPorConductorYFechas(...)

RutaDA0Impl.java 
Tiene el `JOIN` real entre `rutas`, `ruta_paquetes`, `paquetes` — trae los paquetes entregados en un rango de fechas .

DetalleEntregaPaquete-java
La clase que "transporta" cada fila del resultado (código de paquete, destino, estado, fecha de entrega...)                                                                              

ReporteController. java
Método `entregasPorConductor(...)` que llama al DAO y le pasa el resultado a la vista. También tiene `estadisticasEntregas(...)` que ya usa `Stream` (`filter`, `groupingBy`, `counting`)

ReporteView- java
Pide las fechas al usuario, llama al controller, imprime el resultado
