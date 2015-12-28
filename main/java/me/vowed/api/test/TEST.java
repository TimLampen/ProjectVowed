package me.vowed.api.test;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by JPaul on 12/26/2015.
 */
public class TEST
{
    public static void main(String[] args) throws IOException
    {
        GifDecoder gifDecoder = new GifDecoder();
        gifDecoder.read("C:\\Users\\JPaul\\Desktop\\Server\\plugins\\VowedCore\\Transactions\\images\\GIFResult.gif");

        int frameCount = gifDecoder.getFrameCount();

        System.out.println(frameCount);
    }
}
