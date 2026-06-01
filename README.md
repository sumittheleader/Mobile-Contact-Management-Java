# Mobile Contact Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)
![Swing](https://img.shields.io/badge/Java%20Swing-GUI-blue?style=for-the-badge)
![OOP](https://img.shields.io/badge/OOP-Concepts-green?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Completed-brightgreen?style=for-the-badge)

A professional desktop-based **Mobile Contact Management System** built using **Java Swing**.
This application allows users to add, view, search, update, and delete contacts through a clean and user-friendly graphical interface.

---

## 📌 Project Overview

The **Mobile Contact Management System** is a Java GUI application designed to manage personal and professional contacts efficiently.
It provides a simple interface where users can store contact details such as name, mobile number, email, address, and category.

The application uses **Object-Oriented Programming**, **Java Swing**, **event handling**, **input validation**, and **file-based data persistence** to provide a complete desktop application experience.

---

## ✨ Features

* Add new contacts with name, mobile number, email, address, and category
* View all saved contacts in a structured table format
* Search contacts by name, mobile number, email, or category
* Edit and update existing contact details
* Delete selected contacts
* Prevent duplicate mobile numbers
* Validate mobile numbers and email addresses
* Categorize contacts as Personal, Work, Family, Friends, Business, or Other
* Store contact data locally using file handling and object serialization
* Display total contact count
* Double-click contact row to view complete contact details
* Clean and attractive Java Swing user interface

---

## 🛠️ Tech Stack

| Technology           | Purpose                            |
| -------------------- | ---------------------------------- |
| Java                 | Core programming language          |
| Java Swing           | GUI development                    |
| AWT                  | Event handling and UI support      |
| File Handling        | Local data storage                 |
| Object Serialization | Saving and loading contact objects |
| OOP Concepts         | Clean and structured code design   |

---

## 🧠 OOP Concepts Used

* **Class and Object** – Used to represent contacts and application logic
* **Encapsulation** – Contact data is kept private and accessed using getters and setters
* **Constructor** – Used to initialize contact objects
* **Inheritance** – Main class extends `JFrame`
* **Abstraction** – Contact operations are handled through a separate manager class
* **Serialization** – Used to save contact data permanently in a local file
* **Event Handling** – Buttons, search field, and table actions are handled using listeners

---

## 📁 Folder Structure

```text id="smnud9"
Mobile-Contact-Management-Java/
│
├── src/
│   └── MobileContactManager.java
│
├── README.md
└── .gitignore
```

---

## ⚙️ Prerequisites

Before running this project, make sure you have:

* Java JDK installed
* VS Code, IntelliJ IDEA, Eclipse, or any Java-supported IDE
* Basic knowledge of Java and Swing

Check Java installation:

```bash id="i57mae"
java -version
```

Check Java compiler:

```bash id="xj3uc5"
javac -version
```

---

## 🚀 How to Run the Project

### 1. Clone the Repository

```bash id="jglhw1"
git clone https://github.com/your-username/Mobile-Contact-Management-Java.git
```

### 2. Open the Project Folder

```bash id="5a6g0q"
cd Mobile-Contact-Management-Java
```

### 3. Compile the Java File

```bash id="pyp13g"
javac src/MobileContactManager.java
```

### 4. Run the Application

```bash id="a2jx9n"
java -cp src MobileContactManager
```

---

## 📌 Important Note

The Java file name must be exactly:

```text id="m8784r"
MobileContactManager.java
```

This is important because the public class name in the program is:

```java id="0g506b"
public class MobileContactManager
```

If the file name is different, Java will show a compilation error.

---

## 💾 Data Storage

The application stores contact data locally in a file named:

```text id="ky844x"
contacts.dat
```

This file is automatically created when contacts are saved.
It should not be uploaded to GitHub because it contains user-generated data.

---

## 🚫 Suggested .gitignore

Create a `.gitignore` file and add the following content:

```gitignore id="o0o0bu"
*.class
contacts.dat
bin/
out/
target/
.vscode/
.idea/
*.log
.DS_Store
```

---

## 📊 Main Functional Modules

### Contact Module

Handles contact details such as name, mobile number, email, address, category, created date, and modified date.

### Contact Manager Module

Handles business logic such as adding, updating, deleting, searching, saving, and loading contacts.

### GUI Module

Provides the graphical interface using Java Swing components such as `JFrame`, `JTable`, `JDialog`, `JButton`, and `JTextField`.

---

## 🎯 Project Objective

The main objective of this project is to develop a desktop-based contact management application using Java.
This project demonstrates practical implementation of GUI programming, object-oriented programming, event handling, validation, and file-based data persistence.

---

## 🔮 Future Enhancements

* Add login and authentication system
* Add contact profile images
* Export contacts to CSV or PDF
* Import contacts from external files
* Add database support using MySQL or SQLite
* Add dark mode
* Add advanced search and filter options
* Add backup and restore functionality

---

## 👨‍💻 Author

**Sumit Sharma**
B.Tech CSE Student

---

## 📄 License

This project is created for learning and academic purposes.

---

## ⭐ Support

If you like this project, give it a star on GitHub.
