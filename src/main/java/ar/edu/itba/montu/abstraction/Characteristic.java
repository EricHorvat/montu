package ar.edu.itba.montu.abstraction;

public class Characteristic<T>{
  /*Why an object for this? Possible future of power ups by having a list of them and calculating in getValue*/
  private T value;

  public Characteristic(T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }
}
