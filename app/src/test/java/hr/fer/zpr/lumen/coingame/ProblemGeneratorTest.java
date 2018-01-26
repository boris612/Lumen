package hr.fer.zpr.lumen.coingame;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ddarian on 13.12.17..
 */
public class ProblemGeneratorTest {
    private ProblemGenerator generator = new ProblemGenerator();

    @Test
    public void generirajBroj1() throws Exception {
        generator.reset();
        int number = generator.generirajBroj();

        assertEquals(number, generator.getCurrentNumber());
        assertEquals(Integer.valueOf(number), generator.getRecentNumbers().peek());
    }

    @Test
    public void generirajBroj2() throws Exception {
//        generator.reset();
        int number = generator.generirajBroj();

        assertEquals(number, generator.getCurrentNumber());
        assertEquals(2, generator.getRecentNumbers().size());
    }


    @Test
    public void generateSolution() throws Exception {
    }

    @Test
    public void isOptimal() throws Exception {
    }

}