package ca.ulaval.glo4003.quickflyws.domain.dateConverterChecker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class DateConverterChecker {

  private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public LocalDateTime dateStringToDateTimeObject(String dateString) throws DateTimeParseException {
    return LocalDateTime.parse(dateString, formatterDateTime);
  }

  public boolean firstDateIsEqualOrEarlier(LocalDateTime firstDate, LocalDateTime secondDate) {
    return firstDate.compareTo(secondDate) <= 0;
  }

  public boolean dateIsWithinThreeDays(LocalDateTime cargoFlightDate, LocalDateTime passengerFlightDate) {
    return cargoFlightDate.compareTo(passengerFlightDate) <= 3 && cargoFlightDate.compareTo(passengerFlightDate) >= 0;
  }

  public LocalDateTime dateAddHoursAndConvertToDateTimeObject(String date) {
    LocalDateTime dateTime = null;
    LocalDate dateOnly = LocalDate.parse(date, formatterDate);

    if (dateOnly.equals(LocalDate.now())) {
      dateTime = LocalDateTime.of(dateOnly, LocalTime.now().truncatedTo(ChronoUnit.HOURS));
    } else {
      dateTime = LocalDateTime.of(dateOnly, LocalTime.of(0, 0));
    }

    return dateTime;
  }

}