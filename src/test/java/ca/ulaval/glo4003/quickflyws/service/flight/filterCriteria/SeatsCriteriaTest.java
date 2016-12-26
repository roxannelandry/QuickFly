package ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria;

import static org.junit.Assert.assertFalse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;

@RunWith(MockitoJUnitRunner.class)
public class SeatsCriteriaTest {

  private static final boolean IS_ECONOMIC = true;
  private static final boolean IS_NOT_ECONOMIC = false;
  private static final boolean IS_REGULAR = true;
  private static final boolean IS_NOT_REGULAR = false;
  private static final boolean IS_BUSINESS = true;
  private static final boolean IS_NOT_BUSINESS = false;

  @Mock
  private PassengerFlight passengerFlight;

  @Mock
  private LivingLuggageCriteria livingLuggageCriteria;

  private SeatsCriteria seatsCriteria;

  @Before
  public void setUp() {
    seatsCriteria = new SeatsCriteria();
    seatsCriteria.setSuccessor(livingLuggageCriteria);
  }

  @Test
  public void requestForPassengerFlightWithEconomicAndRegularAndBusinessSeat_flightMeetCriteriaforSeats_callNextFilter() {
    given(passengerFlight.hasAvailableSeats(IS_ECONOMIC, IS_REGULAR, IS_BUSINESS)).willReturn(true);
    seatsCriteria.setParameters(IS_ECONOMIC, IS_REGULAR, IS_BUSINESS);

    seatsCriteria.meetCriteria(passengerFlight);

    verify(livingLuggageCriteria).meetCriteria(passengerFlight);
  }

  @Test
  public void requestForPassengerFlightWithEconomicAndRegularAndBusinessSeat_flightMeetCriteriaforSeatsWhenNoSeatsAvailable_FlightMustBeFiltered() {
    given(passengerFlight.hasAvailableSeats(IS_ECONOMIC, IS_REGULAR, IS_BUSINESS)).willReturn(false);
    seatsCriteria.setParameters(IS_ECONOMIC, IS_REGULAR, IS_BUSINESS);

    assertFalse(seatsCriteria.meetCriteria(passengerFlight));
  }

  @Test
  public void requestForPassengerFlightWithRegularAndBusinessSeat_flightMeetCriteriaforSeats_callNextFilter() {
    given(passengerFlight.hasAvailableSeats(IS_NOT_ECONOMIC, IS_REGULAR, IS_BUSINESS)).willReturn(true);
    seatsCriteria.setParameters(IS_NOT_ECONOMIC, IS_REGULAR, IS_BUSINESS);

    seatsCriteria.meetCriteria(passengerFlight);

    verify(livingLuggageCriteria).meetCriteria(passengerFlight);
  }

  @Test
  public void requestForPassengerFlightWithRegularAndBusinessSeat_flightMeetCriteriaforSeatsWhenNoSeatsAvailable_FlightMustBeFiltered() {
    given(passengerFlight.hasAvailableSeats(IS_NOT_ECONOMIC, IS_REGULAR, IS_BUSINESS)).willReturn(false);
    seatsCriteria.setParameters(IS_NOT_ECONOMIC, IS_REGULAR, IS_BUSINESS);

    assertFalse(seatsCriteria.meetCriteria(passengerFlight));
  }

  @Test
  public void requestForPassengerFlightWithEconomicAndBusinessSeat_flightMeetCriteriaforSeats_callNextFilter() {
    given(passengerFlight.hasAvailableSeats(IS_ECONOMIC, IS_NOT_REGULAR, IS_BUSINESS)).willReturn(true);
    seatsCriteria.setParameters(IS_ECONOMIC, IS_NOT_REGULAR, IS_BUSINESS);

    seatsCriteria.meetCriteria(passengerFlight);

    verify(livingLuggageCriteria).meetCriteria(passengerFlight);
  }

  @Test
  public void requestForPassengerFlightWithEconomicAndBusinessSeat_flightMeetCriteriaforSeatsWhenNoSeatsAvailable_FlightMustBeFiltered() {
    given(passengerFlight.hasAvailableSeats(IS_ECONOMIC, IS_NOT_REGULAR, IS_BUSINESS)).willReturn(false);
    seatsCriteria.setParameters(IS_ECONOMIC, IS_NOT_REGULAR, IS_BUSINESS);

    assertFalse(seatsCriteria.meetCriteria(passengerFlight));
  }

  @Test
  public void requestForPassengerFlightWithEconomicAndRegularSeat_flightMeetCriteriaforSeats_callNextFilter() {
    given(passengerFlight.hasAvailableSeats(IS_ECONOMIC, IS_REGULAR, IS_NOT_BUSINESS)).willReturn(true);
    seatsCriteria.setParameters(IS_ECONOMIC, IS_REGULAR, IS_NOT_BUSINESS);

    seatsCriteria.meetCriteria(passengerFlight);

    verify(livingLuggageCriteria).meetCriteria(passengerFlight);
  }

  @Test
  public void requestForPassengerFlightWithEconomicAndRegularSeat_flightMeetCriteriaforSeatsWhenNoSeatsAvailable_FlightMustBeFiltered() {
    given(passengerFlight.hasAvailableSeats(IS_ECONOMIC, IS_REGULAR, IS_NOT_BUSINESS)).willReturn(false);
    seatsCriteria.setParameters(IS_ECONOMIC, IS_REGULAR, IS_NOT_BUSINESS);

    assertFalse(seatsCriteria.meetCriteria(passengerFlight));
  }

  @Test
  public void requestForPassengerFlightWithNoSpecifiSeats_flightMeetCriteriaforSeatsWhenNoSeatsAvailable_FlightMustBeFiltered() {
    given(passengerFlight.hasAvailableSeats(IS_NOT_ECONOMIC, IS_NOT_REGULAR, IS_NOT_BUSINESS)).willReturn(false);
    seatsCriteria.setParameters(IS_NOT_ECONOMIC, IS_NOT_REGULAR, IS_NOT_BUSINESS);

    assertFalse(seatsCriteria.meetCriteria(passengerFlight));
  }

}