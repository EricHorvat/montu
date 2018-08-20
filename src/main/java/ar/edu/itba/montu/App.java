package ar.edu.itba.montu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        Test t = new Test();
        for (int i = 0; i < 5000; i++)
            t.turn(i);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./output/test.txt"));
            writer.write(t.outfile.getValue());

            writer.close();
        }catch (IOException e){
            e.getMessage();
        }

    }
}
