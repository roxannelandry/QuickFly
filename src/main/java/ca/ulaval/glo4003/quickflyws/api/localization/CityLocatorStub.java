package ca.ulaval.glo4003.quickflyws.api.localization;

import java.util.LinkedList;
import java.util.Queue;

import ca.ulaval.glo4003.quickflyws.dto.LocalizationInfoDto;

public class CityLocatorStub implements CityLocator {

  private Queue<String> cities = new LinkedList<String>();

  public CityLocatorStub() {
    cities.add("QC");
    cities.add("Japon");
    cities.add("Iles");
    cities.add("Cuba");
  }

  @Override
  public LocalizationInfoDto cityLocator(String localizationInfo) {
    String currentCity = cities.remove();
    
    LocalizationInfoDto localisationInfoDto = new LocalizationInfoDto();
    localisationInfoDto.currentCity = currentCity;
    cities.add(currentCity);
    
    return localisationInfoDto;
  }

}