import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Lexic {
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		Scanner s = null;
		String str = "";
		char[] myChar = null;
		List<Character> spaces = new ArrayList<Character>();
        try {
        	s = new Scanner(new BufferedReader(new FileReader("Lexic.txt")));
        	while (s.hasNext()){
        		str += s.next() + " "; 
        		myChar = str.toCharArray();
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }
        for (int i = 0; i < myChar.length; i++) {
			TimeUnit.SECONDS.sleep(1);
        	if (myChar[i] == ' ') {
        		spaces.add(myChar[i]);
        	} else {
        		if (!spaces.isEmpty()) {
        			while(!spaces.isEmpty()) {
        				System.out.print(spaces.remove(spaces.size() - 1));
            		}
        		}
                System.out.print(myChar[i]);
        	}
        }
        System.out.println("");
        System.out.println("end");
	}
}
