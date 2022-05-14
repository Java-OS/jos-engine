package ir.moke.jos;

import java.util.Scanner;

public class Shell {
    public static final Shell instance = new Shell();
    private static final String PS1 = "jos-shell:> ";

    private Shell() {
    }

    private void printPs1() {
        System.out.print(PS1);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        printPs1();
        String line = scanner.nextLine();
        while (line != null) {
            if (!line.isBlank()) {
                String[] parts = line.split(" ");
                String cmd = parts[0];
                String params = null;
                if (parts.length > 1) {
                    params = line.substring(line.indexOf(" ")).trim();
                }

                if (cmd.equals("echo")) {
                    Echo.execute(params);
                } else if (cmd.equals("exit")) {
                    break;
                } else {
                    System.out.println("Command not found");
                }

            }
            printPs1();
            line = scanner.nextLine();
        }
    }
}
