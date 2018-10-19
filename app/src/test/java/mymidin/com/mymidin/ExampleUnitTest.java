package mymidin.com.mymidin;

import com.google.firebase.FirebaseApp;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Product;
import model.ProductType;
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
    public void testProductTypeValue(){

        for (ProductType type:ProductType.values()) {
            System.out.println(type.name()+" : "+type.getId());
        }

    }

}