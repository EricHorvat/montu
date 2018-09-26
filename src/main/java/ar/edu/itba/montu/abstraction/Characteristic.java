package ar.edu.itba.montu.abstraction;

public class Characteristic<T extends Comparable>{
  /*Why an object for this? Possible future of power ups by having a list of them and calculating in getValue*/
  private T value;
  private T maxValue;
  private boolean fixed;

  public Characteristic(T value, boolean fixed) {
    this.value = value;
    this.maxValue = value;
    this.fixed = fixed;
  }

  public Characteristic(T value) {
    this(value,false);
  }


    public T value() {
    return value;
  }

  public void setValue(T value) {

    if (!fixed) {
      //TODO if (value.compareTo(maxValue)) value = maxValue;
      this.value = value;
    }
  }

  public T maxValue() {
    return maxValue;
  }
}
