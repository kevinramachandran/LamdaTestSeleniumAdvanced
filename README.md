# LambdaTest Selenium Advanced Automation – Grid Testing Project

This project demonstrates cross-browser testing using Selenium 4 Grid with JUnit 5.

## 🚀 Features

- Selenium WebDriver with Remote Grid setup
- Parameterized browser testing: Chrome (Windows 10), Edge (macOS Ventura)
- Multiple tab/window handling
- Dynamic waits and scrolling
- Integration verification
- Runs inside Gitpod IDE

## 📦 Tech Stack

- Java 17
- Gradle
- JUnit 5
- Selenium 4
- Gitpod for cloud dev environment

---

## ⚙️ How to Run in Gitpod

1. Click this button to launch in Gitpod:

   [![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/kevinramachandran/LamdaTestSeleniumAdvanced)

2. Gitpod will:
   - Pull the image
   - Run `./gradlew build` and `./gradlew test`
   - Launch the project

3. For Selenium Grid:
   - Make sure Selenium Grid is running at `http://localhost:4444/wd/hub`
   - Or configure to run against LambdaTest cloud grid if needed

---

## 📝 Notes

- Make sure to update the Grid URL if you're using LambdaTest or a cloud provider.
- For enabling logs/video/screenshots, update `DesiredCapabilities` with:
  ```java
  caps.setCapability("video", true);
  caps.setCapability("console", true);
  caps.setCapability("network", true);
