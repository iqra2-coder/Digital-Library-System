import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DigitalLibrarySystem {
    public static void main(String[] args) {
        LoginScreen loginScreen = new LoginScreen();
    }
}

class Book {
    String title, author;
    boolean isIssued;
    Date issueDate;
    String reservedBy;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isIssued = false;
        this.issueDate = null;
        this.reservedBy = null;
    }
}

class User {
    String username, password;
    boolean isAdmin;

    public User(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }
}

class LibraryData {
    static ArrayList<Book> books = new ArrayList<>();
    static ArrayList<User> users = new ArrayList<>();
    static HashMap<String, ArrayList<Book>> issuedBooks = new HashMap<>();
    static ArrayList<String> queries = new ArrayList<>();

    static {
        boolean add = users.add(new User("admin", "admin123", true));
        users.add(new User("user", "user123", false));
        boolean add1 = books.add(new Book("Java Programming", "John Doe"));
        boolean add2 = books.add(new Book("Python Basics", "Jane Smith"));
    }
}

class LoginScreen extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;

    public LoginScreen() {
        setTitle("Digital Library - Login");
        setSize(350, 200);
        setLayout(new GridLayout(4, 2));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(this);
        add(loginBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String uname = usernameField.getText().trim();
        String pwd = new String(passwordField.getPassword()).trim();

        for (User u : LibraryData.users) {
            if (u.username.equals(uname) && u.password.equals(pwd)) {
                dispose();
                if (u.isAdmin) new AdminPanel();
                else new UserPanel(uname);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Invalid credentials!");
    }
}

class AdminPanel extends JFrame implements ActionListener {
    JTextField titleField, authorField;

    public AdminPanel() {
        setTitle("Admin Dashboard");
        setSize(400, 400);
        setLayout(new GridLayout(7, 1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        titleField = new JTextField();
        authorField = new JTextField();

        add(new JLabel("Add Book Title:"));
        add(titleField);
        add(new JLabel("Author:"));
        add(authorField);

        JButton addBtn = new JButton("Add Book");
        addBtn.addActionListener(this);
        add(addBtn);

        JButton reportBtn = new JButton("View Reports");
        reportBtn.addActionListener(e -> showReports());
        add(reportBtn);

        JButton queryBtn = new JButton("View Queries");
        queryBtn.addActionListener(e -> viewQueries());
        add(queryBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        if (!title.isEmpty() && !author.isEmpty()) {
            LibraryData.books.add(new Book(title, author));
            JOptionPane.showMessageDialog(this, "Book added successfully.");
        }
    }

    private void showReports() {
        StringBuilder sb = new StringBuilder();
        sb.append("Total Books: ").append(LibraryData.books.size()).append("\n");
        long issued = LibraryData.books.stream().filter(b -> b.isIssued).count();
        long reserved = LibraryData.books.stream().filter(b -> b.reservedBy != null).count();
        sb.append("Issued Books: ").append(issued).append("\n");
        sb.append("Reserved Books: ").append(reserved).append("\n\n");

        for (Map.Entry<String, ArrayList<Book>> entry : LibraryData.issuedBooks.entrySet()) {
            sb.append("User: ").append(entry.getKey()).append("\n");
            for (Book b : entry.getValue()) {
                sb.append("  - ").append(b.title).append("\n");
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void viewQueries() {
        if (LibraryData.queries.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No queries received.");
            return;
        }
        StringBuilder sb = new StringBuilder("User Queries:\n\n");
        for (String q : LibraryData.queries) sb.append("- ").append(q).append("\n");
        JOptionPane.showMessageDialog(this, sb.toString());
    }
}

class UserPanel extends JFrame {
    String currentUser;
    JComboBox<String> bookList;

    public UserPanel(String user) {
        this.currentUser = user;
        setTitle("User Dashboard");
        setSize(400, 400);
        setLayout(new GridLayout(6, 1));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        bookList = new JComboBox<>();
        refreshBookList();
        add(bookList);

        JButton issueBtn = new JButton("Issue Book");
        issueBtn.addActionListener(e -> issueBook());
        add(issueBtn);

        JButton returnBtn = new JButton("Return Book");
        returnBtn.addActionListener(e -> returnBook());
        add(returnBtn);

        JButton searchBtn = new JButton("Search Books");
        searchBtn.addActionListener(e -> searchBooks());
        add(searchBtn);

        JButton reserveBtn = new JButton("Advance Book Reservation");
        reserveBtn.addActionListener(e -> reserveBook());
        add(reserveBtn);

        JButton queryBtn = new JButton("Send Query");
        queryBtn.addActionListener(e -> emailQuery());
        add(queryBtn);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void refreshBookList() {
        bookList.removeAllItems();
        for (Book b : LibraryData.books) {
            bookList.addItem(b.title);
        }
    }

    private void issueBook() {
        String selected = (String) bookList.getSelectedItem();
        for (Book b : LibraryData.books) {
            if (b.title.equals(selected)) {
                if (!b.isIssued) {
                    b.isIssued = true;
                    b.issueDate = new Date();
                    LibraryData.issuedBooks.putIfAbsent(currentUser, new ArrayList<>());
                    LibraryData.issuedBooks.get(currentUser).add(b);
                    JOptionPane.showMessageDialog(this, "Book issued successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Book already issued. Use Reserve option.");
                }
                return;
            }
        }
    }

    private void returnBook() {
        ArrayList<Book> books = LibraryData.issuedBooks.get(currentUser);
        if (books == null || books.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No issued books found.");
            return;
        }

        String[] titles = books.stream().map(b -> b.title).toArray(String[]::new);
        String selected = (String) JOptionPane.showInputDialog(this, "Select book to return:", "Return Book", JOptionPane.PLAIN_MESSAGE, null, titles, titles[0]);
        if (selected != null) {
            Iterator<Book> it = books.iterator();
            while (it.hasNext()) {
                Book b = it.next();
                if (b.title.equals(selected)) {
                    b.isIssued = false;
                    long diff = (new Date().getTime() - b.issueDate.getTime()) / (1000 * 60 * 60 * 24);
                    if (diff > 7) {
                        long fine = (diff - 7) * 10;
                        JOptionPane.showMessageDialog(this, "Returned late. Fine: ₹" + fine);
                    } else {
                        JOptionPane.showMessageDialog(this, "Returned on time. No fine.");
                    }
                    b.issueDate = null;
                    it.remove();
                    return;
                }
            }
        }
    }

    private void searchBooks() {
        String keyword = JOptionPane.showInputDialog(this, "Enter keyword:");
        StringBuilder sb = new StringBuilder("Search Results:\n");
        for (Book b : LibraryData.books) {
            if (b.title.toLowerCase().contains(keyword.toLowerCase()) || b.author.toLowerCase().contains(keyword.toLowerCase())) {
                sb.append(b.title).append(" by ").append(b.author);
                sb.append(b.isIssued ? " (Issued)\n" : " (Available)\n");
            }
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void reserveBook() {
        String selected = (String) bookList.getSelectedItem();
        for (Book b : LibraryData.books) {
            if (b.title.equals(selected)) {
                if (b.isIssued) {
                    b.reservedBy = currentUser;
                    JOptionPane.showMessageDialog(this, "Book reserved. You'll be notified when it's returned.");
                } else {
                    JOptionPane.showMessageDialog(this, "Book is available. No need to reserve.");
                }
                return;
            }
        }
    }

    private void emailQuery() {
        String msg = JOptionPane.showInputDialog(this, "Enter your query:");
        if (msg != null && !msg.trim().isEmpty()) {
            LibraryData.queries.add("From " + currentUser + ": " + msg);
            JOptionPane.showMessageDialog(this, "Query submitted successfully.");
        }
    }
}
