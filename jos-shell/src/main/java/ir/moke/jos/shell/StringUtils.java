package ir.moke.jos.shell;

import ir.moke.jos.common.CliTransient;
import org.nocrala.tools.texttablefmt.Table;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public interface StringUtils {
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
}
