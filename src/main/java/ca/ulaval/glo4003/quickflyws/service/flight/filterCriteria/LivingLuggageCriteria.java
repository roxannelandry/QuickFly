package ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;

public class LivingLuggageCriteria extends SearchResultFilterCriteria {

  private boolean wantAirVivant;

  public LivingLuggageCriteria() {
  }

  @Override
  public boolean meetCriteria(PassengerFlight passengerFlight) {
    if (wantAirVivant) {
      return passengerFlight.isAirVivant();
    } else {
      return true;
    }
  }

  public void setParameters(boolean wantAirVivant) {
    this.wantAirVivant = wantAirVivant;
  }

}