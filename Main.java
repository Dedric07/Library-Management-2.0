/*
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String[] mainMenu = {"Member Management", "Book Management", "Transaction Management", "Exit"};

        int mainChoice = JOptionPane.showOptionDialog(null, "Welcome! What do you want to manage?",
                "Library System", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, mainMenu, mainMenu[0]);

        if (mainChoice == 0) {
            showMemberMenu();
        } else if (mainChoice == 1) {
            showBookMenu();
        } else if (mainChoice == 2) {
            showTransactionMenu();  // New menu for Borrow & Return
        } else {
            JOptionPane.showMessageDialog(null, "Goodbye!");
        }
    }

    public static void showMemberMenu() {
        String[] menu = {"Add Member", "View Members", "Search Member", "Delete Member", "Update Member"};
        int choice = JOptionPane.showOptionDialog(null, "Please choose your choice:",
                "Member Management", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, menu, menu[0]);

        if (choice == 0) {
            Member.addMember();
        } else if (choice == 1) {
            Member.viewMember();
        } else if (choice == 2) {
            Member.searchMember();
        } else if (choice == 3) {
            Member.deleteMember();
        } else if (choice == 4) {
            Member.updateMember();
        } else {
            JOptionPane.showMessageDialog(null, "No option selected. Returning to main menu...");
        }
    }

    public static void showBookMenu() {
        String[] menu = {"Add Book", "View Books", "Search Book", "Delete Book", "Update Book"};
        int choice = JOptionPane.showOptionDialog(null, "Please choose your choice:",
                "Book Management", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, menu, menu[0]);

        if (choice == 0) {
            Book.addBook();
        } else if (choice == 1) {
            Book.viewBook();
        } else if (choice == 2) {
            Book.searchBook();
        } else if (choice == 3) {
            Book.deleteBook();
        } else if (choice == 4) {
            Book.updateBook();

        } else {
            JOptionPane.showMessageDialog(null, "No option selected. Returning to main menu...");
        }
    }

    public static void showTransactionMenu() {
        String[] menu = {"Borrow Book", "Return Book", "Delete Transaction", "View Transaction"};
        int choice = JOptionPane.showOptionDialog(null, "Please choose your transaction:",
                "Transaction Management", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, menu, menu[0]);

        if (choice == 0) {
            Transaction.borrowBook();
        } else if (choice == 1) {
            Transaction.returnBook();
        } else if (choice == 2) {
            Transaction.deleteTransaction();
        } else if (choice ==3 ){
            Transaction.viewTransaction();

        }
        else {
            JOptionPane.showMessageDialog(null, "No transaction selected. Returning to main menu...");
        }
    }
}
*/

