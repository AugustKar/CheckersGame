import model.Board;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;

public class ConvertIndexesAndPointsTests {
    @Test
    public void IndexToPoint(){

        int index1 = 0;
        int index2 = 7;
        Point point1 = new Point(1, 0);
        Point point2 = new Point(4, 1);

        Point p1 = Board.toPoint(index1);
        Point p2 = Board.toPoint(index2);

        Assert.assertEquals(p1.x, point1.x);
        Assert.assertEquals(p2.y, point2.y);
    }

    @Test
    public void PointToIndex(){

        Point point1 = new Point(1, 0);
        Point point2 = new Point(4, 1);
        int index1 = 0;
        int index2 = 6;

        int i1 = Board.toIndex(point1.x, point1.y);
        int i2 = Board.toIndex(point2.x, point2.y);

        Assert.assertEquals(i1, index1);
        Assert.assertEquals(i2, index2);
    }
}
