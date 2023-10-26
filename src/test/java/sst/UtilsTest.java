package sst;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.Console;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import Model.Coordinate;
import Model.Enterprise;
import Model.Entity;
import Model.Game;
import Model.Klingon;
import Model.KlingonCommander;
import Model.Planet;
import Model.Position;
import Utils.Utils;

public class UtilsTest {
    private Game game;
    
    @Before
    public void setUp() throws JsonProcessingException {
        game = mock(Game.class);
        game.con = mock(Console.class);
    }
    
    @Test
    public void randIntShouldWorkAsExpected() {
        int randomVal = Utils.randInt(0, 10);
        for (int i = 0; i < 100; i++) {
            assertTrue(randomVal >= 0);
            assertTrue(randomVal <= 10);
            randomVal = Utils.randInt(0, 10);
        }
    }

    @Test
    public void randDoubleShouldWorkAsExpected() {
        Double randomVal = Utils.randDouble(0, 10);
        for (int i = 0; i < 100; i++) {
            assertTrue(randomVal >= 0);
            assertTrue(randomVal <= 10);
            randomVal = Utils.randDouble(0, 10);
        }
    }
    
    @Test
    public void serializeShouldWorkAsExpected() throws Exception {
        ObjectMapper mapper = mock(ObjectMapper.class);
        String gameJson = "{\"starDate\":0.0,\"map\":null,\"klingons\":null," +
                "\"klingonCommanders\":null,\"klingonSuperCommander\":null," +
                "\"enterprise\":null,\"planets\":null,\"starbases\":null," +
                "\"stars\":null,\"romulans\":null,\"time\":0.0,\"skill\":null," +
                "\"length\":null,\"type\":null,\"scannedQuadrants\":{},\"klingonCount\":0}";

        when(mapper.writeValueAsString(any(Game.class))).thenReturn(gameJson);

        assertEquals(gameJson, Utils.serialize(game));
    }

    // @Test
    // public void deserializeShouldWordAsExpected() throws JsonProcessingException {
    //     String gameJson = "{\"starDate\":0.0,\"map\":null,\"klingons\":null,\"klingonCommanders\":null,\"klingonSuperCommander\":null,\"enterprise\":null,\"planets\":null,\"starbases\":null,\"stars\":null,\"romulans\":null,\"time\":0.0,\"skill\":null,\"length\":null,\"type\":null,\"scannedQuadrants\":{}}";
    //     Game mockGame;
    //     ObjectMapper mapper = mock(ObjectMapper.class);

    //     when(mapper.readValue(anyString(), eq(Game.class))).thenReturn(game);
        
    //     mockGame = Utils.deserialize(gameJson, Game.class);
    //     assertEquals(this.game, mockGame);
    // }

    @Test
    public void testTurnEntityQuadrantsToStrings() {
        Enterprise enterprise = mock(Enterprise.class);
        Klingon klingon = mock(Klingon.class);
        KlingonCommander klingonCommander = mock(KlingonCommander.class);
        Planet planet = mock(Planet.class);
        Entity[] entities = {enterprise, klingon, klingonCommander, planet};

        when(enterprise.getPosition()).thenReturn(new Position(new Coordinate(0, 0), new Coordinate(0, 0)));
        when(klingon.getPosition()).thenReturn(new Position(new Coordinate(0, 0), new Coordinate(0, 0)));
        when(klingonCommander.getPosition()).thenReturn(new Position(new Coordinate(0, 0), new Coordinate(0, 0)));
        when(planet.getPosition()).thenReturn(new Position(new Coordinate(0, 0), new Coordinate(0, 0)));

        String expected = "1 - 1  1 - 1  1 - 1  1 - 1  ";
        assertEquals(expected, Utils.turnEntityQuadrantsToStrings(entities));
    }

    @Test
    public void readCommandsShouldReturnEmptyOptionalForEmptyString() {
        Optional<List<String>> result = Utils.readCommands("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void readCommandsShouldReturnEmptyOptionalForWhitespaceString() {
        Optional<List<String>> result = Utils.readCommands("   ");
        assertTrue(result.isEmpty());
    }

    @Test
    public void readCommandsShouldReturnEmptyOptionalForPunctuationString() {
        Optional<List<String>> result = Utils.readCommands(".,;:");
        assertTrue(result.get().size() == 1);
    }

    @Test
    public void readCommandsShouldReturnCommandAndParams() {
        Optional<List<String>> result = Utils.readCommands("COMMAND param1 param2");
        assertTrue(result.isPresent());
        List<String> expected = List.of("COMMAND", "PARAM1", "PARAM2");
        assertEquals(expected, result.get());
    }

    @Test
    public void parseIntegersShouldReturnEmptyOptionalForEmptyString() {
        Optional<List<Integer>> result = Utils.parseIntegers("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseIntegersShouldReturnEmptyOptionalForWhitespaceString() {
        Optional<List<Integer>> result = Utils.parseIntegers("   ");
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseIntegersShouldReturnEmptyOptionalForNonIntegerString() {
        Optional<List<Integer>> result = Utils.parseIntegers("abc");
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseIntegersShouldReturnListOfIntegers() {
        Optional<List<Integer>> result = Utils.parseIntegers("1 2 3 abc");
        assertTrue(result.isPresent());
        List<Integer> expected = List.of(1, 2, 3);
        assertEquals(expected, result.get());
    }

    @Test
    public void parseDoublesShouldReturnEmptyOptionalForEmptyString() {
        Optional<List<Double>> result = Utils.parseDoubles("");
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseDoublesShouldReturnEmptyOptionalForWhitespaceString() {
        Optional<List<Double>> result = Utils.parseDoubles("   ");
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseDoublesShouldReturnEmptyOptionalForNonDoubleString() {
        Optional<List<Double>> result = Utils.parseDoubles("abc");
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseDoublesShouldReturnListOfDoubles() {
        Optional<List<Double>> result = Utils.parseDoubles("1.0 2.5 3.7 abc");
        assertTrue(result.isPresent());
        List<Double> expected = List.of(1.0, 2.5, 3.7);
        assertEquals(expected, result.get());
    }

    @Test
    public void parseIntegersListShouldReturnEmptyListForNoIntegers() {
        List<String> params = List.of("abc", "def", "ghi");
        List<Integer> result = Utils.parseIntegers(params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseIntegersListShouldReturnListOfIntegers() {
        List<String> params = List.of("1", "2", "3", "abc");
        List<Integer> result = Utils.parseIntegers(params);
        List<Integer> expected = List.of(1, 2, 3);
        assertEquals(expected, result);
    }

    @Test
    public void parseDoublesListShouldReturnEmptyListForNoDoubles() {
        List<String> params = List.of("abc", "def", "ghi");
        List<Double> result = Utils.parseDoubles(params);
        assertTrue(result.isEmpty());
    }

    @Test
    public void parseDoublesListShouldReturnListOfDoubles() {
        List<String> params = List.of("1.0", "2.5", "3.7", "abc");
        List<Double> result = Utils.parseDoubles(params);
        List<Double> expected = List.of(1.0, 2.5, 3.7);
        assertEquals(expected, result);
    }
}
