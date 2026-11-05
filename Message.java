import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** One message in the application. */
public class Message {
    private final String id;
    private final String recipient;
    private final String text;
    private final String flag; // Sent, Stored, or Disregard
    private final String hash;

    public Message(String id, String recipient, String text, String flag) {
        this.id = id;
        this.recipient = recipient;
        this.text = text;
        this.flag = flag;
        this.hash = createHash(id + "|" + recipient + "|" + text);
    }

    private String createHash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) result.append(String.format("%02x", b));
            return result.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is not available", ex);
        }
    }

    public String getId() { return id; }
    public String getRecipient() { return recipient; }
    public String getText() { return text; }
    public String getFlag() { return flag; }
    public String getHash() { return hash; }

    @Override
    public String toString() {
        return "ID: " + id + " | Recipient: " + recipient + " | Flag: " + flag
                + " | Message: " + text + " | Hash: " + hash;
    }
}
