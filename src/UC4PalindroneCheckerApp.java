import java.util.Scanner;

public class UC4PalindroneCheckerApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String input = sc.nextLine();
        char[] characters = input.toCharArray();
        int start = 0;
        int end = characters.length - 1;
        boolean isPalindrome = true;
        while (start < end) {
            if (characters[start] != characters[end]) {
                isPalindrome = false;
                break;
            }
            start++;
            end--;
        }
        if (isPalindrome) {
            System.out.println("The given string is a Palindrome.");
        } else {
            System.out.println("The given    string is NOT a Palindrome.");
        }
        sc.close();
    }
}