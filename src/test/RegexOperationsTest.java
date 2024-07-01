package test;
import Util.TextProcessingUtil;
import java.util.ArrayList;

public class RegexOperationsTest {

    public static void main(String[] args) {
        TextProcessingUtil util = new TextProcessingUtil();

        // Test search operation
        String inputText = "Sample text";
        String regexPattern = "\\w+"; // Example regex pattern for word boundaries

        ArrayList<String> matches = util.search(inputText, regexPattern);
        System.out.println("Matches found: " + matches);

        // Test match operation
        String inputText2 = "Sample_123";
        String regexPattern2 = "\\w+"; // Example regex pattern to match words

        boolean isMatch = util.match(inputText2, regexPattern2);
        System.out.println("Text matches pattern: " + isMatch);

        // Test replace operation
        String inputText3 = "Hello_123";
        String regexPattern3 = "\\d+";
        String replacementText = "world";

        String replacedText = util.replace(inputText3, regexPattern3, replacementText);
        System.out.println("Replaced text: " + replacedText);
    }
}
