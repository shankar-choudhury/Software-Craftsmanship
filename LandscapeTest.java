package Geology;

import java.util.*;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests Landscape class.
 * You can assume there are no errors here.
 * All the tests are not provided here; you will have to add them yourself.
 */
public class LandscapeTest {
    private final Logger logger = Logger.getLogger(Landscape.class.getName());
    private final LoggerTestingHandler handler = new LoggerTestingHandler();

    @Before
    public void setup() {
        logger.addHandler(handler);
    }

    @Test
    public void testModificationRecord() {
        Landscape.Modification record = new Landscape.Modification(0, 4, Landscape.Operation.RAISE);

        assertEquals(0, record.x1());
        assertEquals(4, record.x2());
        assertEquals(Landscape.Operation.RAISE, record.operation());
    }

    @Test
    public void testGet() {
        Landscape landscape = new Landscape();
        Landscape.TestHook testHook = landscape.new TestHook();

        assertEquals(0, testHook.get().size());
    }

    @Test
    public void testRaise() {
        Landscape l = new Landscape();
        Landscape.TestHook testHook = l.new TestHook();

        assertThrows(AssertionError.class, () -> l.modify(6, 0, Landscape.Operation.RAISE));
        assertEquals(Map.of(), testHook.get());
        assertEquals("x1 cannot be greater than x2; returning early", handler.getLastLog().orElse("Failed test"));


        l.modify(0, 0, Landscape.Operation.RAISE);
        assertEquals(Map.of(0,1), testHook.get());

        l.modify(0, 3, Landscape.Operation.RAISE);
        assertEquals(Map.of(0,2,1,1,2,1, 3, 1), testHook.get());

        l.modify(-3, 0, Landscape.Operation.RAISE);
        assertEquals(Map.of(-3, 1, -2, 1, -1, 1, 0, 3, 1, 1, 2, 1, 3, 1), testHook.get());
    }

    @Test
    public void testDepress() {
        Landscape l = new Landscape();
        Landscape.TestHook t = l.new TestHook();

        assertThrows(AssertionError.class, () -> l.modify(6, 0, Landscape.Operation.DEPRESS));
        assertEquals(Map.of(), t.get());
        assertEquals("x1 cannot be greater than x2; returning early", handler.getLastLog().orElse("Nothing in here"));

        l.modify(0,0, Landscape.Operation.DEPRESS);
        assertEquals(Map.of(0, -1), t.get());

        l.modify(0, 2, Landscape.Operation.DEPRESS);
        assertEquals(Map.of(0, -2, 1, -1, 2, -1), t.get());

        l.modify(-2, 0, Landscape.Operation.DEPRESS);
        assertEquals(Map.of(-2, -1, -1, -1, 0, -3, 1, -1, 2, -1), t.get());
    }

    @Test
    public void testCreateHillEven() {
        Landscape l = new Landscape();
        Landscape.TestHook t = l.new TestHook();

        assertThrows(AssertionError.class, () -> l.modify(1, -2, Landscape.Operation.RAISE));
        assertEquals(Map.of(), t.get());
        assertEquals("x1 cannot be greater than x2; returning early", handler.getLastLog().orElse("Failed test"));

        int x1 = -2;
        int x2 = 1;
        l.modify(x1, x2, Landscape.Operation.HILL);
        Map<Integer, Integer> m = t.get();
        List<Integer> expectedHeights = List.of(1, 2, 2, 1);
        Iterator<Integer> expectedIt = expectedHeights.iterator();

        for (int i = x1; i <= x2; i++)
            assertEquals(expectedIt.next(), m.get(i));
    }

    @Test
    public void testCreateHillOdd() {
        Landscape l = new Landscape();
        Landscape.TestHook t = l.new TestHook();

        assertThrows(AssertionError.class, () -> l.modify(2, -2, Landscape.Operation.HILL));
        assertEquals(Map.of(), t.get());
        assertEquals("x1 cannot be greater than x2; returning early", handler.getLastLog().orElse("Failed test"));

        int x1 = -2;
        int x2 = 2;
        l.modify(x1, x2, Landscape.Operation.HILL);
        Map<Integer, Integer> m = t.get();
        List<Integer> expectedHeights = List.of(1, 2, 3, 2, 1);
        Iterator<Integer> expectedIt = expectedHeights.iterator();

        for (int i = x1; i <= x2; i++)
            assertEquals(expectedIt.next(), m.get(i));
    }

    @Test
    public void testCreateValleyEven() {
        Landscape landscape = new Landscape();
        Landscape.TestHook testHook = landscape.new TestHook();
        landscape.modify(0, 3, Landscape.Operation.VALLEY);

        Map<Integer, Integer> mapAfterCallingModify = testHook.get();

        landscape = new Landscape();
        testHook = landscape.new TestHook();
        testHook.createValley(0, 3);

        Map<Integer, Integer> mapAfterCallingCreateValley = testHook.get();

        assertEquals(mapAfterCallingModify, mapAfterCallingCreateValley);
    }

    @Test
    public void testCreateValleyOdd() {
        Landscape l = new Landscape();
        Landscape.TestHook t = l.new TestHook();

        assertThrows(AssertionError.class, () -> l.modify(2, -2, Landscape.Operation.VALLEY));
        assertEquals(Map.of(), t.get());
        assertEquals("x1 cannot be greater than x2; returning early", handler.getLastLog().orElse("Failed test"));

        int x1 = -2;
        int x2 = 2;
        l.modify(x1, x2, Landscape.Operation.VALLEY);
        Map<Integer, Integer> m = t.get();
        List<Integer> expectedHeights = List.of(-1, -2, -3, -2, -1);
        Iterator<Integer> expectedIt = expectedHeights.iterator();

        for (int i = x1; i <= x2; i++)
            assertEquals(expectedIt.next(), m.get(i));
    }

    @Test
    public void testUnknownOperation() {
        Landscape l = new Landscape();
        l.modify(0, 4, Landscape.Operation.FOO);
        assertEquals("Unknown operation, skipping", handler.getLastLog().orElse("You suck"));
    }

    @Test
    public void testRaiseOrDepress() {
        Landscape l = new Landscape();
        Landscape.TestHook t = l.new TestHook();

        assertThrows(AssertionError.class, () -> t.raiseOrDepress(7, 2, Landscape.Operation.DEPRESS));
        assertThrows(AssertionError.class, () -> t.raiseOrDepress(2, 7, null));
        assertThrows(AssertionError.class, () -> t.raiseOrDepress(2, 7, Landscape.Operation.HILL));
        assertThrows(AssertionError.class, () -> t.raiseOrDepress(2, 7, Landscape.Operation.VALLEY));
        assertThrows(AssertionError.class, () -> t.raiseOrDepress(2, 7, Landscape.Operation.FOO));

        t.raiseOrDepress(2, 5, Landscape.Operation.RAISE);
        assertEquals(Map.of(2, 1, 3, 1, 4, 1, 5, 1), t.get());
        t.raiseOrDepress(2, 5, Landscape.Operation.DEPRESS);
        assertEquals(Map.of(2, 0, 3, 0, 4, 0, 5, 0), t.get());
    }

    @Test
    public void testCreateValleyOrHill() {
        Landscape l = new Landscape();
        Landscape.TestHook t = l.new TestHook();

        assertThrows(AssertionError.class, () -> t.createHillOrValley(3,2, Landscape.Operation.VALLEY));
        assertThrows(AssertionError.class, () -> t.createHillOrValley(2,3, null));
        assertThrows(AssertionError.class, () -> t.createHillOrValley(2, 2, Landscape.Operation.HILL));

        t.createHillOrValley(1, 2, Landscape.Operation.HILL);
        assertEquals(Map.of(1, 1, 2, 1), t.get());
    }

    @Test
    public void testInitAmount() {
        Landscape l = new Landscape();
        Landscape.TestHook t = l.new TestHook();

        assertThrows(AssertionError.class, () -> t.initAmount(null));
        assertThrows(AssertionError.class, () -> t.initAmount(Landscape.Operation.FOO));

        assertEquals(1, t.initAmount(Landscape.Operation.RAISE));
        assertEquals(-1, t.initAmount(Landscape.Operation.DEPRESS));
        assertEquals(1, t.initAmount(Landscape.Operation.HILL));
        assertEquals(-1, t.initAmount(Landscape.Operation.VALLEY));
    }

    @Test
    public void testAdjust() {
        Landscape l = new Landscape();
        Landscape.TestHook t = l.new TestHook();

        assertThrows(AssertionError.class, () -> t.adjust(0, null));
        assertThrows(AssertionError.class, () -> t.adjust(1, Landscape.Operation.RAISE));
        assertThrows(AssertionError.class, () -> t.adjust(-1, Landscape.Operation.DEPRESS));

        assertEquals(1, t.adjust(0, Landscape.Operation.HILL));
        assertEquals(-1, t.adjust(0, Landscape.Operation.VALLEY));
    }

    @Test
    public void testVerifyNullModifications() {
        Landscape landscape = new Landscape();

        assertFalse(landscape.verify(null, List.of(0, 0, 0, 0, 0)));
    }

    @Test
    public void testVerifyNullHeights() {
        Landscape landscape = new Landscape();
        Landscape.Modification modification = new Landscape.Modification(0, 3, Landscape.Operation.VALLEY);

        assertFalse(landscape.verify(List.of(modification), null));
    }

    @Test
    public void testVerifyEmptyHeights() {
        Landscape landscape = new Landscape();
        Landscape.Modification modification = new Landscape.Modification(0, 3, Landscape.Operation.VALLEY);

        assertTrue(landscape.verify(List.of(modification), List.of()));
    }

    @Test
    public void testVerifyHeightsWithNull() {
        Landscape landscape = new Landscape();
        Landscape.Modification modification = new Landscape.Modification(0, 3, Landscape.Operation.VALLEY);

        assertFalse(landscape.verify(List.of(modification), Arrays.asList(0, 0, null, 0)));
    }

    @Test
    public void testVerifyModificationsWithNull() {
        Landscape landscape = new Landscape();
        var modList = new ArrayList<Landscape.Modification>();
        modList.add(new Landscape.Modification(0, 3, Landscape.Operation.VALLEY));
        modList.add(new Landscape.Modification(0, 3, Landscape.Operation.HILL));
        modList.add(null);
        landscape.verify(modList, List.of(0, 0, 0, 0));
        assertEquals("modification is null, skipping", handler.getLastLog().orElse("Failed test"));
    }

    @Test
    public void testVerify() {
        var landscape = new Landscape();
        var l = new Landscape();
        var modList = new ArrayList<Landscape.Modification>();
        var modList2 = new ArrayList<Landscape.Modification>();

        modList2.add(new Landscape.Modification(0, 0, Landscape.Operation.RAISE));
        assertTrue(l.verify(modList2, List.of(1)));

        modList.add(new Landscape.Modification(0, 3, Landscape.Operation.VALLEY));
        modList.add(new Landscape.Modification(0, 3, Landscape.Operation.HILL));

        assertTrue(landscape.verify(modList, List.of(0, 0, 0, 0)));
        assertFalse(landscape.verify(modList, List.of(0, 0, 1, 0)));
    }
}
