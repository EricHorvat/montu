package ar.edu.itba.montu;

import ar.edu.itba.montu.war.environment.WarEnviromentGenerator;
import ar.edu.itba.montu.war.environment.WarEnvironment;
import ar.edu.itba.montu.war.utils.RandomUtil;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws Exception {

		/*TODO GET SEED*/
		RandomUtil.setRandom(1);

		//TODO ADD ARGUMENTS
    WarEnviromentGenerator.generate();

		final WarEnvironment warEnvironment = WarEnvironment.getInstance();

		if (warEnvironment == null) {
			throw new Exception("Run WarEnviromentGenerator.generate before getting an instance of WarEnvironment");
		}
		
		final long time = 5000;
		
		warEnvironment.start(time);
		
//        System.out.println( "Hello World!" );
//        Test t = new Test();
//        for (int i = 0; i < 5000; i++)
//            t.turn(i);
//        try {
//            BufferedWriter writer = new BufferedWriter(new FileWriter("./output/test.txt"));
//            writer.write(t.outfile.getValue());
//
//            writer.close();
//        }catch (IOException e){
//            e.getMessage();
//        }

    }
}
