package ir.moke.jos.shell.command;


import ir.moke.jsysbox.network.JNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "ping",
        mixinStandardHelpOptions = true,
        subcommands = {CommandLine.HelpCommand.class},
        description = "Send ICMP ECHO_REQUEST to network hosts")
public class Ping implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Ping.class.getName());
    @CommandLine.Parameters(description = "destination")
    private String destination;

    @CommandLine.Option(names = {"-I", "--interface"}, description = "Specify interface address")
    private String iface;

    @CommandLine.Option(names = {"-i", "--interval"}, description = "Wait interval seconds between sending each packet")
    private int interval;

    @CommandLine.Option(names = {"-c", "--count"}, description = "Count ECHO_REQUEST packets to send")
    private int count;

    @CommandLine.Option(names = {"-t"}, description = "Set the IP Time to Live")
    private int ttl;

    @CommandLine.Option(names = {"-W"}, description = "Time to wait for response")
    private int timeout;


    @Override
    public void run() {
        JNetwork.ping(destination, iface, ttl, count, timeout, interval);
    }
}
