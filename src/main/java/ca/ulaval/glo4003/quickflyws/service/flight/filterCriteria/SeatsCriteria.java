package ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;

public class SeatsCriteria extends SearchResultFilterCriteria {

  private boolean wantEconomic;
  private boolean wantRegular;
  private boolean wantBusiness;

  public SeatsCriteria() {
  }

  @Override
  public boolean meetCriteria(PassengerFlight passengerFlight) {
    if (!passengerFlight.hasAvailableSeats(wantEconomic, wantRegular, wantBusiness)) {
      return false;
    } else {
      return successor.meetCriteria(passengerFlight);
    }
  }

  public void setParameters(boolean wantEconomic, boolean wantRegular, boolean wantBusiness) {
    this.wantEconomic = wantEconomic;
    this.wantRegular = wantRegular;
    this.wantBusiness = wantBusiness;
  }

}