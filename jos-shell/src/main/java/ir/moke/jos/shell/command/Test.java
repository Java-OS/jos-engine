package ir.moke.jos.shell.command;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@CommandLine.Command(name = "infocmp",
        mixinStandardHelpOptions = true,
        subcommands = {CommandLine.HelpCommand.class},
        description = "call linux command /usr/bin/infocmp")
public class Test implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Test.class.getName());

    @Override
    public void run() {
        try {



            String s;
            Process p = Runtime.getRuntime().exec("/usr/bin/infocmp");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = br.readLine()) != null)
                System.out.println(s);
            p.waitFor();
            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception ignored) {
        }
    }
}
