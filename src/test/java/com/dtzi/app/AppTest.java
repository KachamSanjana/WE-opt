package com.dtzi.app;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void multipleFoodTestCases() {
       Food banana=new Food(15,10.0f);
       Food potato =new Food(10,15.0f);

        assertEquals(15,banana.getHpRestore());
        assertEquals(10.0f,banana.getPrice());
        
        assertEquals(10,potato.getHpRestore());
        assertEquals(15.0f,potato.getPrice());
    }
}
