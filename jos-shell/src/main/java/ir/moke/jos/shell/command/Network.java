package ir.moke.jos.shell.command;

import ir.moke.jos.shell.ConsoleUtils;
import ir.moke.jsysbox.JSysboxException;
import ir.moke.jsysbox.network.Ethernet;
import ir.moke.jsysbox.network.JNetwork;
import ir.moke.jsysbox.network.Route;
import org.nocrala.tools.texttablefmt.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Collections;
import java.util.List;

@CommandLine.Command(name = "net",
        version = "0.1",
        mixinStandardHelpOptions = true,
        subcommands = {Network.DevCMD.class, Network.RouteCMD.class},
        synopsisSubcommandLabel = "COMMAND",
        description = "Network Utils")
public class Network implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Network.class.getName());

    @CommandLine.Spec
    private CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        throw new CommandLine.ParameterException(spec.commandLine(), "Missing required subcommand");
    }

    /**
     * Dev Commands
     */
    @CommandLine.Command(name = "dev",
            mixinStandardHelpOptions = true,
            subcommands = {DevCMD.SetCMD.class, DevCMD.FlushCMD.class},
            description = "Configure a network interface")
    protected static class DevCMD implements Runnable {

        @CommandLine.Option(names = "-i", description = "Interface name")
        private String iface;

        @Override
        public void run() {
            if (iface != null && !iface.isEmpty()) {
                Ethernet ethernet = JNetwork.ethernet(iface);
                Table table = ConsoleUtils.formatNetworkInterfaceTable(Collections.singletonList(ethernet));
                logger.info(table.render());
            } else {
                List<Ethernet> ethernetList = JNetwork.ethernetList();
                Table table = ConsoleUtils.formatNetworkInterfaceTable(ethernetList);
                logger.info(table.render());
            }
        }

        @CommandLine.Command(name = "flush",
                mixinStandardHelpOptions = true,
                description = "Flush interface")
        protected static class FlushCMD implements Runnable {

            @CommandLine.Spec
            private CommandLine.Model.CommandSpec spec;

            @CommandLine.Parameters
            private String iface;

            @Override
            public void run() {
                if (iface == null || iface.isEmpty())
                    throw new CommandLine.ParameterException(spec.commandLine(), "Missing required parameters");

                Table table = null;
                try {
                    JNetwork.flush(iface);
                    Ethernet ethernet = JNetwork.ethernet(iface);
                    table = ConsoleUtils.formatNetworkInterfaceTable(Collections.singletonList(ethernet));
                } catch (JSysboxException e) {
                    logger.error(e.getMessage());
                } finally {
                    if (table != null) logger.info(table.render());
                }
            }
        }

        @CommandLine.Command(name = "set",
                mixinStandardHelpOptions = true,
                description = "Set interface ip address")
        protected static class SetCMD implements Runnable {
            @CommandLine.Spec
            private CommandLine.Model.CommandSpec spec;
            @CommandLine.Parameters(description = "interface name")
            private String[] parameters;

            @CommandLine.Option(names = "-c", description = "Clear interface ip and netmask")
            private boolean clear;

            @Override
            public void run() {
                if (parameters == null)
                    throw new CommandLine.ParameterException(spec.commandLine(), "Missing required parameters");

                if (parameters.length < 2) {
                    logger.error("Invalid parameters");
                    return;
                }
                String iface = parameters[0];
                if (!JNetwork.isEthernetExists(iface)) {
                    logger.error("Ethernet not exists");
                    return;
                }

                Table table = null;
                try {
                    if (parameters.length == 3) {
                        String ipAddress = parameters[1];
                        String netmask = parameters[2];
                        JNetwork.setIp(iface, ipAddress, netmask);
                        Ethernet ethernet = JNetwork.ethernet(iface);
                        table = ConsoleUtils.formatNetworkInterfaceTable(Collections.singletonList(ethernet));
                    } else if (parameters.length == 2 && parameters[1].equals("0.0.0.0")) {
                        JNetwork.flush(iface);
                        Ethernet ethernet = JNetwork.ethernet(iface);
                        table = ConsoleUtils.formatNetworkInterfaceTable(Collections.singletonList(ethernet));
                    } else if (parameters.length == 2 && parameters[1].contains("/")) {
                        String ipAddress = parameters[1].split("/")[0];
                        String cidrStr = parameters[1].split("/")[1];
                        int cidr = Integer.parseInt(cidrStr);
                        if (cidr > 32) logger.error("Invalid cidr");
                        String netmask = JNetwork.cidrToNetmask(cidr);
                        JNetwork.setIp(iface, ipAddress, netmask);
                        Ethernet ethernet = JNetwork.ethernet(iface);
                        table = ConsoleUtils.formatNetworkInterfaceTable(Collections.singletonList(ethernet));
                    } else {
                        logger.error("Invalid parameter");
                    }
                } catch (JSysboxException e) {
                    logger.error(e.getMessage());
                } finally {
                    if (table != null) logger.info(table.render());
                }
            }
        }
    }


    /**
     * Route Commands
     */
    @CommandLine.Command(name = "route",
            mixinStandardHelpOptions = true,
            subcommands = {RouteCMD.AddRouteCMD.class, RouteCMD.DeleteRouteCMD.class, RouteCMD.DefaultGatewayCMD.class},
            description = "Show/Manipulate the IP routing table")
    protected static class RouteCMD implements Runnable {

        @Override
        public void run() {
            printRouteTable();
        }

        protected static void printRouteTable() {
            List<Route> route = JNetwork.route();
            Table table = ConsoleUtils.formatRouteTable(route);
            logger.info(table.render());
        }

        @CommandLine.Command(name = "add",
                mixinStandardHelpOptions = true,
                description = "Add a new route")
        protected static class AddRouteCMD implements Runnable {

            @CommandLine.Spec
            private CommandLine.Model.CommandSpec spec;

            @CommandLine.Option(names = {"-m", "--metrics"}, description = "route metrics", defaultValue = "600")
            private int metrics;

            @CommandLine.Option(names = {"--host"}, description = "route host destination", defaultValue = "")
            private String host;

            @CommandLine.Option(names = {"--network"}, description = "route network destination", defaultValue = "")
            private String network;

            @CommandLine.Option(names = {"-g", "--gateway"}, description = "route packets via a gateway", defaultValue = "")
            private String gateway;

            @CommandLine.Option(names = {"-n", "--netmask"}, description = "when adding a network route, the netmask to be used.", defaultValue = "")
            private String netmask;

            @CommandLine.Option(names = {"-i", "--interface"}, description = "network interface", defaultValue = "")
            private String iface;

            @Override
            public void run() {
                if (!host.isEmpty() && !network.isEmpty())
                    throw new CommandLine.ParameterException(spec.commandLine(), "Invalid options");
                if (!network.isEmpty() && netmask.isEmpty())
                    throw new CommandLine.ParameterException(spec.commandLine(), "Invalid options");
                if (!host.isEmpty() && !netmask.isEmpty())
                    throw new CommandLine.ParameterException(spec.commandLine(), "Invalid options");

                if (!host.isEmpty()) {
                    JNetwork.addHostToRoute(host, gateway, iface, metrics);
                }

                if (!network.isEmpty()) {
                    JNetwork.addNetworkToRoute(network, netmask, gateway, iface, metrics);
                }

                printRouteTable();
            }
        }


        @CommandLine.Command(name = "del",
                mixinStandardHelpOptions = true,
                description = "delete a route")
        protected static class DeleteRouteCMD implements Runnable {

            @CommandLine.Spec
            private CommandLine.Model.CommandSpec spec;

            @CommandLine.Parameters(description = "id of route table")
            private int id;

            @Override
            public void run() {
                JNetwork.deleteRoute(id);
                printRouteTable();
            }
        }

        @CommandLine.Command(name = "default",
                mixinStandardHelpOptions = true,
                description = "Set default gateway")
        protected static class DefaultGatewayCMD implements Runnable {

            @CommandLine.Spec
            private CommandLine.Model.CommandSpec spec;

            @CommandLine.Parameters(description = "Default gateway ip address")
            private String defaultGw;

            @Override
            public void run() {
                JNetwork.setDefaultGateway(defaultGw);
                printRouteTable();
            }
        }
    }
}

