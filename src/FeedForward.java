import javax.imageio.ImageIO; 
import java.awt.image.BufferedImage; 
import java.io.*; 
import java.util.*;

public class FeedForward
{
	public static void main(String[] args) throws IOException 
	{	
		double arrayOne[][] = new double[300][785];
		createArray("hidden-weights.txt", arrayOne);
		double arrayTwo[][] = new double[10][301];
		createArray("output-weights.txt", arrayTwo);
		
		for (int i = 0; i < 100; i++)
		{		
			BufferedImage img = null;
			img = ImageIO.read(new File("src/" + i + ".png"));
			double[] dummy = null;
			double[] X = img.getData().getPixels(0, 0, img.getWidth(), img.getHeight(), dummy);
			changeScale(X);	
			
			double[] temp = calculateHidden(arrayOne, X);
			calculateOutput(temp, arrayTwo);
		}	
	}
	
	public static void createArray(String file, double[][] aa) throws FileNotFoundException
	{
		FileReader fileInput = new FileReader(file);
		Scanner fileScanner = new Scanner(fileInput);
		while (fileScanner.hasNextDouble())
		{
			for (int i = 0; i < aa.length; i++)
			{
				for (int j = 0; j < aa[0].length; j++)
				{
					double num = fileScanner.nextDouble();
					aa[i][j] = num;
				}
			}			
		}		
		fileScanner.close();
	}	
	
	public static void changeScale(double[] a)
	{
		for (int i = 0; i < a.length; i ++)
		{
			if (a[i] > 0 || a[i] < 0)
			{
				a[i] = a[i]/255.0;
			}
		}
	}
	
	public static double[] calculateHidden(double[][] hidden, double[] image)
	{
		double[] stepOne = new double[hidden.length];
		double[] endResult = new double[stepOne.length];
		
		for (int i = 0; i < hidden.length; i++)
		{
			for (int j = 0; j < hidden[0].length; j++)
			{
				if (j == (hidden[0].length - 1))
				{
					stepOne[i] += hidden[i][j];
				}
				else
				{
					if (image[j] > 0)
					{
						stepOne[i] += (j*hidden[i][j]);
					}
					else
					{
						stepOne[i] += 0;
					}
				}
			}
		}
		for (int i = 0; i < stepOne.length; i++)
		{
			endResult[i] = (1/(1 + Math.pow(Math.E,(-1*stepOne[i]))));
		}
		return endResult;
	}
	
	public static void calculateOutput(double[] hidden, double[][] output)
	{
		double[] stepTwo = new double[output.length];
		double[] endResult = new double[stepTwo.length];
		
		for (int i = 0; i < output.length; i++)
		{
			for (int j = 0; j < output[0].length; j++)
			{
				if (j == (output[0].length - 1))
				{
					stepTwo[i] += output[i][j];
				}
				else
				{
					if (hidden[j] > 0)
					stepTwo[i] += (j*output[i][j]);
					else
					stepTwo[i] += 0;
				}
			}
		}
		
		double max = 0;
		int index = 0;
		
		for (int i = 0; i < stepTwo.length; i++)
		{
			endResult[i] = (1/(1 + Math.pow(Math.E,(-1*stepTwo[i]))));
			
			if (endResult[i] > max)
			{
				max = endResult[i];
				index = i;
			}
		}
		
		System.out.println("The network prediction is " + index);
	}
}