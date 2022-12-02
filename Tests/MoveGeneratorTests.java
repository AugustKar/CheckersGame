import model.Board;
import model.MoveGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorTests {
    @Test
    public void checkerEndPointsListSize(){
        List<Point> endPoints = new ArrayList<>();
        int startIndex = 7;
        Point point = Board.toPoint(startIndex);
        int move = 1;

        MoveGenerator.addPoints(endPoints, point, move);
        Assert.assertEquals(endPoints.size(), 4);


    }
}