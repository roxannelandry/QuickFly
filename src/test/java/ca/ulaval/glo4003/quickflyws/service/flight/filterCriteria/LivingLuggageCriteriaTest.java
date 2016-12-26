package ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;

@RunWith(MockitoJUnitRunner.class)
public class LivingLuggageCriteriaTest {

  private static final boolean AIR_VIVANT = true;
  private static final boolean NO_AIR_VIVANT = false;

  @Mock
  private PassengerFlight passengerFlight;

  private LivingLuggageCriteria livingLuggageCriteria;

  @Before
  public void setUp() {
    livingLuggageCriteria = new LivingLuggageCriteria();
  }

  @Test
  public void passengerFlightWithLivingLuggageAllowed_flightMeetCriteriaforLivingLuggage_flightMustNotBeFiltered() {
    given(passengerFlight.isAirVivant()).willReturn(AIR_VIVANT);
    livingLuggageCriteria.setParameters(AIR_VIVANT);

    assertTrue(livingLuggageCriteria.meetCriteria(passengerFlight));
  }

  @Test
  public void passengerFlightWithNoLivingLuggageAllowed_flightMeetCriteriaforLivingLuggage_FlightMustBeFiltered() {
    given(passengerFlight.isAirVivant()).willReturn(NO_AIR_VIVANT);
    livingLuggageCriteria.setParameters(AIR_VIVANT);

    assertFalse(livingLuggageCriteria.meetCriteria(passengerFlight));
  }

  @Test
  public void passengerFlightWithLivingLuggageAllowedAndNoLivingOnlyCriteria_flightMeetCriteriaforLivingLuggage_flightMustNotBeFiltered() {
    given(passengerFlight.isAirVivant()).willReturn(AIR_VIVANT);
    livingLuggageCriteria.setParameters(NO_AIR_VIVANT);

    assertTrue(livingLuggageCriteria.meetCriteria(passengerFlight));
  }

  @Test
  public void passengerFlightWithNoLivingLuggageAllowedAndNoLivingOnlyCriteria_flightMeetCriteriaforLivingLuggage_flightMustNotBeFiltered() {
    given(passengerFlight.isAirVivant()).willReturn(NO_AIR_VIVANT);
    livingLuggageCriteria.setParameters(NO_AIR_VIVANT);

    assertTrue(livingLuggageCriteria.meetCriteria(passengerFlight));
  }
  
}