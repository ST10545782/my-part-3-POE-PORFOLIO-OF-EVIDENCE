
public class MessageManagerTest {
    private static int testsPassed = 0;

    public static void main(String[] args) {
        MessageManager manager = new MessageManager();
        manager.addMessage("1", "+27834557896", "Did you get the cake?", "Sent");
        Message second = manager.addMessage("2", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored");
        manager.addMessage("3", "+27834484567", "Yoooooo, I am at your gate.", "Disregard");
        manager.addMessage("08388884567", "Developer", "It is dinner time!", "Sent");
        manager.addMessage("5", "+27838884567", "OK, I am leaving without you.", "Stored");

        assertEquals("Sent messages count", 2, manager.getSentMessages().size());
        assertEquals("Disregarded messages count", 1, manager.getDisregardedMessages().size());
        assertEquals("Stored messages count", 2, manager.getStoredMessages().size());
        assertEquals("Longest message", second.getText(), manager.getLongestMessage().getText());
        assertEquals("Search ID recipient", "Developer", manager.searchById("08388884567").getRecipient());
        assertEquals("Search ID message", "It is dinner time!", manager.searchById("08388884567").getText());
        assertEquals("Search recipient count", 2, manager.searchByRecipient("+27838884567").size());
        assertEquals("Delete by hash", true, manager.deleteByHash(second.getHash()));
        assertEquals("Deleted message no longer found", null, manager.searchById("2"));
        assertEquals("Report data remains", 4, manager.allMessages().size());
        System.out.println("\nRESULT: " + testsPassed + "/10 tests passed.");
    }

    // assertEquals helper: compares expected result with actual result.
    private static void assertEquals(String testName, Object expected, Object actual) {
        if (expected == null ? actual == null : expected.equals(actual)) {
            testsPassed++;
            System.out.println("PASS: " + testName);
        } else {
            System.out.println("FAIL: " + testName + " | Expected: " + expected + " | Actual: " + actual);
        }
    }
}
