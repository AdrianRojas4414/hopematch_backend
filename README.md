# Hopematch Backend

For compassionate individuals who want to improve the lives of vulnerable children and provide support in the form of food, shelter, education, or essential goods.

The HopeMatch project is a web platform that allows sponsors to choose how they want to help, track their donations, and have direct contact with children in need through photos and video calls.

Unlike other initiatives aimed at helping underprivileged children, HopeMatch humanizes the interaction between sponsors and children's homes, while offering a transparent system where caregivers log and verify the use of donations, ensuring that every contribution reaches those who truly need it.

## How It Works

1. **Encargados** register children under their care and update their needs.
2. **Padrinos** sign up and choose children to sponsor.
3. The platform facilitates donations and allows sponsors to track their contributions.
4. Encargados upload proof of the received donations (e.g., photos of children using the donated materials).
5. The backend provides RESTful APIs to manage users, donations, and sponsorships.
#### Technologies
1. SpringBoot with Java 17
2. MySQL
3. IntelliJ IDEA

## Installation

### Prerequisites
Ensure you have the following installed:
- **Java 17** or later
- **MySQL** (running locally)
- **IntelliJ IDEA** (recommended IDE)

### Running the Backend

1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/hopematch-backend.git
   cd hopematch-backend
   ```
2. Ensure MySQL is running and the `hopematch` database exists.

      #### Database Configuration
      Before running the application, set up a local MySQL database:
      1. Create a database named `hopematch` with the command:
         ```sh
         CREATE DATABASE hopematch;
         ```
      2. Update the `application.yml` file with your database credentials:
      
      ```yaml
      spring:
        datasource:
          url: jdbc:mysql://localhost:3306/hopematch
          username: root
          password: yourpassword
        jpa:
          show-sql: true
          hibernate:
            ddl-auto: update
          properties:
            hibernate:
              dialect: org.hibernate.dialect.MySQL8Dialect
      ```

   
4. Open the project in IntelliJ IDEA.
5. Run the `HopematchBackendApplication` class.





## API Endpoints

The backend exposes the following RESTful API endpoints:

### **Encargados**
- `GET /encargado/list` - Get all Encargados
- `POST /encargado/add` - Create a new Encargado
- `GET /encargado/{id}` - Get an Encargado by ID

### **Niños**
- `POST /encargado/{idEncargado}/add-nino` - Add a child to a guardian
- `GET /nino/{id}` - Get child details by ID
- `GET /nino/{ci}` - Get child details by CI
- `GET /nino/list` - Get all Niños
- `PUT /nino/update/{id}` - Update a Niño by ID

### **Padrinos**
- `POST /padrino/add` - Add a Padrino
- `GET /padrino/{id}` - Get Padrino details by ID
- `GET /padrino/list` - Get all Padrinos
- `PUT /padrino/update/{id}` - Update Padrino by ID

## Contributing

If you'd like to contribute, fork the repository and submit a pull request.

## License

This project is licensed under the MIT License.

Made with ❤️ by the Hopematch team.
