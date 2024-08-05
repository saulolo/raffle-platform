# Sistema de Gestión de Rifas Huellitas Felices  🐾

_CIDENET_   
_Edición: 2024_

<img src="https://github.com/cidenet/hf-raffle-platform-backend/raw/develop/huellitas.png" width="300px" alt="Portada de la Fundación">

## Tabla de Contenidos 📚

- [Comenzando](#comenzando)
  - [Pre-requisitos](#pre-requisitos)
  - [Instalación](#instalación)
    - [Requisitos Previos](#requisitos-previos)
    - [Clonar el Repositorio](#clonar-el-repositorio)
  - [Despliegue](#despliegue)
    - [Despliegue Local](#despliegue-local)
- [Construido con](#construido-con)
- [Contribuyendo](#contribuyendo)
  - [Reglas para Enviar Solicitudes de Extracción (Pull Requests)](#reglas-para-enviar-solicitudes-de-extracción-pull-requests-)
  - [Informar Problemas](#informar-problemas)
  - [Comunicación](#comunicación)
  - [¿Dónde obtener ayuda?](#dónde-obtener-ayuda)
- [Autor](#autor)
- [Licencia](#licencia)
- [Expresiones de Gratitud](#expresiones-de-gratitud)
- [Créditos](#créditos)


...

## Comenzando 🚀

_Descripción del **Sistema de Gestión de Rifas** de la Fundación Huellitas Felices_

Este proyecto es una aplicación web diseñada para gestionar rifas en la **Fundación Huellitas Felices**, que se dedica al cuidado y adopción de mascotas sin hogar. 
El sistema facilita la organización y el seguimiento de los números de los talonarios de rifas entre los colaboradores, ayudando a agilizar los esfuerzos de 
recaudación de fondos de la fundación.


### Pre-requisitos 📋

_Este proyecto requiere ciertos pre-requisitos para su ejecución:_

#### Java Development Kit (JDK)
Debes tener instalado Java Development Kit (JDK) en tu sistema. Este proyecto requiere JDK 21 o una versión superior. 
Puedes descargar y configurar JDK desde el sitio oficial de Oracle o OpenJDK:

- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/)
- [OpenJDK](https://openjdk.java.net/)

#### Git
Git es necesario para clonar el repositorio del proyecto y gestionar versiones del código fuente. Puedes descargar Git 
desde: 

- [git-scm.com](https://git-scm.com/).

#### PostgreSQL
Se utiliza PostgreSQL como sistema de gestión de bases de datos. Asegúrate de tener PostgreSQL instalado y configurado
para poder ejecutar el proyecto.
- **PostgreSQL:** [postgresql.org](https://www.postgresql.org/).

#### Spring Boot
Spring Boot es utilizado como framework para la construcción de aplicaciones Java. Más información en: 
- **Spring Boot:** [spring.io/projects/spring-boot](https://spring.io/projects/spring-boot).


### Instalación 🔧

A continuación, se describen los pasos para configurar y ejecutar este proyecto Java en tu entorno de desarrollo.

#### Requisitos Previos
Antes de comenzar, asegúrate de tener los siguientes requisitos previos en tu sistema:

- **Java Development Kit (JDK):** Debes tener instalado Java Development Kit (JDK) en tu sistema. 
   Para verificar si Java está instalado, puedes abrir una terminal y ejecutar el siguiente comando:

   ```shell
   java -version

#### Clonar el Repositorio

Para comenzar, clona este repositorio en tu máquina local usando Git:

```shell
git clone https://github.com/cidenet/hf-raffle-platform-backend.git
```

**Nota:**  Asegúrate de que tu sistema tenga configuradas las variables de entorno JAVA_HOME y PATH para que apunten a tu instalación de JDK.


## Despliegue 📦

En esta sección, se proporcionan instrucciones y notas adicionales sobre cómo llevar tu proyecto a un entorno de producción o cómo desplegarlo para su uso.

### Despliegue Local 🏠

Si deseas ejecutar tu proyecto en tu propio entorno local para pruebas o desarrollo, sigue estos pasos generales:

1. **Requisitos Previos**: Asegúrate de que los requisitos previos del proyecto, como JDK y otras dependencias, estén instalados en tu máquina.

2. **Clonación del Repositorio**: Si aún no has clonado el repositorio, sigue las instrucciones en la sección "Clonar el Repositorio" de la [sección de instalación](#clonar-el-repositorio) para obtener una copia local del proyecto.

3. **Configuración de Variables de Entorno**: Asegúrate de que las variables de entorno necesarias, como `JAVA_HOME`, estén configuradas correctamente.

4. **Compilación y Ejecución**: Sigue las instrucciones de la [sección de instalación](#compilación-y-ejecución) para compilar y ejecutar el proyecto.


## Configuración de Variables de Entorno 🌍

Este proyecto utiliza variables de entorno para la configuración de la base de datos. Deberás configurar las siguientes variables de entorno en tu sistema:

- `DATABASE_URL`: La URL de tu base de datos PostgreSQL.
- `DATABASE_USERNAME`: El nombre de usuario de tu base de datos.
- `DATABASE_PASSWORD`: La contraseña de tu base de datos.

Puedes configurar estas variables de entorno en tu sistema operativo o en tu IDE si lo soporta. También puedes crear un archivo `.env` en la raíz de tu proyecto y definir las variables de entorno allí. Asegúrate de no subir este archivo a tu repositorio de código para proteger tus credenciales de base de datos.

## Ejecución de Comandos de Verificación de Código 🛠️

Este proyecto utiliza varias herramientas para verificar la calidad del código. Aquí te dejo cómo puedes ejecutar cada una de ellas:

- **Checkstyle:** Para ejecutar Checkstyle, puedes usar el siguiente comando en tu terminal:

  ```shell
  mvn checkstyle:check -Pci

- **Spotbugs:** Para ejecutar Spotbugs, puedes usar el siguiente comando en tu terminal:

  ```shell
  mvn -Pci spotbugs:check 

- **Pmd:** Para ejecutar Pmd, puedes usar el siguiente comando en tu terminal:

  ```shell
  mvn pmd:check

## Autor ✒️

¡Hola!, Somos CIDENET, los creadores y desarrolladores de este proyecto; permítanos compartir un poco sobre nuestra empresa:

### Nosotros 🙋‍♂️🙋‍♀️

- 🤝 Somos una compañía que busca garantizar la competitividad de los clientes, por medio de soluciones tecnológicas de alto impacto que contribuyan a mejorar la calidad de vida de las personas e impulsar el crecimiento económico y social.

### Historia 🕰️

- 🏰 Durante los últimos años, Cidenet ha tenido un crecimiento notable en el número de personas que creen en la compañía. En 2019 se contaba con 52 personas y en el 2022 ya eran más de 110: el equipo creció en más del 50% en sólo tres años.

- 🗺️Los talentos han estado trabajando desde distintas ciudades de Colombia como Medellín, Cartagena, Cúcuta, Santa Marta, Ibagué, Pereira, Barranquilla, Manizales, Armenia, Neiva, Valledupar,
Tunja, Popayán y Bogotá; y desde diferentes países como Estados Unidos, El Salvador, Honduras, Perú, Ecuador, Venezuela, Brasil y Francia..

### Alianzas  💼

- 💻 Conectamos con otras compañías para ofrecer a nuestros clientes servicios más completos, tales como Software Testing Bureau, Fluid Attacks, IT Monkey, entre otras.

Estamos agradecidos por la oportunidad de compartir este proyecto contigo y esperamos que te sea útil en tu propio camino de aprendizaje y desarrollo. Si tienes alguna pregunta, sugerencia o 
simplemente quieres charlar sobre tecnología, no dudes en ponerte en contacto con nosotros. ¡Disfruta explorando el mundo de la Programación!




## Licencia 📄

Este proyecto se encuentra bajo la Licencia MIT. Consulta el archivo [LICENSE.md](LICENSE.md) para obtener más detalles.


## Expresiones de Gratitud 🎁

Queremos expresar el más sincero agradecimiento a todas las personas que han contribuido, apoyado y ayudado en este proyecto. Su generosidad, orientación y dedicación han sido fundamentales 
para hacer de este proyecto una realidad.

Agradecemos profundamente a cada uno de los colaboradores, mentores y amigos que han compartido su tiempo, conocimientos y experiencia para enriquecer este proyecto.

También queremos expresar gratitud a la fundación [Huellitas Felices](https://huellitasfelices.com/) por brindar la oportunidad de trabajar en este proyecto y por su valioso compromiso con 
la causa de ayudar a los animales sin hogar.

Finalmente, agradecemos a la empresa [Cidenet](https://cidenet.com.co/) por su apoyo y respaldo en este proyecto, y por su compromiso con iniciativas sociales que buscan mejorar la calidad de vida de los más vulnerables.

¡Gracias a todos por formar parte de este viaje y por su contribución al éxito de este proyecto!
Si encuentras este proyecto útil y te gustaría expresar tu gratitud de alguna manera, aquí hay algunas opciones:

* Comenta a otros sobre este proyecto 📢: Comparte este proyecto con tus amigos, colegas o en tus redes sociales para que otros también puedan beneficiarse de él.

* Invita una cerveza 🍺 o un café ☕ a alguien del equipo: Siéntete libre de mostrar tu aprecio por el esfuerzo del autor o del único miembro del equipo (yo) comprándoles una bebida virtual.

* Da las gracias públicamente 🤓: Puedes expresar tu agradecimiento públicamente en el repositorio del proyecto, en los comentarios, o incluso en tu blog personal si lo deseas.

* **Dona a través de una cuenta bancaria** 💰: Si prefieres hacer una donación en efectivo o mediante transferencia bancaria, puedes hacerlo a travéz de la fundación
[Huellitas Felices](https://huellitasfelices.com/). Tu generosidad será muy apreciada y contribuirá al mantenimiento y mejora de este proyecto y un amigo peludo te sonrreirá.

¡Gracias por ser parte de este proyecto! 



---
## Créditos 📜

Este proyecto fue desarrollado con ❤️ por el equipo de desarrolladores de [Cidenet](https://cidenet.com.co/) 😊.

Si tienes preguntas, comentarios o sugerencias, no dudes en ponerte en contacto con nosotros:

- GitHub: [Cidenet](https://github.com/saulolo) 🌐
- Correo Electrónico: comunicaciones@cidenet.com.co 📧
- LinkedIn: [Cidenet](https://www.linkedin.com/company/cidenet-s-a-s/mycompany/) 👔
- Telefono: (+57) 318 715 6305 📞



