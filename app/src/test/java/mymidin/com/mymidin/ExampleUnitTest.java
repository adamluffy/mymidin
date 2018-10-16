package mymidin.com.mymidin;

import com.google.firebase.FirebaseApp;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Product;
import respository.ProductDatabase;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testIncrementNumber(){

        String password = "IV00000001";
        password = password.replaceAll("\\D+","");
        String increment = String.format("%0"+password.length()+"d",
                Integer.parseInt(password)+1);
        String salesNumber = "IV"+increment;
        assertEquals("IV00000002",salesNumber);

    }

}