# SA7TI backend local setup

The application expects a local MySQL database named `sahti`. Configure the
database connection and required credentials through environment variables
before starting the backend. Do not commit real values or files uploaded by
users.

Required variables:

- `JWT_SECRET`: a random signing key of at least 32 bytes
- `SAHTI_ADMIN_PASSWORD`: password for the seeded admin account

Optional variables:

- `DB_URL` (defaults to the local `sahti` database)
- `DB_USERNAME` (defaults to `root`)
- `DB_PASSWORD` (defaults to empty)
- `SAHTI_ADMIN_EMAIL` (defaults to `admin@sahti.tn`)
- `SAHTI_UPLOAD_DIR` (defaults to `uploads`)
- `MAIL_USERNAME` and `MAIL_PASSWORD` (needed only for real email delivery)

On Windows PowerShell, set the required variables for the current terminal
session before running `.\mvnw.cmd spring-boot:run`:

```powershell
$env:JWT_SECRET = "<generate-a-random-secret-of-at-least-32-bytes>"
$env:SAHTI_ADMIN_PASSWORD = "<choose-a-local-admin-password>"
```

Create the `sahti` database before the first launch. Hibernate updates the
tables on startup, and the backend seeds a small Tunisian food catalog for
demonstration. The shared client APIs are:

- `GET/PUT /api/profile`
- `GET /api/foods`
- `GET/POST/DELETE /api/meals`
- `GET/POST/DELETE /api/activities`
- `GET /api/progress`

Profile, meal, activity, and progress endpoints require a client JWT. Meal
calories are estimates derived from the selected catalog food and quantity.
