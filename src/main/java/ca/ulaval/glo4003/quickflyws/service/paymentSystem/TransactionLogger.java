package ca.ulaval.glo4003.quickflyws.service.paymentSystem;

import ca.ulaval.glo4003.quickflyws.domain.paymentSystem.TransactionDetails;

public interface TransactionLogger {

  void log(TransactionDetails transactionDetails);

}