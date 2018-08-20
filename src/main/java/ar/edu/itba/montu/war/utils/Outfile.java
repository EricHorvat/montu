package ar.edu.itba.montu.war.utils;

public class Outfile {

    private String value;

    public Outfile() {
        value = "";
    }

    public void addLine(String s){
        value += s + '\n';
    }

    public void addLine(Integer s){
        value += "" + s + '\n';
    }

    public void addInit(Integer s, Integer i){
        addLine(s+8);
        addLine(i);
        addLine("0\t0\t0\t0");
        addLine("0\t200\t0\t0");
        addLine("200\t0\t0\t0");
        addLine("200\t200\t0\t0");
    }

    public String getValue() {
        return value;
    }
}
