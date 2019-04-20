/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package texasholdem;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sierramws
 */
public class CreditTest {
    
    public CreditTest() {
    }

    /**
     * Test of difference method, of class Credit.
     */
    @Test
    public void testDifferenceR1() {
        System.out.println("difference");
        int highestBet = 0;
        GameStatus round = GameStatus.ROUNDONE;
        Credit instance = new Credit(10,20,30);
        int expResult = -10;
        int result = instance.difference(highestBet, round);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of difference method, of class Credit.
     */
    @Test
    public void testDifferenceR2() {
        System.out.println("difference");
        int highestBet = 0;
        GameStatus round = GameStatus.ROUNDTWO;
        Credit instance = new Credit(10,20,30);
        int expResult = -20;
        int result = instance.difference(highestBet, round);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of difference method, of class Credit.
     */
    @Test
    public void testDifferenceR3() {
        System.out.println("difference");
        int highestBet = 0;
        GameStatus round = GameStatus.ROUNDTHREE;
        Credit instance = new Credit(10,20,30);
        int expResult = -30;
        int result = instance.difference(highestBet, round);
        assertEquals(expResult, result);
    }

    /**
     * Test of place method, of class Credit.
     */
    @Test
    public void testPlaceGood() {
        System.out.println("place");
        int highestBet = 5;
        int amount = 10;
        GameStatus round = GameStatus.ROUNDONE;
        Credit instance = new Credit(20,20,30);
        boolean expResult = true;
        boolean result = instance.place(highestBet, amount, round);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of place method, of class Credit.
     */
    @Test
    public void testPlaceBad() {
        System.out.println("place");
        int highestBet = 25;
        int amount = 2;
        GameStatus round = GameStatus.ROUNDONE;
        Credit instance = new Credit(20,20,30);
        boolean expResult = false;
        boolean result = instance.place(highestBet, amount, round);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of place method, of class Credit.
     */
    @Test
    public void testPlaceBoundary() {
        System.out.println("place");
        int highestBet = 5;
        int amount = 5;
        GameStatus round = GameStatus.ROUNDONE;
        Credit instance = new Credit(20,20,30);
        boolean expResult = true;
        boolean result = instance.place(highestBet, amount, round);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of creditAt method, of class Credit.
     */
    @Test
    public void testCreditAtR1() {
        System.out.println("creditAt");
        GameStatus round = GameStatus.ROUNDONE;
        Credit instance = new Credit(10,20,30);
        int expResult = 10;
        int result = instance.creditAt(round);
        assertEquals(expResult, result);
       
    }
    
    /**
     * Test of creditAt method, of class Credit.
     */
    @Test
    public void testCreditAtR2() {
        System.out.println("creditAt");
        GameStatus round = GameStatus.ROUNDTWO;
        Credit instance = new Credit(10,20,30);
        int expResult = 20;
        int result = instance.creditAt(round);
        assertEquals(expResult, result);
       
    }
    
    /**
     * Test of creditAt method, of class Credit.
     */
    @Test
    public void testCreditAtR3() {
        System.out.println("creditAt");
        GameStatus round = GameStatus.ROUNDTHREE;
        Credit instance = new Credit(10,20,30);
        int expResult = 30;
        int result = instance.creditAt(round);
        assertEquals(expResult, result);
       
    }
    
}
