import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Stores messages in the five arrays/lists required by the assignment. */
public class MessageManager {
    private final List<Message> sentMessages = new ArrayList<Message>();
    private final List<Message> disregardedMessages = new ArrayList<Message>();
    private final List<Message> storedMessages = new ArrayList<Message>();
    private final List<String> messageHashes = new ArrayList<String>();
    private final List<String> messageIds = new ArrayList<String>();

    public Message addMessage(String id, String recipient, String text, String flag) {
        Message message = new Message(id, recipient, text, flag);
        if ("Sent".equalsIgnoreCase(flag)) sentMessages.add(message);
        else if ("Stored".equalsIgnoreCase(flag)) storedMessages.add(message);
        else if ("Disregard".equalsIgnoreCase(flag)) disregardedMessages.add(message);
        else throw new IllegalArgumentException("Flag must be Sent, Stored, or Disregard.");
        messageHashes.add(message.getHash());
        messageIds.add(message.getId());
        return message;
    }

    public List<Message> getSentMessages() { return Collections.unmodifiableList(sentMessages); }
    public List<Message> getDisregardedMessages() { return Collections.unmodifiableList(disregardedMessages); }
    public List<Message> getStoredMessages() { return Collections.unmodifiableList(storedMessages); }
    public List<String> getMessageHashes() { return Collections.unmodifiableList(messageHashes); }
    public List<String> getMessageIds() { return Collections.unmodifiableList(messageIds); }

    // a. Display the sender/developer and recipient of all stored messages.
    public void displayStoredMessages() {
        if (storedMessages.isEmpty()) { System.out.println("There are no stored messages yet."); return; }
        for (Message m : storedMessages)
            System.out.println("ID: " + m.getId() + " | Recipient: " + m.getRecipient());
    }

    // b. Return the longest message from the messages currently in memory.
    public Message getLongestMessage() {
        Message longest = null;
        for (Message m : allMessages())
            if (longest == null || m.getText().length() > longest.getText().length()) longest = m;
        return longest;
    }

    // c. Search for an ID and show its recipient and message.
    public Message searchById(String id) {
        for (Message m : allMessages()) if (m.getId().equals(id)) return m;
        return null;
    }

    // d. Return every message stored for a recipient.
    public List<Message> searchByRecipient(String recipient) {
        List<Message> found = new ArrayList<Message>();
        for (Message m : allMessages()) if (m.getRecipient().equals(recipient)) found.add(m);
        return found;
    }

    // e. Delete a message using its unique SHA-256 message hash.
    public boolean deleteByHash(String hash) {
        boolean removed = removeFrom(sentMessages, hash);
        removed = removeFrom(disregardedMessages, hash) || removed;
        removed = removeFrom(storedMessages, hash) || removed;
        if (removed) {
            messageHashes.remove(hash);
            messageIds.clear();
            for (Message m : allMessages()) messageIds.add(m.getId());
        }
        return removed;
    }

    private boolean removeFrom(List<Message> list, String hash) {
        Iterator<Message> it = list.iterator();
        while (it.hasNext()) if (it.next().getHash().equals(hash)) { it.remove(); return true; }
        return false;
    }

    // f. Print a report containing every message's hash, recipient and text.
    public void displayReport() {
        if (allMessages().isEmpty()) { System.out.println("No messages to report."); return; }
        for (Message m : allMessages()) {
            System.out.println("Message Hash: " + m.getHash());
            System.out.println("Recipient: " + m.getRecipient());
            System.out.println("Message: " + m.getText());
            System.out.println("----------------------------------------");
        }
    }

    public List<Message> allMessages() {
        List<Message> all = new ArrayList<Message>();
        all.addAll(sentMessages); all.addAll(disregardedMessages); all.addAll(storedMessages);
        return all;
    }

    /** Loads a simple JSON array containing objects with id, recipient, message and flag. */
    public int loadStoredMessagesFromJson(File file) throws IOException {
        StringBuilder json = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try { String line; while ((line = reader.readLine()) != null) json.append(line); }
        finally { reader.close(); }
        Pattern objectPattern = Pattern.compile("\\{([^}]*)\\}");
        Matcher objects = objectPattern.matcher(json.toString());
        int count = 0;
        while (objects.find()) {
            String object = objects.group(1);
            String id = jsonValue(object, "id");
            String recipient = jsonValue(object, "recipient");
            String message = jsonValue(object, "message");
            String flag = jsonValue(object, "flag");
            if (id != null && recipient != null && message != null && flag != null) {
                addMessage(id, recipient, message, flag); count++;
            }
        }
        return count;
    }

    private String jsonValue(String object, String key) {
        Pattern p = Pattern.compile("\\\"" + key + "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"");
        Matcher m = p.matcher(object);
        if (!m.find()) return null;
        return m.group(1).replace("\\\"", "\"").replace("\\n", "\n").replace("\\\\", "\\");
    }
}
