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

On Windows PowerShell, set the required variables for the current terminal
session before running `.\mvnw.cmd spring-boot:run`:

```powershell
$env:JWT_SECRET = "<generate-a-random-secret-of-at-least-32-bytes>"
$env:SAHTI_ADMIN_PASSWORD = "<choose-a-local-admin-password>"
```
