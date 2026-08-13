<div align="center">

# 🚀 Web Automation Framework with Grafana Integration

**A scalable Selenium Test Automation Framework** built with Java, TestNG & Maven — featuring full CI/CD integration, MySQL-backed execution history, Extent Reports, and real-time Grafana dashboards.

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Selenium](https://img.shields.io/badge/Selenium-43B02A?style=for-the-badge&logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-FF6B00?style=for-the-badge&logo=testinglibrary&logoColor=white)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)](https://www.jenkins.io/)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white)](https://grafana.com/)

⭐ **If you find this project useful, don't forget to star the repo!**

</div>

---

## 📌 Features

| | | |
|---|---|---|
| ✅ Selenium WebDriver Automation | ✅ ThreadLocal WebDriver Management | ✅ Retry Analyzer for Failed Tests |
| ✅ Java + TestNG Framework | ✅ Explicit Wait Utilities | ✅ Screenshot Capture (Pass & Fail) |
| ✅ Maven Build Management | ✅ Extent Reports w/ Screenshots | ✅ MySQL Database Integration |
| ✅ Jenkins CI/CD Integration | ✅ Grafana Dashboard Visualization | ✅ Historical Execution Tracking |
| ✅ Build-wise Report Storage | ✅ Dynamic Report Links | ✅ Modular, Scalable Architecture |

---

## 🛠 Tech Stack

| Category | Technology |
|---|---|
| **Language** | Java |
| **Automation** | Selenium WebDriver |
| **Testing** | TestNG |
| **Build Tool** | Maven |
| **Database** | MySQL |
| **Reporting** | Extent Reports |
| **Dashboard** | Grafana |
| **CI/CD** | Jenkins |
| **IDE** | Eclipse |
| **Version Control** | Git & GitHub |

---

## 🏗 Framework Architecture

```mermaid
flowchart TD
    A["🧑‍💻 Jenkins Trigger"] --> B["⚙️ Execute TestNG Suite"]
    B --> C["🌐 Selenium WebDriver"]

    C --> D["📑 Extent Report"]
    C --> E["📸 Screenshot"]
    C --> F["🗄️ MySQL Database"]

    D --> D1["HTML Report"]
    E --> E1["PNG Images"]
    F --> F1["test_results Table"]

    D1 --> G["📊 Grafana Dashboard"]
    F1 --> G

    G --> H["📈 Historical Test Analytics"]

    style A fill:#D24939,color:#fff
    style C fill:#43B02A,color:#fff
    style F fill:#4479A1,color:#fff
    style G fill:#F46800,color:#fff
    style H fill:#2E7D32,color:#fff
```

---

## 🔄 Test Execution Flow

```mermaid
sequenceDiagram
    participant J as Jenkins
    participant T as TestNG Suite
    participant W as WebDriver
    participant E as Extent Report
    participant DB as MySQL
    participant G as Grafana

    J->>T: Trigger build
    T->>W: Initialize & launch browser
    W->>W: Execute test case
    W->>E: Capture screenshots
    W->>E: Generate report
    W->>DB: Store execution results
    J->>J: Publish Jenkins artifacts
    G->>DB: Read execution data
    G->>G: Update dashboard
```

---

## 💾 Database Integration

Every executed test is automatically stored in MySQL for reporting and dashboard visualization.

**Captured Information**

- Run ID
- Test Name
- Status
- Browser
- Execution Duration
- Screenshot Path
- Exception Details
- Executed By
- Execution Timestamp
- Extent Report Path

---

## 📊 Grafana Dashboard

The Grafana dashboard reads execution data directly from MySQL and provides real-time analytics.

**Dashboard Features**

- 🥧 PASS / FAIL / SKIP Pie Chart
- 📅 Execution History
- 🌐 Browser Details
- ⏱️ Test Duration
- 📆 Execution Timeline
- 🔗 Direct Extent Report Links
- 📜 Historical Test Runs

---

## 📑 Extent Reports

The framework generates interactive Extent Reports after every execution.

**Features**

- Execution Timeline
- Test Logs
- Pass / Fail Status
- Embedded Screenshots
- Exception Stack Trace
- Execution Duration
- Test Categories

---

## 🗄 MySQL Test Results

Each execution is inserted into the database for reporting and dashboard visualization.

---

## ⚙ Jenkins Integration

Every Jenkins build automatically creates versioned reports.

```
Reports/
└── build_45/
    ├── latest/
    │   └── ExtentReport.html
    └── Run_20260813_121000/
        ├── spark/
        ├── screenshots/
        └── ExtentReport.html
```

**Benefits**

- Build-wise Report History
- Latest Report Shortcut
- Archived Reports
- Artifact Publishing
- Easy Report Sharing

---

## 📂 Project Structure

```
WebAutomationFramework
├── src
│   ├── main
│   │   └── java
│   │       └── com.base.framework
│   │           ├── BaseTest.java
│   │           ├── DriverManager.java
│   │           ├── ConfigReader.java
│   │           ├── DbManager.java
│   │           ├── ExtentManager.java
│   │           ├── ExtentReportListener.java
│   │           ├── RetryAnalyzer.java
│   │           ├── ScreenshotListener.java
│   │           ├── TakeScreenshot.java
│   │           └── WaitUtils.java
│   └── test
│       └── java
│           └── com.base.tests
│               ├── LoginTest.java
│               ├── InvalidLoginTest.java
│               └── DashboardValidationTest.java
├── Reports
├── Screenshots
├── pom.xml
└── testng.xml
```

---

## ▶ Running the Project

**Clone the repository**

```bash
git clone https://github.com/atharvajoshibsl/WebAutomationFramework.git
```

**Install dependencies**

```bash
mvn clean install
```

**Execute tests**

```bash
mvn test
```

**Run a specific TestNG suite**

```bash
mvn test -DsuiteXmlFile=testng.xml
```

---

## 📈 Sample Execution Workflow

```mermaid
flowchart LR
    A["👨‍💻 Developer"] --> B["📤 Git Push"]
    B --> C["🏗️ Jenkins Build"]
    C --> D["🧪 Selenium Tests"]
    D --> E["📑 Extent Report"]
    D --> F["🗄️ MySQL"]
    E --> G["📊 Grafana Dashboard"]
    F --> G
    G --> H["📈 Execution Analytics"]

    style A fill:#6f42c1,color:#fff
    style C fill:#D24939,color:#fff
    style D fill:#43B02A,color:#fff
    style F fill:#4479A1,color:#fff
    style G fill:#F46800,color:#fff
    style H fill:#2E7D32,color:#fff
```

---

## 🚀 Future Enhancements

- [ ] Parallel Execution
- [ ] Cross Browser Execution
- [ ] Docker Support
- [ ] Selenium Grid
- [ ] GitHub Actions CI/CD
- [ ] Email Notifications
- [ ] Slack Notifications
- [ ] Playwright Integration
- [ ] API Automation
- [ ] Allure Reporting
- [ ] Kubernetes Execution

---

## 🖼 Screenshots

### Grafana Dashboard
![Grafana Dashboard](docs/images/2026-08-13_12-03-51.png)

### Extent Report
![Extent Report](docs/images/2026-08-13_11-49-54.png)

### MySQL Test Results
![MySQL Test Results](docs/images/2026-08-13_12-16-10.png)

### Jenkins Artifacts
![Jenkins Artifacts](docs/images/2026-08-13_12-20-17.png)

---

## 👨‍💻 Author

**Atharva Joshi**
*Automation Test Engineer*

[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/atharvajoshibsl)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/atharva-joshi-a624641ba/)

---

<div align="center">

### ⭐ Star this repo if it helped you!

</div>