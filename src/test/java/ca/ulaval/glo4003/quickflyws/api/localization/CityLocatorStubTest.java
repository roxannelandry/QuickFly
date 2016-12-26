package ca.ulaval.glo4003.quickflyws.api.localization;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import ca.ulaval.glo4003.quickflyws.dto.LocalizationInfoDto;

public class CityLocatorStubTest {
  
  private static final String IP_ADDRESS = "192.190.1.0";

  private CityLocator cityLocator;
  
  @Before
  public void setUp() {
    cityLocator = new CityLocatorStub();
  }

  @Test
  public void autoGetLocalization_returnValidLocalization() {
    LocalizationInfoDto localizationInfoDto = cityLocator.cityLocator(IP_ADDRESS);
    
    assertNotNull(localizationInfoDto.currentCity);
  }
  
}