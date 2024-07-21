package coffee.dape.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageUtils
{
	/**
	 * Retrieves an image from a URL
	 * @param url URL destination of image
	 * @return BufferedImage
	 */
	public static BufferedImage getImageFromURL(URL url)
	{
		BufferedImage image;
		
        try
        {
            image = ImageIO.read(url);
            return image;
        }
        catch (IOException e)
        {
            Logg.error("Failed to read image from URL: " + url.toString(),e);
            return null;
        }
	}
	
	/**
	 * Resizes an image (might not work on Linux pls check)
	 * @param img BufferedImage
	 * @param width new width
	 * @param height new height
	 * @return BufferedImage
	 */
	public static BufferedImage resize(BufferedImage img,int width,int height)
	{
		Image tmp = img.getScaledInstance(width,height,Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp,0,0,null);
		g2d.dispose();
		return dimg;
	}
	
	/**
	 * Creates a blank image of a colour
	 * @param colour Colour
	 * @param width image width
	 * @param height image height
	 * @return BufferedImage
	 */
	public static BufferedImage getBlank(Color colour,int width,int height)
	{
		BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(colour);
		g2d.fillRect(0,0,width,height);
		g2d.dispose();
		return img;
	}
	
	/**
	 * Draws 1 image on top of another
	 * @param img Image being drawn on top of
	 * @param img2 Image being drawn 
	 * @param x X offset
	 * @param y Y offset
	 * @return BufferedImage
	 */
	public static BufferedImage drawImageOntop(BufferedImage img,BufferedImage img2,int x,int y)
	{
		Graphics2D g2d = img.createGraphics();
		g2d.drawImage(img2,x,y,null);
		g2d.dispose();
		return img;
	}
}
