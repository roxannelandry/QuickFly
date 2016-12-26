package ca.ulaval.glo4003.quickflyws.service.flight.filterCriteria;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;

public abstract class SearchResultFilterCriteria {
  
  protected SearchResultFilterCriteria successor;
  
  public SearchResultFilterCriteria() {
  }
    
  public void setSuccessor(SearchResultFilterCriteria successor) {
    this.successor = successor;
  }
  
  public abstract boolean meetCriteria(PassengerFlight passengerFlight);
  
}