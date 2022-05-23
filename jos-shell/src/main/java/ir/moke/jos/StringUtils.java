package ir.moke.jos;

import org.nocrala.tools.texttablefmt.Table;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.List;

public interface StringUtils {
    static Table formatListToTextTable(List<?> objectList) {
        Field[] fields = objectList.get(0).getClass().getDeclaredFields();
        Table table = new Table(fields.length);
        for (Field field : fields) {
            table.addCell(field.getName());
        }

        for (Object o : objectList) {
            for (Field field : fields) {
                try {
                    Class<?> aClass = o.getClass();
                    Method declaredMethod = aClass.getDeclaredMethod(field.getName());
                    Object value = declaredMethod.invoke(o);
                    if (value instanceof String) {
                        table.addCell(String.valueOf(value));
                    } else if (value instanceof Boolean) {
                        table.addCell(Boolean.toString((Boolean) value));
                    } else if (value instanceof Number) {
                        table.addCell(String.valueOf(value));
                    } else if (value instanceof Path) {
                        table.addCell(String.valueOf(((Path) value).getFileName()));
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
