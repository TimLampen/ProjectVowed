package me.vowed.api.race.test;

/**
 * Created by JPaul on 12/26/2015.
 */
import boofcv.alg.feature.detect.template.TemplateMatching;
import boofcv.alg.feature.detect.template.TemplateMatchingIntensity;
import boofcv.alg.misc.ImageStatistics;
import boofcv.alg.misc.PixelMath;
import boofcv.factory.feature.detect.template.FactoryTemplateMatching;
import boofcv.factory.feature.detect.template.TemplateScoreType;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import boofcv.io.UtilIO;
import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.feature.Match;
import boofcv.struct.image.ImageFloat32;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Example of how to find objects inside an image using template matching.  Template matching works
 * well when there is little noise in the image and the object's appearance is known and static.  It can
 * also be very slow to compute, depending on the image and template size.
 *
 * @author Peter Abeles
 */
public class ExampleTemplateMatching {

    /**
     * Demonstrates how to search for matches of a template inside an image
     *
     * @param image           Image being searched
     * @param template        Template being looked for
     * @param mask            Mask which determines the weight of each template pixel in the match score
     * @param expectedMatches Number of expected matches it hopes to find
     * @return List of match location and scores
     */
    private static List<Match> findMatches(ImageFloat32 image, ImageFloat32 template, ImageFloat32 mask,
                                           int expectedMatches) {
        // create template matcher.
        TemplateMatching<ImageFloat32> matcher =
                FactoryTemplateMatching.createMatcher(TemplateScoreType.SUM_DIFF_SQ, ImageFloat32.class);

        // Find the points which match the template the best
        matcher.setTemplate(template, mask,expectedMatches);
        matcher.process(image);

        return matcher.getResults().toList();

    }

    /**
     * Computes the template match intensity image and displays the results. Brighter intensity indicates
     * a better match to the template.
     */
    public static void showMatchIntensity(ImageFloat32 image, ImageFloat32 template, ImageFloat32 mask) {

        // create algorithm for computing intensity image
        TemplateMatchingIntensity<ImageFloat32> matchIntensity =
                FactoryTemplateMatching.createIntensity(TemplateScoreType.SUM_DIFF_SQ, ImageFloat32.class);

        // apply the template to the image
        matchIntensity.process(image, template, mask);

        // get the results
        ImageFloat32 intensity = matchIntensity.getIntensity();

        // adjust the intensity image so that white indicates a good match and black a poor match
        // the scale is kept linear to highlight how ambiguous the solution is
        float min = ImageStatistics.min(intensity);
        float max = ImageStatistics.max(intensity);
        float range = max - min;
        PixelMath.plus(intensity, -min, intensity);
        PixelMath.divide(intensity, range, intensity);
        PixelMath.multiply(intensity, 255.0f, intensity);

        BufferedImage output = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_BGR);
        VisualizeImageData.grayMagnitude(intensity, output, -1);
        ShowImages.showWindow(output, "Match Intensity", true);
    }

    public static void main(String args[]) {

        // Load image and templates
        String directory = UtilIO.pathExample("C:\\Users\\JPaul\\ProjectVowed\\lib\\pics");

        ImageFloat32 image = UtilImageIO.loadImage(directory ,"SOURCE.jpg", ImageFloat32.class);
        ImageFloat32 templateCursor = UtilImageIO.loadImage(directory , "COMPARER.png", ImageFloat32.class);
        ImageFloat32 maskCursor = UtilImageIO.loadImage(directory , "COMPARER.png", ImageFloat32.class);

        // create output image to show results
        BufferedImage output = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_BGR);
        ConvertBufferedImage.convertTo(image, output);
        Graphics2D g2 = output.createGraphics();

        // Search for the cursor in the image.  For demonstration purposes it has been pasted 3 times
        g2.setColor(Color.RED); g2.setStroke(new BasicStroke(5));
        drawRectangles(g2, image, templateCursor, null, 1);
        // show match intensity image for this template

        // Now it's try finding the cursor without a mask.  it will get confused when the background is black

        // Now it searches for a specific icon for which there is only one match

        ShowImages.showWindow(output, "Found Matches",true);
    }

    /**
     * Helper function will is finds matches and displays the results as colored rectangles
     */
    private static void drawRectangles(Graphics2D g2,
                                       ImageFloat32 image, ImageFloat32 template, ImageFloat32 mask,
                                       int expectedMatches) {
        List<Match> found = findMatches(image, template, mask, expectedMatches);

        int r = 2;
        int w = template.width + 2 * r;
        int h = template.height + 2 * r;

        for (Match m : found) {
            // the return point is the template's top left corner
            int x0 = m.x - r;
            int y0 = m.y - r;
            int x1 = x0 + w;
            int y1 = y0 + h;

            g2.drawLine(x0, y0, x1, y0);
            g2.drawLine(x1, y0, x1, y1);
            g2.drawLine(x1, y1, x0, y1);
            g2.drawLine(x0, y1, x0, y0);
        }
    }
}
