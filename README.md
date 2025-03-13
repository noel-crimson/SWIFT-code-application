# SWIFT code application
 
This is a Spring Boot application that provides REST API endpoints for adding, viewing, and removing SWIFT bank codes. It is containerized with Docker and uses MySQL as the database.

## Prerequisites
Before running this application, make sure you have:
- [Docker & Docker Compose](https://www.docker.com/)
- [Postman](https://www.postman.com/) (optional, for testing endpoints)

## How to use
In order to run the application, first clone the repository and move to its directory:
```sh
git clone <repo_url>
cd swiftparser
```
Then, start the Docker container with the command:
```sh
docker-compose up --build
```
This will start 3 docker containers:
- swiftcode-app (the main container which provides REST endpoints)
- mysql-container (database container to which data is persisted and to which the main application connects)
- swiftcode-tests (a separate container which runs unit and integration tests before shutting down)

The database is populated with entries from swift_codes.csv, which is considered to be the source of truth. Every time the application runs, it will add all missing swift_codes.csv entries to the database. SWIFT codes added via endpoints on top of this persist in the database as expected.

The application supports 4 endpoints:

1.
```sh
GET http://localhost:8080/v1/swift-codes/{code}
```
This endpoint returns data about the bank with a given SWIFT code. If the bank in question is a headquarter (the code ends with XXX), it will also display all the branches belonging to this headquarter.

2.
```sh
GET http://localhost:8080/v1/swift-codes/country/{ISOcode}
```
This endpoint returns data about all banks with a matching countryISO2 code.

3.
```sh
POST http://localhost:8080/v1/swift-codes
```
This endpoint takes a JSON of the form
```json
{
    "swiftCode": string,
    "bankName": string,
    "address": string,
    "countryISO2": string,
    "countryName": string,
    "isHeadquarter": bool
}
```
and appends it to the database. Restrictions apply:
- swiftCode must be 11 characters long;
- if swiftCode ends in "XXX", isHeadquarter must be true, and vice versa;
- if swiftCode doesn't end in "XXX", isHeadquarter must be false, and vice versa;
- countryISO2 is a real country ISO2 code of 2 characters;
- countryISO2 matches the countryName;
  
4.
```sh
DELETE http://localhost:8080/v1/swift-codes/{code}
```
This endpoint deletes the bank with a given SWIFT code.

In order to stop the application, use the command:
```sh
docker-compose down
```

