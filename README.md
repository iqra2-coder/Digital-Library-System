A simple yet comprehensive Java Swing-based Digital Library Management System that facilitates book management, issuing/returning, advance reservations, reporting, and user queries, designed for academic projects and beginner-level Java GUI learning. No external database is required – all data is managed in-memory using Java collections.

✅ Features
🔐 Login System
Separate login for Admin and User roles.

Predefined sample users (admin/admin123, user/user123).

📚 Admin Functionalities
➕ Add new books with title and author.

📈 Generate library reports:

Total books

Issued books

Reserved books

Issued book details by user

💬 View user queries submitted through the system.

👤 User Functionalities
📖 View available books

📥 Issue books (limitless, no check constraint)

📤 Return books and auto-calculate late fine (₹10/day after 7 days)

🔎 Search books by title or author

📌 Advance reservation for already issued books

✉️ Send email-style queries to admin (viewable via admin panel)

🛠️ Tech Stack
Language: Java

GUI: Swing (Java Foundation Classes)

Data Storage: In-memory (ArrayList, HashMap)

Tools: NetBeans or any Java IDE

