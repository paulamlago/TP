package es.ucm.fdi.tp;



import static org.junit.Assert.*;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;



public class MainTest {

public static void MainTest(String[] args){
		assertEquals("Players should be just 2", 2, args.length - 1);
		assertEquals("That game is not defined", "WAS", args[0]);
		Result result = JUnitCore.runClasses(WolfAndSheepStateTest.class);
		for (Failure fail: result.getFailures()){
			System.out.println(fail.toString());
		}

}


}
