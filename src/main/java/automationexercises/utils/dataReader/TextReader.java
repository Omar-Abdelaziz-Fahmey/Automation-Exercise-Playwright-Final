package automationexercises.utils.dataReader;

import automationexercises.utils.logs.LogsManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReader {
    private final String TEST_DATA_PATH = "src/test/resources/test-data/";
    private Map<String, String> textData;
    private String textFileName;
    private String delimiter;
    private String rawContent;

    /**
     * Constructor for TextReader with default delimiter "="
     * @param textFileName name of the text file without extension
     */
    public TextReader(String textFileName) {
        this(textFileName, "=");
    }

    /**
     * Constructor for TextReader with custom delimiter
     * @param textFileName name of the text file without extension
     * @param delimiter custom delimiter for key-value pairs
     */
    public TextReader(String textFileName, String delimiter) {
        this.textFileName = textFileName;
        this.delimiter = delimiter;
        this.textData = new HashMap<>();
        loadTextFile();
    }

    /**
     * Loads the text file and stores both parsed data and raw content
     */
    private void loadTextFile() {
        try (BufferedReader reader = new BufferedReader(
                new FileReader(TEST_DATA_PATH + textFileName + ".txt"))) {

            StringBuilder contentBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                contentBuilder.append(line).append(System.lineSeparator());

                // Skip empty lines and comments (lines starting with #)
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }

                // Parse key-value pairs if delimiter exists
                String[] parts = line.split(delimiter, 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    textData.put(key, value);
                }
            }

            rawContent = contentBuilder.toString();
            LogsManager.info("Successfully loaded text file:", textFileName);

        } catch (IOException e) {
            LogsManager.error("Error reading text file:", textFileName, e.getMessage());
            rawContent = "";
        }
    }

    /**
     * Extracts price as integer from text content
     * @return the price as integer, or 0 if not found
     */
    public int extractPrice() {
        return extractPrice(rawContent);
    }

    /**
     * Extracts price as integer from a specific text
     * @param text the text to extract price from
     * @return the price as integer, or 0 if not found
     */
    public int extractPrice(String text) {
        try {
            // Multiple regex patterns to match different price formats
            String[] patterns = {
                    "(?:total purchase amount is|amount is|total is|price is|cost is)\\s*\\$?([0-9,]+\\.?[0-9]*)",
                    "\\$([0-9,]+\\.?[0-9]*)",
                    "(?:USD|EUR|GBP)?\\s*([0-9,]+\\.?[0-9]*)",
                    "([0-9,]+\\.?[0-9]*)\\s*(?:dollars|euros|pounds)"
            };

            for (String patternString : patterns) {
                Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(text);

                if (matcher.find()) {
                    String priceStr = matcher.group(1).replaceAll(",", "");
                    // Convert to integer, removing decimal part if exists
                    int price = (int) Double.parseDouble(priceStr);
                    return price;
                }
            }

            LogsManager.warn("No price found in text");
            return 0;

        } catch (Exception e) {
            LogsManager.error("Error extracting price from text", e.getMessage());
            return 0;
        }
    }

    /**
     * Extracts price as String (for cases where you need the original format)
     * @return the price as String, or empty string if not found
     */
    public String extractPriceAsString() {
        return extractPriceAsString(rawContent);
    }

    /**
     * Extracts price as String from a specific text
     * @param text the text to extract price from
     * @return the price as String, or empty string if not found
     */
    public String extractPriceAsString(String text) {
        try {
            String[] patterns = {
                    "(?:total purchase amount is|amount is|total is|price is|cost is)\\s*\\$?([0-9,]+\\.?[0-9]*)",
                    "\\$([0-9,]+\\.?[0-9]*)",
                    "(?:USD|EUR|GBP)?\\s*([0-9,]+\\.?[0-9]*)",
                    "([0-9,]+\\.?[0-9]*)\\s*(?:dollars|euros|pounds)"
            };

            for (String patternString : patterns) {
                Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(text);

                if (matcher.find()) {
                    String price = matcher.group(1).replaceAll(",", "");
                    LogsManager.info("Extracted price string:", price);
                    return price;
                }
            }

            LogsManager.warn("No price found in text");
            return "";

        } catch (Exception e) {
            LogsManager.error("Error extracting price string from text", e.getMessage());
            return "";
        }
    }

    /**
     * Extracts all prices as integers from the text
     * @return List of all prices found as integers
     */
    public List<Integer> extractAllPrices() {
        return extractAllPrices(rawContent);
    }

    /**
     * Extracts all prices as integers from a specific text
     * @param text the text to extract prices from
     * @return List of all prices found as integers
     */
    public List<Integer> extractAllPrices(String text) {
        List<Integer> prices = new ArrayList<>();

        try {
            // Pattern to match various number formats that could be prices
            String pattern = "(?:amount is|total is|price is|cost is)?\\s*\\$?([0-9,]+\\.?[0-9]*)";
            Pattern regexPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regexPattern.matcher(text);

            while (matcher.find()) {
                String priceStr = matcher.group(1).replaceAll(",", "");
                int price = (int) Double.parseDouble(priceStr);
                prices.add(price);
            }

        } catch (Exception e) {
            LogsManager.error("Error extracting all prices from text", e.getMessage());
        }

        return prices;
    }

    /**
     * Extracts numeric value as integer with a specific keyword
     * @param keyword the keyword to search for (e.g., "total", "amount", "price")
     * @return the numeric value as integer
     */
    public int extractNumberByKeyword(String keyword) {
        try {
            // Pattern to find numbers near the keyword
            String pattern = String.format(
                    "(?:%s[^0-9]{0,20})([0-9,]+\\.?[0-9]*)|([0-9,]+\\.?[0-9]*)(?:[^0-9]{0,20}%s)",
                    keyword, keyword
            );

            Pattern regexPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regexPattern.matcher(rawContent);

            if (matcher.find()) {
                String result = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                result = result.replaceAll(",", "");
                int number = (int) Double.parseDouble(result);
                return number;
            }

            LogsManager.warn("No number found for keyword:", keyword);
            return 0;

        } catch (Exception e) {
            LogsManager.error("Error extracting number by keyword:", keyword, e.getMessage());
            return 0;
        }
    }

    /**
     * Extracts data using a custom regex pattern and returns as integer
     * @param pattern the regex pattern to use
     * @return the first match as integer, or 0 if not found
     */
    public int extractIntByPattern(String pattern) {
        return extractIntByPattern(rawContent, pattern, 1);
    }

    /**
     * Extracts data using a custom regex pattern from specific text and returns as integer
     * @param text the text to search in
     * @param pattern the regex pattern to use
     * @param groupIndex the regex group index to extract (default is 1)
     * @return the matched data as integer, or 0 if not found
     */
    public int extractIntByPattern(String text, String pattern, int groupIndex) {
        try {
            Pattern regexPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            Matcher matcher = regexPattern.matcher(text);

            if (matcher.find()) {
                String result = matcher.group(groupIndex).replaceAll(",", "");
                int value = (int) Double.parseDouble(result);
                return value;
            }

            LogsManager.warn("No match found for pattern:", pattern);
            return 0;

        } catch (Exception e) {
            LogsManager.error("Error extracting integer with pattern:", pattern, e.getMessage());
            return 0;
        }
    }

    /**
     * Gets data from the text file by key
     * @param key the key to look up
     * @return the value associated with the key, or empty string if not found
     */
    public String getTextData(String key) {
        try {
            String value = textData.getOrDefault(key, "");
            if (value.isEmpty()) {
                LogsManager.warn("Key not found in text file:", key);
            }
            return value;
        } catch (Exception e) {
            LogsManager.error("Error getting text data for key:", key, e.getMessage());
            return "";
        }
    }

    /**
     * Gets the raw content of the text file
     * @return the entire file content as a string
     */
    public String getRawContent() {
        return rawContent;
    }



    /**
     * Reloads the text file from disk
     */
    public void reload() {
        textData.clear();
        loadTextFile();
        LogsManager.info("Reloaded text file:", textFileName);
    }
}