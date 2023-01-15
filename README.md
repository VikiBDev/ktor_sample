# ktor_sample
A sample Ktor application extracted from a startup project I'm helping get off the ground. The only feature module that I want to showcase is called "points", the other module just contain some simple classes needed for the E2E tests to run successfully.

The main goal behind the whole architecture is to have loosely coupled modules, which will allow a painless transition to a microservices architecture (if it'll ever be needed) in the future. Please note that I used the words "loosely coupled", not "completely decoupled". I had to live with some minor trade-offs in order to make the development of the current mvp as smooth as possible, without introducing too many contraints that would be needed in order to achieve a perfect separation. This is the reason why the "points" module can't work by itself, but needs some info from the modules it interacts with. 

SOLID principles are applied throughout the code, with a E2E testing strategy that's used to reduce the development time to the minimum, while also assuring high quality. Unit tests on the services/repos don't cover the whole codebase at the moment, but are trivial to write since both services and repositories are 100% pure kotlin classes, without external dependencies.

## Requirements:
- Docker Desktop
- Postgresql version 14.6
- Java 17

## How to run

The setup process is quite simple when using IntelliJ Idea. The first step is to clone the project and open it in IntelliJ. The IDE will automatically download all the Gradle dependencies, it might take a couple of minutes.

The only manual setup required is setting up some env variables, the mandatory ones are:
- MAIN_DB_URL: url pointing to the local Postgres DB
- MAIN_DB_USERNAME: username for the local Postgres DB
- MAIN_DB_PSW: password for the local Postgres DB
- OAS_USER: username used to protect the /api-docs endpoint via basic auth
- OAS_PSW: password used to protect the /api-docs endpoint via basic auth
- USER_SESSION_ENCRYPT_KEY: 32 bytes random string used to encrypt session cookies
- USER_SESSION_SIGN_KEY: 32 bytes random string used to sign session cookies

Once everything is ready and indexed, there are two useful tasks that can be run to demo the app: run all the E2E tests (powered by Docker containers), or run the app locally.

### Run the app locally
Make sure that Postgres is running, then navigate to the folder where you cloned the project, and run .\gradlew.bat run (or .\gradlew run if running on Mac or Linux). The app will automatically run all the required DB migrations, and then start listening on the address http://127.0.0.1:8080. There's not much that can be done in this project slice, the only interesting UI is available at the http://127.0.0.1:8080/api-docs url. It displays how the Open API specification that's automatically generated is served directly from the app. Don't forget that, in order to access that page, you'll have to enter the OAS username/password that were setup in the env variables.

### Run the E2E tests
Make sure that Docker Desktop is running, then navigate to the folder where you cloned the project, and run .\gradlew.bat test (or .\gradlew test if running on Mac or Linux). A "BUILD SUCCESSFUL" message indicates that all the tests run successfully.

If you prefer a more UI oriented solution, E2E tests can also be run via IntelliJ, by navigating to the test/kotlin folder using the inspector panel on the left, right clicking on it, and selecting "Run tests in 'main-backend'".
