package ca.ulaval.glo4003.quickflyws.api.weightScale;

import java.util.LinkedList;
import java.util.Queue;

public class WeightScaleStub implements WeightScale {

  private Queue<String> weights = new LinkedList<String>();

  public WeightScaleStub() {
    weights.add("20.91");
    weights.add("40.23");
    weights.add("60.01");
    weights.add("99.99");
  }

  @Override
  public String autoDetectWeight() {
    String currentWeight = weights.remove();

    String weightString = "{\"luggageWeight\" : " + currentWeight + "}";
    weights.add(currentWeight);

    return weightString;
  }

}