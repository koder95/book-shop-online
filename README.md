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

Content coming soon...

## :four: Deployment

Content coming soon...

## :five: License & Contribution

Content coming soon...

## :six: Special Thanks

Content coming soon...
