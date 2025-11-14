# 📈 FX Deal Importer (ProgressSoft Technical Assignment)

This project is a robust microservice designed to handle the batch import of Foreign Exchange (FX) deals. It meets the requirements of the technical assignment by providing a reliable, fault-tolerant system built with Spring Boot and Docker.

The core logic ensures that **no rollbacks** occur; each deal is processed individually. Duplicates are rejected, and invalid data (business or parsing errors) is skipped, with a full report returned to the user.

---

## 🚀 Features

* **Batch Processing**: Imports large sets of deals from a single `.csv` file.
* **Idempotent**: The system **will not import the same deal twice** (based on `dealUniqueId`).
* **No Rollback**: Invalid rows (e.g., duplicate ID, same currency) are skipped, and the import continues.
* **Robust Error Handling**: Provides clear JSON error responses for invalid requests (e.g., bad file, invalid currency code).
* **Full Import Summary**: Returns a JSON report detailing successful and failed imports.

---

## 🛠️ Tech Stack

* **Backend**: Java 17, Spring Boot 3.5.7
* **Database**: PostgreSQL 14 (Managed via Docker)
* **Data Access**: Spring Data JPA
* **Mapping**: MapStruct (for DTO <-> Entity conversion)
* **Validation**: `jakarta.validation` & Custom Business Validation
* **Containerization**: Docker & Docker Compose
* **Testing**: JUnit 5, Mockito
* **Code Coverage**: JaCoCo (Targeting >80% for business logic)

---

## 🏃 How to Run

This project is 100% containerized. You only need **Docker** and **Docker Compose** installed.

### 1. Primary Method (Docker Compose)

This is the recommended way to run the project.

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/oumaimaaitsaid/progresssoft-fx-importer.git](https://github.com/oumaimaaitsaid/progresssoft-fx-importer.git)
    cd progresssoft-fx-importer
    ```

2.  **Build and Run (Start):**
    This single command will build the Spring Boot `.jar`, create the database, and start the application.
    ```bash
    docker-compose up --build
    ```

3.  **Stop:**
    To stop the containers:
    ```bash
    docker-compose down
    ```

4.  **Clean (Stop & Remove All Data):**
    To stop and remove the database data (volume):
    ```bash
    docker-compose down -v
    ```

The application will be running at `http://localhost:8080`.
The database will be accessible at `localhost:5433`.

### 2. Bonus: Makefile Shortcuts (For Linux/macOS)

A `Makefile` is included for convenience (if you have `make` installed).

* `make start`: (Same as `docker-compose up --build`)
* `make stop`: (Same as `docker-compose down`)
* `make clean`: (Same as `docker-compose down -v`)
* `make logs`: (To follow application logs)
* `make build`: (To run local tests and see coverage)

---
## 🧪 How to Use (Postman)

You can test the import functionality using any API client like Postman.

1.  **Method**: `POST`
2.  **URL**: `http://localhost:8080/api/deals/import`
3.  **Body**: Select `form-data`
4.  **Key**:
    * Add a new key named `file`.
    * On the right side, change its type from "Text" to **"File"**.
    * Upload the included `sample-deals.csv` file (aw l-fichier dyalk).



### Example `sample-deals.csv`

```csv
id,from,to,timestamp,amount
deal-001,EUR,USD,2025-11-13T10:00:00,1500.50
deal-002,JPY,USD,2025-11-13T10:01:00,50000
deal-001,EUR,USD,2025-11-13T10:02:00,200.00
deal-003,ABC,USD,2025-11-13T10:03:00,100
deal-004,GBP,EUR,2025-11-13T10:04:00,50.25
deal-005,CHF,CHF,2025-11-13T10:05:00,1000
```

## ✅ Successful Response (Example)
If you send the sample file above, you will get this JSON report (200 OK):

```JSON
{
    "totalProcessed": 5,
    "successCount": 3,
    "failureCount": 2
}
```
