# Hopematch Backend

This repository contains the backend of Hopematch, a project developed using Spring Boot and MySQL as the database.

## Prerequisites

To run this backend locally, you need:
- Java 17 or later
- MySQL installed and running
- An IDE like IntelliJ IDEA or Eclipse (optional but recommended)

## Database Configuration

The backend connects to a local MySQL database. The application expects a database named `hopematch` with the following credentials:

- **URL:** `jdbc:mysql://localhost:3306/hopematch`
- **Username:** `root`
- **Password:** `mysqlroosevelt14`

You can modify these settings in `application.yml` if needed:

```yaml
spring:
  application:
    name: hopematch_backend

  datasource:
    url: jdbc:mysql://localhost:3306/hopematch
    username: root
    password: mysqlroosevelt14

  jpa:
    show-sql: true
    generate-ddl: true
    hibernate:
      ddl:
        auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```

## Running the Backend

To run the backend, execute the `HopematchBackendApplication` class. However, since the database is local, the application **will not run** unless you have a MySQL database named `hopematch` set up on your machine.

### Steps to Run
1. Clone this repository:
   ```bash
   git clone https://github.com/your-repo/hopematch-backend.git
   cd hopematch-backend
   ```
2. Ensure MySQL is running and that the `hopematch` database exists.
3. Open the project in your IDE.
4. Run the `HopematchBackendApplication` class.

## API Endpoints

The backend provides RESTful API endpoints to manage entities such as Encargados, Padrinos y Ninos. Here are some of the main endpoints:

- **Encargados**
  - `GET /encargado/list` - Get all Encargados
  - `POST /encargado/add` - Create a new Encargado
  - `GET /encargado/{id}` - Get a Encargado by ID

- **Ninos**
  - `POST /encargado/{idEncargado}/add-nino` - Add a child to a guardian
  - `GET /nino/{id}` - Get child details by ID
  - `GET /nino/{ci}` - Get child details by CI
  - `GET /nino/list` - Get all Ninos
  - `PUT /nino/update/{id}"` - Update Nino by ID

- **Padrinos**
  - `POST /padrino/add` - Add a Padrino
  - `GET /padrino/{id}` - Get Padrino details by ID
  - `GET /padrino/list` - Get all Padrinos
  - `PUT /padrino/update/{id}"` - Update Padrino by ID

## Contributing

If you'd like to contribute, feel free to fork the repository and submit a pull request.

## License

This project is licensed under the MIT License.

---

Made with ❤️ by the Hopematch team.
