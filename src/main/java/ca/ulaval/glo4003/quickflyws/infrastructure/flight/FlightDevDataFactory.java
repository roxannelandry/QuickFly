package ca.ulaval.glo4003.quickflyws.infrastructure.flight;

import java.util.List;

import ca.ulaval.glo4003.quickflyws.domain.flight.cargoFlight.CargoFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.HeavyFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.LightFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.MediumFlight;
import ca.ulaval.glo4003.quickflyws.domain.flight.passengerFlight.PassengerFlight;
import jersey.repackaged.com.google.common.collect.Lists;

public class FlightDevDataFactory {

  private static final boolean IS_AIR_VIVANT = true;
  private static final boolean IS_NOT_AIR_VIVANT = false;

  public List<PassengerFlight> createPassengerFlightData() {
    final List<PassengerFlight> flights = Lists.newArrayList();

    HeavyFlight cuba = new HeavyFlight("QC", "Cuba", "2017-10-01 17:00", "AirGras", 3, 20.00, 5, 40.00, 2, 90.00, 10.00, 10.00, IS_AIR_VIVANT);
    flights.add(cuba);

    MediumFlight iles = new MediumFlight("QC", "Iles", "2017-09-25 09:00", "AirLousse", 2, 20.00, 10, 40.00, 2, 90.00, IS_NOT_AIR_VIVANT);
    flights.add(iles);

    LightFlight iles2 = new LightFlight("QC", "Iles", "2017-09-20 07:00", "AirLine", 0, 20.00, 6, 40.00, 0, 90.00, IS_AIR_VIVANT);
    flights.add(iles2);

    LightFlight iles3 = new LightFlight("QC", "Iles", "2017-01-01 15:00", "AirLousse", 0, 20.00, 5, 40.00, 0, 90.00, IS_NOT_AIR_VIVANT);
    flights.add(iles3);

    LightFlight iles4 = new LightFlight("Iles", "QC", "2018-01-15 16:00", "AirLousse", 0, 20.00, 5, 40.00, 0, 90.00, IS_AIR_VIVANT);
    flights.add(iles4);

    LightFlight iles5 = new LightFlight("Iles", "QC", "2017-01-15 19:00", "AirLine", 0, 20.00, 5, 40.00, 0, 90.00, IS_NOT_AIR_VIVANT);
    flights.add(iles5);

    MediumFlight japon = new MediumFlight("QC", "Japon", "2016-12-25 22:00", "AirLine", 3, 20.00, 5, 40.00, 2, 90.00, IS_AIR_VIVANT);
    flights.add(japon);

    LightFlight japon2 = new LightFlight("QC", "Japon", "2016-12-20 23:00", "AirLousse", 0, 20.00, 5, 40.00, 0, 90.00, IS_NOT_AIR_VIVANT);
    flights.add(japon2);

    HeavyFlight japon3 = new HeavyFlight("QC", "Japon", "2017-01-01 17:00", "AirGras", 3, 20.00, 5, 40.00, 2, 90.00, 50, 10.00, IS_AIR_VIVANT);
    flights.add(japon3);

    LightFlight japon4 = new LightFlight("Japon", "QC", "2016-12-27 06:00", "AirGras", 0, 20.00, 5, 40.00, 0, 90.00, IS_NOT_AIR_VIVANT);
    flights.add(japon4);

    HeavyFlight japon5 = new HeavyFlight("Japon", "QC", "2017-12-01 08:00", "AirMasse", 3, 20.00, 5, 40.00, 2, 90.00, 8.00, 10.00, IS_AIR_VIVANT);
    flights.add(japon5);

    HeavyFlight cuba2 = new HeavyFlight("QC", "Cuba", "2017-11-21 13:00", "AirMasse", 3, 20.00, 5, 40.00, 2, 90.00, 0, 10.00, IS_NOT_AIR_VIVANT);
    flights.add(cuba2);

    HeavyFlight japon6 = new HeavyFlight("Japon", "QC", "2017-12-03 11:00", "AirGras", 3, 20.00, 5, 40.00, 2, 90.00, 0, 10.00, IS_AIR_VIVANT);
    flights.add(japon6);

    return flights;
  }

  public List<CargoFlight> createCargoFlightData() {
    final List<CargoFlight> flights = Lists.newArrayList();

    CargoFlight cuba = new CargoFlight("QC", "Cuba", "2017-10-01 19:00", "AirGras", 10.00, 200.00);
    flights.add(cuba);

    CargoFlight iles = new CargoFlight("QC", "Iles", "2017-09-22 15:00", "AirGras", 10.00, 200.00);
    flights.add(iles);

    return flights;
  }

}