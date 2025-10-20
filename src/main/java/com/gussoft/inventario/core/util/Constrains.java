package com.gussoft.inventario.core.util;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Constrains {

  public static final String CRITERIA_LIKE_PERCENT = "%";
  public static final String FILTER_SEPARATOR = "&";
  public static final String FILTER_EQUAL = "=";
  public static final String FILTER_VALUE_SEPARATOR = ",";
  public static final String FILTER_VALUE_GUION = "-";

  public static final String FILTER_NAME = "nombre";
  public static final String FILTER_LASTNAME = "apellido";
  public static final String FILTER_DNI = "dni";
  public static final String FILTER_EMAIL = "email";
  public static final String FILTER_PHONE = "telefono";
  public static final String FILTER_DATE = "fechaRegistro";
  public static final String FILTER_ADDRESS = "direccion";
  public static final String FILTER_STATUS = "estado";
  public static final String FILTER_DESCRIPTION = "descripcion";
  public static final String FILTER_STOCK = "stock";
  public static final String FILTER_PRICE = "precio";
  public static final String FILTER_COUNT = "cantidad";

  public static final ZoneId LIMA_ZONE = ZoneId.of("America/Lima");
  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
}
