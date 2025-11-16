import java.util.ArrayList;
import java.util.List;

/**
 * Minimal CSV parser that supports:
 *  - quoted fields
 *  - embedded commas
 *  - empty fields
 *  - trimming whitespace
 */
public class CSV {

    public static String[] parse(String line) {
        List<String> fields = new ArrayList<>();
        if (line == null || line.isEmpty()) {
            return new String[0];
        }

        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes; // toggle
            } else if (c == ',' && !inQuotes) {
                fields.add(sb.toString().trim());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }

        fields.add(sb.toString().trim());
        return fields.toArray(new String[0]);
    }
}
