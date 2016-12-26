package ca.ulaval.glo4003.quickflyws.api.weightScale;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class WeightScaleStubTest {

  private WeightScale weightScale;

  @Before
  public void setUp() {
    weightScale = new WeightScaleStub();
  }

  @Test
  public void autoGetWeight_returnValidWeight() {
    String weigthReturned = weightScale.autoDetectWeight();

    assertNotNull(weigthReturned);
  }

}