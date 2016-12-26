package ca.ulaval.glo4003.quickflyws.domain.flight.dateConverterChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

import org.junit.Before;
import org.junit.Test;

import ca.ulaval.glo4003.quickflyws.domain.dateConverterChecker.DateConverterChecker;

public class DateConverterCheckerTest {

  private static final String DATE_WRONG_FORMAT = "2016/08/18";
  private static final String DATE_GOOD_FORMAT = "2016-08-18 00:00";
  private static final String DATE_NO_HOUR_FORMAT = "2016-08-18";

  private static final LocalDateTime FIRST_DATE = LocalDateTime.of(2016, 8, 18, 00, 00);
  private static final LocalDateTime DATE_ONE_YEAR_LATER = LocalDateTime.of(2017, 8, 18, 00, 00);
  private static final LocalDateTime DATE_ONE_MONTH_LATER = LocalDateTime.of(2016, 9, 18, 00, 00);
  private static final LocalDateTime DATE_ONE_DAY_LATER = LocalDateTime.of(2016, 8, 19, 00, 00);

  private static final LocalDateTime DATE_FOUR_DAYS_LATER = LocalDateTime.of(2016, 8, 22, 00, 00);
  private static final LocalDateTime DATE_FOUR_DAYS_EARLER = LocalDateTime.of(2016, 8, 14, 00, 00);

  private DateConverterChecker dateChecker;

  @Before
  public void setUp() {
    dateChecker = new DateConverterChecker();
  }

  @Test(expected = DateTimeParseException.class)
  public void dateStringWithBadFormat_convertingToDateObject_throwDateTimeParse() {
    dateChecker.dateStringToDateTimeObject(DATE_WRONG_FORMAT);
  }

  @Test
  public void dateInStringWithGoodFormat_convertingToDateObject_yearIsSameAsString() {
    int year = extractYearFromDateString(DATE_GOOD_FORMAT);

    LocalDateTime dateObject = dateChecker.dateStringToDateTimeObject(DATE_GOOD_FORMAT);

    assertEquals(year, dateObject.getYear());
  }

  @Test
  public void dateInStringWithGoodFormat_convertingToDateObject_monthIsSameAsString() {
    int month = extractMonthFromDateString(DATE_GOOD_FORMAT);

    LocalDateTime dateObject = dateChecker.dateStringToDateTimeObject(DATE_GOOD_FORMAT);

    assertEquals(month, dateObject.getMonthValue());
  }

  @Test
  public void dateInStringWithGoodFormat_convertingToDateObject_dayIsSameAsString() {
    int day = extractDayFromDateString(DATE_GOOD_FORMAT);

    LocalDateTime dateObject = dateChecker.dateStringToDateTimeObject(DATE_GOOD_FORMAT);

    assertEquals(day, dateObject.getDayOfMonth());
  }

  @Test
  public void firstDateBeforeSecondDate_checkingIfFirstDateIsEarlierByDay_returnTrue() {
    assertTrue(dateChecker.firstDateIsEqualOrEarlier(FIRST_DATE, DATE_ONE_DAY_LATER));
  }

  @Test
  public void firstDateBeforeSecondDate_checkingIfFirstDateIsEarlierByMonth_returnTrue() {
    assertTrue(dateChecker.firstDateIsEqualOrEarlier(FIRST_DATE, DATE_ONE_MONTH_LATER));
  }

  @Test
  public void firstDateBeforeSecondDate_checkingIfFirstDateIsEarlierByYear_returnTrue() {
    assertTrue(dateChecker.firstDateIsEqualOrEarlier(FIRST_DATE, DATE_ONE_YEAR_LATER));
  }

  @Test
  public void dateOneDaysLaterThanFirstDate_checkingDateIsWithinThreeDays_returnTrue() {
    assertTrue(dateChecker.dateIsWithinThreeDays(DATE_ONE_DAY_LATER, FIRST_DATE));
  }

  @Test
  public void dateFourDaysLaterThanFirstDate_checkingDateIsWithinThreeDays_returnFalse() {
    assertFalse(dateChecker.dateIsWithinThreeDays(DATE_FOUR_DAYS_LATER, FIRST_DATE));
  }

  @Test
  public void dateFourDaysEarlierThanFirstDate_checkingDateIsWithinThreeDays_returnFalse() {
    assertFalse(dateChecker.dateIsWithinThreeDays(DATE_FOUR_DAYS_EARLER, FIRST_DATE));
  }

  @Test
  public void todayDate_addingHoursAndConvertToDateTimeObject_returnDateWithCurrentHour() {
    LocalDateTime dateInGoodFormat = dateChecker.dateAddHoursAndConvertToDateTimeObject(LocalDate.now().toString());

    assertEquals(dateInGoodFormat, LocalDateTime.now().truncatedTo(ChronoUnit.HOURS));
  }

  @Test
  public void dateNotToday_addingHoursAndConvertToDateTimeObject_returnDateWithCurrentHour() {
    LocalDateTime dateInGoodFormat = dateChecker.dateAddHoursAndConvertToDateTimeObject(DATE_NO_HOUR_FORMAT);

    assertEquals(dateInGoodFormat, FIRST_DATE);
  }

  private static int extractYearFromDateString(String date) {
    String year = date.substring(0, 4);
    return convertStringToInt(year);
  }

  private static int extractMonthFromDateString(String date) {
    String month = date.substring(5, 7);
    return convertStringToInt(month);
  }

  private static int extractDayFromDateString(String date) {
    String day = date.substring(8, 10);
    return convertStringToInt(day);
  }

  private static int convertStringToInt(String value) {
    return Integer.valueOf(value);
  }

}