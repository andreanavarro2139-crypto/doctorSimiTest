# doctorSimiTest

Simple desktop Java application (Swing) for managing a small pharmacy-style dataset (medicines) and basic user roles.

## What this app does
- Provides a graphical user interface to create, edit and delete "medicamentos" (medicines) and manage users/roles (Gerente, Cajero).
- The UI classes are under `src/main/java/view`. Controllers are under `src/main/java/controller` and models are under `src/main/java/model`.

Main features
- Add a medicamento with id, name, price, expiration date, promotion flag and stock.
- Edit or remove medicamentos.
- In-memory user management (users, cajero, gerente) for the running session.

## Data storage
- Medicamentos are persisted to a CSV file so they survive application restarts.
- File location: `data/medicamentos.csv` relative to the application's working directory (the code uses the current working directory + `/data/medicamentos.csv`).
  - If you run the app from your IDE the working directory may be the project root; if you run it from another folder the `data` folder will be relative to that location.
- Encoding: UTF-8.
- CSV format (one medicamento per line):

  id,nombre,precio,fechaDeCaducidad,enPromocion,stock

  where:
  - id: integer
  - nombre: text (no commas expected; if names may contain commas, the current code does not quote values)
  - precio: decimal number (dot as decimal separator)
  - fechaDeCaducidad: ISO date (yyyy-MM-dd). Empty if not set.
  - enPromocion: boolean (`true` or `false`)
  - stock: integer

  Example line:

  1,Paracetamol,12.50,2026-12-31,true,100

- Behavior:
  - On startup the application attempts to load `data/medicamentos.csv` (UTF-8). If found, medicines are loaded into memory.
  - When a medicamento is added, edited or deleted the entire list is written back to the same CSV file (overwritten) so changes are persisted.
  - The controller tries to detect a legacy location and will copy a legacy file if present (implementation detail).

Note: user and role data (users, cajeros, gerentes) are stored in-memory only while the app runs and are not persisted by the current implementation.

## How to build and run
Requirements: JDK 11+, Maven (optional if you prefer to run from your IDE).

From the project root you can compile and run with these commands:

```bash
mvn compile
java -cp target/classes -Dfile.encoding=UTF-8 view.Main
```

Or run directly from your IDE by launching the `view.Main` class (it starts the Swing UI).

If you prefer to package first:

```bash
mvn package
java -cp target/classes -Dfile.encoding=UTF-8 view.Main
```

## Where to look in the code
- Main UI entry: `src/main/java/view/Main.java`
- Main window and views: `src/main/java/view/MainView.java`, `MedicamentoView.java`, `CajeroView.java`, `GerenteView.java`.
- Medicamentos persistence logic: `src/main/java/controller/MedicamentoController.java` (loads/saves `data/medicamentos.csv`).
- User management (in-memory): `src/main/java/controller/UserController.java`.

## Tips
- Make sure the process has write permission to the working directory so the `data` folder and CSV file can be created.
- If the app doesn't seem to load existing medicines, check the working directory and look for `data/medicamentos.csv`.

If you want I can add an example CSV file, improve CSV handling (quote values / support commas), or add Maven exec configuration to run the app with `mvn exec:java`.
