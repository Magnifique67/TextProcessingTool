package test;
import Util.TextProcessingUtil;
import java.util.ArrayList;

public class RegexOperationsTest {

    public static void main(String[] args) {
        TextProcessingUtil util = new TextProcessingUtil();

        // Test search operation
        String inputText = "Sample text with regex patterns";
        String regexPattern = "\\b\\w+\\b"; // Example regex pattern for word boundaries

        ArrayList<String> matches = util.search(inputText, regexPattern);
        System.out.println("Matches found: " + matches);

        // Test match operation
        String inputText2 = "Sample text to match";
        String regexPattern2 = "\\w+"; // Example regex pattern to match words

        boolean isMatch = util.match(inputText2, regexPattern2);
        System.out.println("Text matches pattern: " + isMatch);

        // Test replace operation
        String inputText3 = "Replace text with regex";
        String regexPattern3 = "text";
        String replacementText = "words";

        String replacedText = util.replace(inputText3, regexPattern3, replacementText);
        System.out.println("Replaced text: " + replacedText);
    }
}
