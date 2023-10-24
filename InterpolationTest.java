import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InterpolationTest {

    @Test
    public void polynomialCoefficients() {
        var expectedCoefficients = List.of(1,-6,11,-6);
        assertEquals(expectedCoefficients, Interpolation.polynomialCoefficients(List.of(1,2,3)));

        expectedCoefficients = List.of(1,-10,35,-50,24);
        assertEquals(expectedCoefficients, Interpolation.polynomialCoefficients(List.of(1,2,3,4)));

        assertThrows(NullPointerException.class, () -> Interpolation.polynomialCoefficients(null));
        var containsNull = Arrays.asList(1,2,null,4);
        assertThrows(IllegalArgumentException.class, () -> Interpolation.polynomialCoefficients(containsNull));

        var badList = List.of(2,1,3);
        assertThrows(IllegalArgumentException.class, () -> Interpolation.polynomialCoefficients(badList));
        var badList2 = List.of(1,2,2,3);
        assertThrows(IllegalArgumentException.class, () -> Interpolation.polynomialCoefficients(badList2));
    }
}