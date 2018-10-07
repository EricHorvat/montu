package ar.edu.itba.montu.abstraction;

import com.google.common.collect.Range;

public class Characteristic<T extends Comparable<T>>{
  /*Why an object for this? Possible future of power ups by having a list of them and calculating in getValue*/
//	private T value;
//  private T maxValue;
  private Range<T> valueRange;
  private T value;
//  private boolean fixed;

  private Characteristic(final Range<T> valueRange, final T value) {
  	this.valueRange = valueRange;
  	this.value = value;
  }
  
  public static <T extends Comparable<T>> Characteristic<T> withFixedValue(final T value) {
  	return new Characteristic<T>(Range.closed(value, value), value);
  }

  public static <T extends Comparable<T>> Characteristic<T> withChangingValue(final T minValue, final T maxValue, final T value) {
    return new Characteristic<>(Range.closed(minValue, maxValue), value);
  }

  public static <T extends Comparable<T>> Characteristic<T> withChangingValue(final T minValue, final T maxValue) {
    return Characteristic.withChangingValue(minValue, maxValue, maxValue);
  }

  public T value() {
  	return value;
  }
  
  public T maxValue() {
  	return valueRange.upperEndpoint();
  }
  
  public void updateValue(final T value) {
  	if (valueRange.contains(value)) {
  		this.value = value;
  	}
  }
}
