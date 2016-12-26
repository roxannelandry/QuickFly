package ca.ulaval.glo4003.quickflyws.infrastructure.paymentSystem.transactionLogger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ca.ulaval.glo4003.quickflyws.domain.paymentSystem.TransactionDetails;
import ca.ulaval.glo4003.quickflyws.service.paymentSystem.TransactionLogger;

public class TransactionLoggerInFile implements TransactionLogger {
  
  private static final SimpleFormatter formatter = new SimpleFormatter();

  @Override
  public void log(TransactionDetails transactionDetails) {
    Logger logger = Logger.getLogger(TransactionLoggerInFile.class.getName());
    FileHandler fileHandler = null;

    try {
      fileHandler = new FileHandler("./transactionLog.log", true);
      logger.addHandler(fileHandler);
      fileHandler.setFormatter(formatter);

      logger.info(transactionDetails.transactionDetailsToString() + System.lineSeparator());
      fileHandler.close();

    } catch (SecurityException e) {
      logger.removeHandler(fileHandler);
      logger.info("A problem happened on the server. Please call to get your transaction details");

    } catch (IOException e) {
      logger.removeHandler(fileHandler);
      logger.info("A problem happened on the server. Please call to get your transaction details");
    }
  }

}