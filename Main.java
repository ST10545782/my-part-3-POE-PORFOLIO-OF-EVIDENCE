import java.io.File;
import java.util.List;
import java.util.Scanner;

/** Program entry point and menus. Run this class in NetBeans. */
public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    private static final MessageManager manager = new MessageManager();

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("       MESSAGE MANAGEMENT APP");
        System.out.println("====================================");
        while (true) {
            System.out.println("\nMAIN MENU");
            System.out.println("1. Add a message");
            System.out.println("2. Stored Messages menu (Part 3)");
            System.out.println("3. Display all sent messages");
            System.out.println("4. Exit");
            System.out.print("Choose 1-4: ");
            String choice = INPUT.nextLine().trim();
            if (choice.equals("1")) addMessage();
            else if (choice.equals("2")) storedMenu();
            else if (choice.equals("3")) displaySent();
            else if (choice.equals("4")) { System.out.println("Goodbye!"); break; }
            else System.out.println("Please type 1, 2, 3 or 4.");
        }
    }

    // Allows the user to populate the arrays instead of hard-coding app data.
    private static void addMessage() {
        System.out.print("Enter message ID (example: 1001): ");
        String id = INPUT.nextLine().trim();
        System.out.print("Enter recipient phone number: ");
        String recipient = INPUT.nextLine().trim();
        System.out.print("Type your message: ");
        String text = INPUT.nextLine().trim();
        System.out.println("Choose flag: 1=Sent, 2=Stored, 3=Disregard");
        System.out.print("Your choice: ");
        String f = INPUT.nextLine().trim();
        String flag = f.equals("1") ? "Sent" : f.equals("2") ? "Stored" : f.equals("3") ? "Disregard" : "";
        if (id.isEmpty() || recipient.isEmpty() || text.isEmpty() || flag.isEmpty()) {
            System.out.println("Nothing saved. Please enter all details and a valid flag."); return;
        }
        Message m = manager.addMessage(id, recipient, text, flag);
        System.out.println("Message added successfully.");
        System.out.println("Your message hash is: " + m.getHash());
    }

    private static void storedMenu() {
        while (true) {
            System.out.println("\nSTORED MESSAGES MENU");
            System.out.println("1. Display sender/developer and recipient of stored messages");
            System.out.println("2. Display the longest message");
            System.out.println("3. Search by message ID");
            System.out.println("4. Search messages by recipient");
            System.out.println("5. Delete a message using its hash");
            System.out.println("6. Display full message report");
            System.out.println("7. Load stored messages from stored_messages.json");
            System.out.println("8. Back to main menu");
            System.out.print("Choose 1-8: ");
            String choice = INPUT.nextLine().trim();
            switch (choice) {
                case "1": manager.displayStoredMessages(); break;
                case "2":
                    Message longest = manager.getLongestMessage();
                    System.out.println(longest == null ? "No messages yet." : longest.getText()); break;
                case "3":
                    System.out.print("Enter message ID: ");
                    Message byId = manager.searchById(INPUT.nextLine().trim());
                    System.out.println(byId == null ? "Message not found." : "Recipient: " + byId.getRecipient() + "\nMessage: " + byId.getText()); break;
                case "4":
                    System.out.print("Enter recipient number: ");
                    List<Message> found = manager.searchByRecipient(INPUT.nextLine().trim());
                    if (found.isEmpty()) System.out.println("No messages found for that recipient.");
                    else for (Message m : found) System.out.println(m.getText()); break;
                case "5":
                    System.out.print("Paste the message hash: ");
                    System.out.println(manager.deleteByHash(INPUT.nextLine().trim()) ? "Message successfully deleted." : "No message has that hash."); break;
                case "6": manager.displayReport(); break;
                case "7":
                    try { int n = manager.loadStoredMessagesFromJson(new File("stored_messages.json")); System.out.println(n + " message(s) loaded."); }
                    catch (Exception ex) { System.out.println("Could not load JSON. Keep stored_messages.json in the project folder. Details: " + ex.getMessage()); } break;
                case "8": return;
                default: System.out.println("Please type a number from 1 to 8.");
            }
        }
    }

    private static void displaySent() {
        if (manager.getSentMessages().isEmpty()) { System.out.println("No sent messages yet."); return; }
        for (Message m : manager.getSentMessages()) System.out.println(m);
    }
}
