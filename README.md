# Automation Exercise Test Framework

## 📌 Project Overview
This project is a robust, scalable, and maintainable **Hybrid Test Automation Framework** designed for the [Automation Exercise](https://automationexercise.com/) e-commerce platform. It combines **UI automation** (using Playwright) and **API automation** (using Rest Assured) to ensure high quality and reliability of the application.

The framework follows the **Page Object Model (POM)** design pattern and utilizes **Data-Driven Testing** to separate test logic from test data.

## 🚀 Key Features
*   **Hybrid Approach:** Leverages API calls for fast data setup/teardown (e.g., creating/deleting users) and Playwright for UI validation.
*   **Page Object Model (POM):** Modular and readable code structure.
*   **Data-Driven:** Test data is externalized in JSON files.
*   **Cross-Browser Support:** Runs on Chromium, Firefox, and WebKit (via Playwright).
*   **Robust Reporting:** Integrated with **Allure Report** for detailed insights, screenshots, and logs.
*   **Parallel Execution:** Supports running tests in parallel using TestNG.
*   **CI/CD Ready:** Designed to be easily integrated into CI/CD pipelines.

## 🛠️ Technology Stack
*   **Language:** Java 21
*   **UI Automation:** [Playwright](https://playwright.dev/java/)
*   **API Automation:** [Rest Assured](https://rest-assured.io/)
*   **Test Runner:** [TestNG](https://testng.org/)
*   **Reporting:** [Allure Report](https://allurereport.org/)
*   **Build Tool:** Maven
*   **Logging:** Log4j2

## 📋 Prerequisites
Before running the tests, ensure you have the following installed:
*   **Java Development Kit (JDK) 21** or higher.
*   **Maven** (3.8+).
*   **IntelliJ IDEA** (Recommended IDE).

## ⚙️ Installation
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/your-repo-name.git
    ```
2.  **Navigate to the project directory:**
    ```bash
    cd Playwright-Project
    ```
3.  **Install dependencies:**
    ```bash
    mvn clean install -DskipTests
    ```
4.  **Install Playwright browsers:**
    ```bash
    mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"
    ```

## 🏃‍♂️ Running Tests

### Run All Tests
To run the entire test suite:
```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

### Run Specific Groups
You can run tests based on their functional groups using the provided XML suites:

*   **Regression:** `mvn clean test -DsuiteXmlFile=regression.xml`
*   **Checkout:** `mvn clean test -DsuiteXmlFile=checkout.xml`
*   **Login:** `mvn clean test -DsuiteXmlFile=login.xml`
*   **Registration:** `mvn clean test -DsuiteXmlFile=register.xml`
*   **Products:** `mvn clean test -DsuiteXmlFile=products.xml`
*   **Contact Us:** `mvn clean test -DsuiteXmlFile=contactus.xml`
*   **Invoice:** `mvn clean test -DsuiteXmlFile=invoice.xml`

## 📊 Generating Reports
After test execution, generate and view the Allure report using:

```bash
mvn allure:serve
```
This will start a local web server and open the report in your default browser.

## 📂 Project Structure
```
Playwright-Project
├── src/
│   ├── main/java/          # Source code
│   │   └── automationexercises/
│   │       ├── apis/       # API implementation classes
│   │       ├── pages/      # Page Object classes
│   │       ├── utils/      # Utility classes (JSON reader, etc.)
│   │       ├── FileUtils.java
│   │       └── PlaywrightManager.java
│   └── test/java/          # Test code
│       └── automationexercises/
│           ├── base/       # Base Test class
│           ├── listeners/  # TestNG listeners
│           └── tests/
│               └── ui/     # UI Test classes
├── src/test/resources/     # Test resources
│   ├── downloads/          # Downloaded files during tests
│   └── test-data/          # JSON data files
├── *.xml                   # TestNG Suite files (testng.xml, regression.xml, etc.)
├── pom.xml                 # Maven dependencies and build configuration
└── README.md               # Project documentation
```

## 👤 Author
**Omar Abdelaziz**
*   Graduation Project - ITI
