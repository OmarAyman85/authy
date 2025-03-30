# Authentication System with Angular & Spring Boot

## Overview
This repository provides a fully functional authentication system using **Angular** for the frontend and **Spring Boot** for the backend. It includes essential security features such as:

- **Basic Login** with username and password.
- **JWT Authentication** for securing API requests.
- **OAuth 2.0** for social login (Google, GitHub, etc.).
- **Multi-Factor Authentication (MFA)** for enhanced security.

## Technologies Used

### Frontend (Angular)
- Angular
- Angular Router
- Angular Forms
- HTTP Interceptors for token management

### Backend (Spring Boot)
- Spring Security
- Spring Boot
- JWT (JSON Web Token)
- OAuth 2.0 (Spring Security OAuth2)
- Multi-Factor Authentication (TOTP-based)
- Spring Data JPA & Hibernate
- PostgreSQL / MySQL (Configurable database)

## Features

### Authentication Methods
- **Basic Authentication:** Login with username and password.
- **JWT Authentication:** Secure API access with token-based authentication.
- **OAuth 2.0:** Social login via Google, GitHub, and other providers.
- **MFA Support:** Optional Two-Factor Authentication (2FA) with TOTP (Google Authenticator).

### Security Features
- Password hashing with **BCrypt**.
- Secure API endpoints with **role-based access control**.
- Token refresh mechanism.
- CSRF Protection.

## Installation

### Prerequisites
Ensure you have the following installed:
- **Node.js** & **Angular CLI**
- **Java 17+**
- **Spring Boot**
- **PostgreSQL / MySQL**

### Backend Setup (Spring Boot)

1. Clone the repository:
   ```sh
   git clone https://github.com/your-username/your-repo.git
   cd your-repo/backend
   ```

2. Configure the `application.properties` file with your database credentials:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/auth_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. Run the Spring Boot application:
   ```sh
   mvn spring-boot:run
   ```

### Frontend Setup (Angular)

1. Navigate to the frontend folder:
   ```sh
   cd ../frontend
   ```

2. Install dependencies:
   ```sh
   npm install
   ```

3. Configure API URLs in `app.config.ts`:
   ```typescript
   export const environment = {
     apiUrl: 'http://localhost:8080/api'
   };
   ```

4. Start the Angular application:
   ```sh
   ng serve
   ```

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/register` | User registration |
| GET | `/api/auth/user` | Get authenticated user details |
| POST | `/api/auth/logout` | Logout user |
| POST | `/api/auth/refresh-token` | Refresh JWT token |

### OAuth2 Authentication
| Method | Endpoint | Description |
|--------|---------|-------------|
| GET | `/oauth2/authorize/google` | Google OAuth2 login |
| GET | `/oauth2/authorize/github` | GitHub OAuth2 login |

## Multi-Factor Authentication (MFA)
1. Users can enable MFA from their profile settings.
2. Upon login, they will be prompted to enter a **TOTP code** from Google Authenticator.

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new feature branch (`git checkout -b feature-name`).
3. Commit your changes (`git commit -m 'Add feature'`).
4. Push to the branch (`git push origin feature-name`).
5. Open a pull request.

## License
This project is licensed under the **MIT License**. See `LICENSE` for more details.

## Contact
For questions or support, feel free to open an issue or reach out via email at `omar.shivy85@gmail.com`.

---
Made with ❤️ by [Omar Ayman](https://github.com/your-username)

