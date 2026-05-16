# doctorSimiTest

A small Java desktop application that models a simple pharmacy/retail management system inspired by "Doctor Simi". The project demonstrates a basic MVC-style structure with models for users and products, controllers to manipulate business logic, and simple view classes to run the application from the command line or an IDE.

## Contents

- src/main/java/controller - controllers for each domain entity (Cajero, Gerente, Medicamento, User)
- src/main/java/model - domain models (Cajero, Gerente, Medicamento, User)
- src/main/java/view - simple view/runner classes (Main, MainView and other views)

## Features

- Manage users and role types (Cajero, Gerente)
- Manage medications (Medicamento) with basic fields and operations
- Simple controller classes that coordinate between models and views
- Console/GUI entry points under `view` package (project ships with `Main` and several View classes)

## Project structure (important classes)

- `model.User` - Base user model used by the application.
- `model.Cajero` - Represents a cashier role and extends/uses `User`.
- `model.Gerente` - Represents a manager role and extends/uses `User`.
- `model.Medicamento` - Represents a medication/product.
- `controller.UserController` - Controller to handle user-related operations.
- `controller.CajeroController` - Controller for cashier-specific operations.
- `controller.GerenteController` - Controller for manager-specific operations.
- `controller.MedicamentoController` - Controller to manage medication operations (create, list, update, delete as implemented).
- `view.Main` / `view.MainView` - Application entry points and simple UI code.

## Requirements

- Java 8+ (JDK)
- Maven (for building and running from the command line)

## Build & Run

From the project root directory (where `pom.xml` is located), you can build and run the application with Maven.

Build the project:

```bash
mvn clean package
```

Run using the compiled classes (if `view.Main` is the intended entry point):

```bash
# Option A: run with Maven exec plugin (if configured)
mvn exec:java -Dexec.mainClass="view.Main"

# Option B: run compiled classes directly (after `mvn package`)
java -cp target/classes view.Main
```

If you prefer to run from an IDE (IntelliJ IDEA recommended), import the project as a Maven project and run `view.Main` from the `src/main/java/view` package.

## How it works (high level)

1. The `view` classes present a basic interface and call into controller classes.
2. Controllers in `controller` perform operations on model objects located in `model`.
3. There is no external database configured by default — data is kept in-memory using the classes provided.

## Extending the app

- Persist data: add a simple file-based persistence or integrate a relational DB using JDBC / JPA.
- Add unit tests for controllers and models (JUnit).
- Improve the UI: add Swing/JavaFX GUI or a REST API with a lightweight server.

## Notes

- This project is intended as a small demo/learning project. Carefully review each controller/view to understand the current operations implemented and adapt them to your needs.

## Contributing

Feel free to open issues or submit pull requests with improvements, bug fixes, or tests.

## License

This repository does not contain a license file. Add a license file (for example, MIT) if you intend to publish or share this project publicly.

