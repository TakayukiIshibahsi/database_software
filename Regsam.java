import java.util.regex.*;
import java.time.*;
public class Regsam {
    public static void main(String[] args){
        String regex="(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(\\d+).*";
        System.out.println(String.valueOf(LocalDateTime.now()));
        Pattern timep = Pattern.compile(regex);
        String time=String.valueOf(LocalDateTime.now());
        Matcher b = timep.matcher(time);
        b.matches();
            System.out.println(b.group(6));

        
    }
    
}
