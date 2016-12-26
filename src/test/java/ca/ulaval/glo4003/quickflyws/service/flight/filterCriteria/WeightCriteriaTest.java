package ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria;

import static org.junit.Assert.assertFalse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.weightCategory.WeightCategory;

@RunWith(MockitoJUnitRunner.class)
public class WeightCriteriaTest {

  private static final boolean AIR_CARGO_ALLOWED = true;
  private static final boolean AIR_CARGO_NOT_ALLOWED = false;
  
  private static final double LUGGAGE_WEIGHT = 80.00;
  
  private static final WeightCategory FLIGHT_CATEGORY_LIGHT = WeightCategory.AIR_LEGER;
  private static final WeightCategory FLIGHT_CATEGORY_HEAVY = WeightCategory.AIR_LOURD;

  @Mock
  private PassengerFlight passengerFlight;

  @Mock
  private CargoFlight cargoFlight;

  @Mock
  private SeatsCriteria seatsCriteria;

  private List<CargoFlight> cargoFlights;

  private WeightCriteria weightCriteria;

  @Before
  public void setUp() {
    weightCriteria = new WeightCriteria();
    weightCriteria.setSuccessor(seatsCriteria);
    cargoFlights = new ArrayList<>();
  }

  public void requestForPassengerFlightAsLightFlightWithExceedingLuggageAndAirCargoAllowedWithNoSpace_flightMeetCriteriaforWeight_flightMustBeFiltered() {
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(true);
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_LIGHT);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(false);
    cargoFlights.add(cargoFlight);

    weightCriteria.setParameters(LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED, cargoFlights);

    assertFalse(weightCriteria.meetCriteria(passengerFlight));
  }

  @Test
  public void requestForPassengerFlightAsLightFlightWithExceedingLuggageAndAirCargoAllowedWithEnoughSpace_flightMeetCriteriaforWeight_callNextFilter() {
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(true);
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_LIGHT);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(true);
    cargoFlights.add(cargoFlight);

    weightCriteria.setParameters(LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED, cargoFlights);
    weightCriteria.meetCriteria(passengerFlight);

    verify(seatsCriteria).meetCriteria(passengerFlight);
  }

  @Test
  public void requestForPassengerFlightAsHeavyFlightWithExceedingLuggageAndAirCargoAllowedWithNoSpace_flightMeetCriteriaforWeight_callNextFilter() {
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(true);
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_HEAVY);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(false);
    cargoFlights.add(cargoFlight);

    weightCriteria.setParameters(LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED, cargoFlights);
    weightCriteria.meetCriteria(passengerFlight);

    verify(seatsCriteria).meetCriteria(passengerFlight);
  }

  @Test
  public void requestForPassengerFlightAsLightFlightWithValidLuggageAndAirCargoAllowedWithNoSpace_flightMeetCriteriaforWeight_callNextFilter() {
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(false);
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_LIGHT);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(false);
    cargoFlights.add(cargoFlight);

    weightCriteria.setParameters(LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED, cargoFlights);
    weightCriteria.meetCriteria(passengerFlight);

    verify(seatsCriteria).meetCriteria(passengerFlight);
  }

  @Test
  public void requestForPassengerFlightAsHeavyFlightWithValidLuggageAndAirCargoAllowedWithEnoughSpace_flightMeetCriteriaforWeight_callNextFilter() {
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(false);
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_HEAVY);
    given(cargoFlight.hasEnoughSpace(LUGGAGE_WEIGHT)).willReturn(true);
    cargoFlights.add(cargoFlight);

    weightCriteria.setParameters(LUGGAGE_WEIGHT, AIR_CARGO_ALLOWED, cargoFlights);
    weightCriteria.meetCriteria(passengerFlight);

    verify(seatsCriteria).meetCriteria(passengerFlight);
  }

  @Test
  public void requestForPassengerFlightAsLightFlightWithValidLuggageAndNoAirCargoAllowed_flightMeetCriteriaforWeight_callNextFilter() {
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(false);
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_LIGHT);

    weightCriteria.setParameters(LUGGAGE_WEIGHT, AIR_CARGO_NOT_ALLOWED, cargoFlights);
    weightCriteria.meetCriteria(passengerFlight);

    verify(seatsCriteria).meetCriteria(passengerFlight);
  }

  @Test
  public void requestForPassengerFlightAsLightFlightWithExceedingLuggageAndNoAirCargoAllowed_flightMeetCriteriaforWeight_flightMustBeFiltered() {
    given(passengerFlight.weightIsExceeding(LUGGAGE_WEIGHT)).willReturn(true);
    given(passengerFlight.getFlightCategory()).willReturn(FLIGHT_CATEGORY_LIGHT);

    weightCriteria.setParameters(LUGGAGE_WEIGHT, AIR_CARGO_NOT_ALLOWED, cargoFlights);
    weightCriteria.meetCriteria(passengerFlight);

    assertFalse(weightCriteria.meetCriteria(passengerFlight));
  }

}