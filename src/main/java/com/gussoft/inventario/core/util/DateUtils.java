package com.gussoft.inventario.core.util;

import static com.gussoft.inventario.core.util.Constrains.DATE_TIME_FORMATTER;
import static com.gussoft.inventario.core.util.Constrains.LIMA_ZONE;

import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtils {

  public static LocalDateTime fechaLima() {
    return LocalDateTime.now(LIMA_ZONE);
  }

  public static String dateTimeFormater(LocalDateTime dateTime) {
    return dateTime.format(DATE_TIME_FORMATTER);
  }

  public static LocalDateTime parseStringToDate(String dateStr) {
    return LocalDateTime.parse(dateStr, DATE_TIME_FORMATTER);
  }
}
