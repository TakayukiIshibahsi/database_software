import java.util.regex.*;

public class Regex_sample {
    public static void main(String[] args) {
        String str = "C:\\Users\\Takayuki Ishibashi\\Documents\\hobby\\Go\\ch02\\hellomr\\hellomr.go";
        Pattern regex_sam= Pattern.compile(".*[\\\\](\\w+\\.\\w+)");
        Matcher match = regex_sam.matcher(str);
        if (match.matches()){
            System.out.println(match.group(1));
        }
    }
}
