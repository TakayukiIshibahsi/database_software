import java.util.Scanner;
import java.util.regex.*;

public class Regex_sample {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str =sc.nextLine();
        
        Pattern file_name_regex_windows=Pattern.compile("\\\".*[\\\\]([^\\\\]+\\.\\w+)\\\"");
        
        Matcher match2 = null;
        if(file_name_regex_windows.matcher(str).find()) {
            match2 = file_name_regex_windows.matcher(str);
        }
        if (match2.matches()){
            System.out.println(match2.group(1)+" second");
            System.out.println(match2.matches());
        }
        sc.close();
    }
}
