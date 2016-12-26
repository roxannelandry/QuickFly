package ca.ulaval.glo4003.quickflyws.infrastructure.flight.flightModificationLogger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import ca.ulaval.glo4003.quickflyws.service.flight.FlightModificationLogger;

public class FlightModificationLoggerInFile implements FlightModificationLogger {

  @Override
  public void log(PassengerFlight passengerFlight) {

    Logger logger = Logger.getLogger(FlightModificationLoggerInFile.class.getName());
    FileHandler fileHandler = null;

    try {
      fileHandler = new FileHandler("./flightModificationLog.log", true);
      logger.addHandler(fileHandler);
      SimpleFormatter formatter = new SimpleFormatter();
      fileHandler.setFormatter(formatter);

      logger.info("Modifications saved for the following flight : " + System.lineSeparator() + System.lineSeparator() + passengerFlight.flightToString());
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