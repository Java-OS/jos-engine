package ir.moke.jos.shell;

import ir.moke.jos.common.CliTransient;
import ir.moke.jsysbox.network.Ethernet;
import ir.moke.jsysbox.network.EthernetStatistic;
import ir.moke.jsysbox.network.Route;
import org.nocrala.tools.texttablefmt.Table;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface ConsoleUtils {
    static Table formatListToTextTable(Collection<?> objectList) {
        Object first = objectList.iterator().next();
        List<Method> methods = Arrays.stream(first.getClass().getDeclaredMethods())
                .filter(item -> item.getName().startsWith("get") || item.getName().startsWith("is"))
                .filter(item -> !item.isAnnotationPresent(CliTransient.class))
                .toList();
        Table table = new Table(methods.size());
        for (Method m : methods) {
            if (m.getName().startsWith("get")) {
                table.addCell(m.getName().substring(3));
            }

            if (m.getName().startsWith("is")) {
                table.addCell(m.getName().substring(2));
            }
        }

        for (Object o : objectList) {
            for (Method m : methods) {
                try {
                    Object value = m.invoke(o);
                    if (value instanceof String) {
                        table.addCell(String.valueOf(value));
                    } else if (value instanceof Boolean) {
                        table.addCell(Boolean.toString((Boolean) value));
                    } else if (value instanceof Number) {
                        table.addCell(String.valueOf(value));
                    } else if (value instanceof Path) {
                        table.addCell(String.valueOf(((Path) value).getFileName()));
                    } else if (value instanceof Collection<?>) {
                        table.addCell(value.toString());
                    } else {
                        table.addCell(" ");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return table;
    }

    static Table formatNetworkInterfaceTable(List<Ethernet> ethernetList) {
        Table table = new Table(11);
        table.addCell("Iface");
        table.addCell("Mac");
        table.addCell("IP Address");
        table.addCell("CIDR");
        table.addCell("Netmask");
        table.addCell("rx_pkts");
        table.addCell("rx_bytes");
        table.addCell("rx_err");
        table.addCell("tx_pkts");
        table.addCell("tx_bytes");
        table.addCell("tx_err");

        for (Ethernet ethernet : ethernetList) {
            table.addCell(ethernet.iface());
            table.addCell(ethernet.mac());
            table.addCell(ethernet.ip());
            table.addCell(ethernet.cidr() != null ? "/" + ethernet.cidr() : "");
            table.addCell(ethernet.netmask());

            EthernetStatistic statistic = ethernet.statistic();
            table.addCell(String.valueOf(statistic.rx_pkts()));
            table.addCell(String.valueOf(statistic.rx_bytes()));
            table.addCell(String.valueOf(statistic.rx_errors()));
            table.addCell(String.valueOf(statistic.tx_pkts()));
            table.addCell(String.valueOf(statistic.tx_bytes()));
            table.addCell(String.valueOf(statistic.tx_errors()));
        }
        return table;
    }

    static Table formatRouteTable(List<Route> routeList) {
        Table table = new Table(12);
        table.addCell("id");
        table.addCell("Destination");
        table.addCell("Netmask");
        table.addCell("Gateway");
        table.addCell("Interface");
        table.addCell("Flags");
        table.addCell("Use");
        table.addCell("Metrics");
        table.addCell("MTU");
        table.addCell("Window");
        table.addCell("IRTT");
        table.addCell("REFCNT");

        for (Route route : routeList) {
            table.addCell(String.valueOf(route.id()));
            table.addCell(route.destination());
            table.addCell(route.netmask());
            table.addCell(route.gateway());
            table.addCell(route.iface());
            table.addCell(route.getFlagStr());
            table.addCell(String.valueOf(route.use()));
            table.addCell(String.valueOf(route.metrics()));
            table.addCell(String.valueOf(route.mtu()));
            table.addCell(String.valueOf(route.window()));
            table.addCell(String.valueOf(route.irtt()));
            table.addCell(String.valueOf(route.refcnt()));
        }

        return table;
    }


}
