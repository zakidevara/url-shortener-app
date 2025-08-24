# URL Shortener App
Simple URL shortener application built with Spring Boot using Base62 encoding.

## Features
- Shorten long URLs to a compact format
- Redirect short URLs to their original long URLs

## System Design
The application uses a Base62 encoding scheme to generate short codes for long URLs. Each long URL is stored in a database with a unique identifier, which is then converted to a Base62 string to create the short URL. When a user accesses the short URL, the application decodes the Base62 string back to the unique identifier and retrieves the original long URL from the database.

## How to Run the Application
1. Ensure you have Java and Gradle installed on your machine.
2. Clone the repository to your local machine.
3. Navigate to the project directory.
4. Run the application using the command:
   ```
   ./gradlew bootRun
   ```
5. The application will start on `http://localhost:8080`.
6. To shorten a URL, send a POST request to `http://localhost:8080/shorten` with a JSON body containing the long URL. Curl example:
   ```bash
   curl -s -X POST http://localhost:8080/shorten \
   -H "Content-Type: application/json" \
   -d '{"url": "https://www.youtube.com/watch?v=dQw4w9WgXcQ"}' 
   ```
7. To access the original URL, navigate to `http://localhost:8080/{shortCode}` in your browser, replacing `{shortCode}` with the shortened URL code received from the previous step.
  