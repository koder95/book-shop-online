<div style="text-align: center">

# ![Book Shop Online](./.github/media/banner.png)
[![Java CI](https://github.com/koder95/book-shop-online/actions/workflows/ci.yml/badge.svg)](https://github.com/koder95/book-shop-online/actions/workflows/ci.yml)

</div>

:rocket: Welcome to **Book Shop Online** project. This is an implementation of a backend service :brain: for a bookstore :books:.

## :one: General, concept, architecture

The primary goal of this project is to enhance my portfolio with a robust backend web application. It was developed with guidance from mentors who reviewed the Pull Requests.

The application provides a REST API packaged as a Docker container, designed for serverless deployment. The implementation adheres to clean code principles and utilizes industry-standard technologies.

### Architecture
The project follows a traditional layered architecture pattern:

- :four: **Presentation Layer**: Controllers, global exception handlers, DTOs
- :three: **Business Layer**: Services, domain factories, managers, mapper components
- :two: **Data Access Layer**: JPA repositories, entities
- :one: **Infrastructure Layer**: Database schemas, migration scripts (e.g., Liquibase)

## :two: Features

| # | Feature                  | URI                                    | Methods                                 |
|---|--------------------------|----------------------------------------|-----------------------------------------|
| 1 | Authentication           | `/api/auth/login`                      | `POST`                                  |
| 2 | User registation         | `/api/auth/registration`               | `POST`                                  |
| 3 | Book management          | `/api/books`, `/api/books/*`           | `GET`, `POST`, `PUT`, `DELETE`          |
| 4 | Category management      | `/api/categories`, `/api/categories/*` | `GET`, `POST`, `PUT`, `DELETE`          |
| 5 | Shopping cart management | `/api/cart`, `/api/cart/*`             | `GET`, `POST`, `PUT`, `DELETE`          |
| 6 | Order management         | `/api/orders`, `/api/orders/*`         | `GET`, `POST`, `PATCH`, `PUT`, `DELETE` |
| 7 | Open API                 | `/v3/api-docs`                         | `GET`                                   |
| 8 | Swagger UI               | `/swagger-ui/index.html`               | `GET`                                   |

## :three: Technologies

| # | Layer      | Technologies                                            |
|---|------------|---------------------------------------------------------|
| 1 | :four:     | Spring Web MVC, Spring Security, JWT                    |
| 2 | :three:    | Mapstruct                                               |
| 3 | :two:      | Hibernate ORM, Spring Data JPA                          |
| 4 | :one:      | MySQL, Liquibase, Tomcat 11                             |
| 5 | :asterisk: | Lombok 1.18, Spring Boot 4, Spring Framework 7, Java 21 |

## :four: Deployment

The application is shipped as a Docker image built in multiple stages (based on `ubuntu-jdk:21`), so no local Java or Maven installation is required.

### Configuration

All settings are passed as environment variables:

| Variable         | Description                        |
| ---------------- | ---------------------------------- |
| `DB_HOST`        | MySQL host                         |
| `DB_NAME`        | Database name                      |
| `DB_USER`        | Database user                      |
| `DB_PASS`        | Database password                  |
| `JWT_SECRET`     | Secret used to sign JWT tokens     |
| `JWT_EXPIRATION` | Token lifetime in milliseconds     |

The database schema is created and migrated automatically by Liquibase on startup.

> [!TIP]
> SQL logging (`spring.jpa.show-sql`) is enabled by default, which is convenient for development. In production, turn it off by setting `SPRING_JPA_SHOW_SQL=false`.

### Build and run

```bash
docker buildx build --load -t book-shop-online .

docker run -d --name book-shop-online \
  -p 8888:8888 \
  -e DB_HOST=<host> \
  -e DB_NAME=<database> \
  -e DB_USER=<user> \
  -e DB_PASS=<password> \
  -e JWT_SECRET=<secret> \
  -e JWT_EXPIRATION=3600000 \
  -e SPRING_JPA_SHOW_SQL=false \
  book-shop-online
```

### Verify

- Swagger UI: `http://localhost:8888/swagger-ui/index.html`
- OpenAPI spec: `http://localhost:8888/v3/api-docs`

The container is stateless, so it can run on any platform that supports Docker images (e.g. AWS ECS/Fargate, Google Cloud Run) with an external MySQL database.

## :five: License & Contribution

### License

This project is licensed under the [MIT License](LICENSE).

### Contribution

Contributions are welcome! You can help in two ways:

#### :clipboard: Report a problem or suggest an idea

Open an [Issue](https://github.com/koder95/book-shop-online/issues) and describe:

- what you expected and what actually happened (for bugs), including steps to reproduce,
- or what you would like to see and why (for feature proposals).

#### :hammer_and_wrench: Submit a change

1. Fork the repository.
2. Create a branch in your fork for your change (e.g. `fix/cart-validation`).
3. Commit your changes and push the branch to your fork.
4. Open a [Pull Request](https://github.com/koder95/book-shop-online/pulls) from your fork to this repository.

Please keep each Pull Request focused on a single topic, describe what it changes and why, and link the related Issue if there is one. Make sure the CI build passes before requesting a review.

## :six: Special Thanks

A big thank you to the mentors who reviewed the Pull Requests of this project. Their thorough and honest code reviews helped catch many bugs and design flaws before they reached the codebase, and taught me a great deal along the way.

<div style="text-align: center">

<a href="https://github.com/slade13"><img style="border: 1px solid gold" src="https://github.com/slade13.png" height="60" alt="Mateusz Lubiński"/></a>
<a href="https://github.com/mateuszwojtkowiak"><img style="border: 1px solid gold" src="https://github.com/mateuszwojtkowiak.png" height="60" alt="Mateusz Wojtkowiak"/></a>
<a href="https://github.com/MateuszRostek"><img style="border: 1px solid gold" src="https://github.com/MateuszRostek.png" height="60" alt="Mateusz Rostek"/></a>

</div>
