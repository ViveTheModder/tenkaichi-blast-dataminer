package cmd;
//Tenkaichi Blast Dataminer v1.0 by ViveTheModder
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class MainApp 
{
	//flags are in this order: isBlocked, isBoosted, duringMPM, duringBlastCombo
	static boolean[] blastDamageFlags = new boolean[4];
	static File list; //currently loaded skill list
	static final int CHARA_COUNT = 161;
	static String[] blastNames = new String[5];
	static String[] charaNames = new String[CHARA_COUNT];
	static final String BLAST_1_PATH = "./blast1-param/";
	static final String BLAST_2_PATH = "./blast2-param/";
	static final String SKL_LST_PATH = "./skill-lists/";
	
	public static boolean hasDestroyPlanetFlag(RandomAccessFile param, int blastID) throws IOException
	{
		short pos;
		short[] posValues = {2,6,10};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		param.seek(pos);
		byte flagset = param.readByte();
		if ((flagset & 0x20)==0x20) return true;
		return false;
	}
	public static boolean hasBeamStruggleFlag(RandomAccessFile param, int blastID) throws IOException
	{
		short pos;
		short[] posValues = {1,5,9};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		param.seek(pos);
		byte flagset = param.readByte();
		if ((flagset & 1)!=1) return true;
		return false;
	}
	public static boolean isStackable(RandomAccessFile param, int blastID) throws IOException
	{
		short pos;
		if (blastID>2) return false;
		else if (blastID==2) pos=13;
		else if (blastID==1) pos=9;
		else return false;
		
		param.seek(pos);
		byte flagset = param.readByte();
		if ((flagset & 1)==1) return true;
		return false;
	}
	public static boolean isBlastRush(byte blastType)
	{
		if (blastType==4) return true;
		if (blastType==6) return true;
		return false;
	}
	public static byte getBlastType(RandomAccessFile param, int blastID) throws IOException
	{
		short pos;
		short[] posValues = {318,319,320};
		if (blastID>5 || blastID<3) return 0;
		pos = posValues[blastID-3];
		
		param.seek(pos);
		return param.readByte();
	}
	public static float getBlastSpeed(RandomAccessFile param, int blastID) throws IOException
	{
		short pos;
		short[] posValues = {508,512,516};
		if (blastID<=0 || blastID>posValues.length) return 0;
		
		pos=posValues[blastID-1];
		if (!isBlastRush(getBlastType(param,blastID))) pos-=424;
		param.seek(pos);
		return LittleEndian.getFloat(param.readFloat());
	}
	public static float getBlastDuration(RandomAccessFile param, int blastID) throws IOException
	{
		short pos;
		short[] posValues = {36,40,44};
		if (blastID>5 || blastID<3) return 0;
		pos=posValues[blastID-1];
		
		param.seek(pos);
		return LittleEndian.getFloat(param.readFloat());
	}
	public static int getBlastDamage(RandomAccessFile param, int blastID) throws IOException
	{
		short pos;
		//order of values in array: 1st B1, 2nd B1, 1st B2, 2nd B2, Ultimate
		short[] posValues = {140, 144, 400, 404, 408}; //array stores positions of blocked B2 damages
		double coefficient=1;
		if (blastID<=0 || blastID>posValues.length) return 0;
		
		pos=posValues[blastID-1];
		if (!blastDamageFlags[0])
		{
			if (blastID>2) pos-=12;
			else pos-=8;
		}
		posValues = null; //garbage collector, do your thing
		param.seek(pos);
		int rawDmg = LittleEndian.getInt(param.readInt());
		int modDmg;
		
		short[] posValuesForHits = {64, 66, 156, 157, 158};
		pos=posValuesForHits[blastID-1];
		param.seek(pos);
		
		byte hits = param.readByte();
		hits++; //prevents division by zero, plus a Blast must always hit
		//return damage of single hit for Blast 1's (number of hits varies in gameplay)
		if (blastID<=2) return getRoundedValue((int) (rawDmg/hits),10);
		if (blastDamageFlags[1] && blastID>2) coefficient*=1.1;
		if (blastID>2 && blastID<5)
		{
			if (blastDamageFlags[2]) coefficient*=1.2;
			if (blastDamageFlags[3]) coefficient=0.3;
		}
		
		if (hits==1) return getRoundedValue((int) (rawDmg*coefficient),10);
		else
		{
			modDmg = (int) (getRoundedValue(rawDmg,hits*10)*coefficient);
			return getRoundedValue(modDmg,hits*10);
		}
	}
	public static int getRoundedValue(int source, int divisor)
	{
		if (divisor==0) return 0;
		int remainder = source%divisor;
		if (remainder!=0) source += (divisor-remainder);
		return source; //source is rounded to the next multiple of divisor
	}
	public static short getBlastMagicValue(RandomAccessFile param, int blastID) throws IOException
	{
		short pos;
		short[] posValues = {16,18,24,26,28};
		if (blastID<=0 || blastID>posValues.length) return 0;
		pos=posValues[blastID-1];
		
		param.seek(pos);
		return LittleEndian.getShort(param.readShort());
	}
	public static short getBlast2ClashAdv(RandomAccessFile param, int blastID) throws IOException
	{
		short pos;
		short[] posValues = {30,32,34};
		if (blastID<=0 || blastID>posValues.length) return 0;
		pos=posValues[blastID-1];
		
		param.seek(pos);
		return LittleEndian.getShort(param.readShort());
	}
	public static String exportCSV(File[] paths, RandomAccessFile[] paramsB1, RandomAccessFile[] paramsB2) throws IOException
	{
		String output="";
		File outputCsv = new File("out.csv");
		FileWriter outputWriter = new FileWriter(outputCsv);
		
		int damage=0;
		System.out.println("[out.csv Contents]");
		for (int i=0; i<paths.length; i++)
		{
			list = paths[i];
			for (int blastID=1; blastID<=5; blastID++)
			{	
				if (blastID>2) damage=getBlastDamage(paramsB2[i],blastID);
				else damage=getBlastDamage(paramsB1[i],blastID);
				setBlastNames(blastID);
				output+=charaNames[i]+","+blastNames[blastID-1]+","+damage+"\n";
			}
		}
		outputWriter.write(output);
		outputWriter.close();
		return output;
	}
	public static void setBlastNames(int blastID) throws IOException
	{
		Scanner sc = new Scanner(list, "UTF-16");
		boolean hasContinue=false;
		int blastIndex = blastID-1;
		while (sc.hasNextLine())
		{
			String line = sc.nextLine();
			if (line.startsWith("!F*"))
			{
				char[] lineArray = line.toCharArray();
				if (blastID==5 && lineArray[3]=='1') 
				{
					blastNames[blastIndex] = line.substring(6); break;
				}
				if (lineArray[3]=='0' && lineArray[4]!='0')
				{
					if (blastID==3) 
					{
						if (hasContinue) blastIndex=blastID;
						blastNames[blastIndex] = line.substring(6); break;
					}
					if (blastID==4) {blastID=3; hasContinue=true; continue;}
				}
				if (lineArray[3]=='0' && lineArray[4]=='0')
				{
					if (blastID==1)
					{
						if (hasContinue) blastIndex=blastID;
						blastNames[blastIndex] = line.substring(6); break;
					}
					if (blastID==2) {blastID=1; hasContinue=true; continue;}
				}
			}
		}
		sc.close();
	}
	public static void setCharaNames(File names) throws IOException
	{
		Scanner sc = new Scanner(names);
		int rowCnt=0;
		while (sc.hasNextLine())
		{
			String name = sc.nextLine();
			charaNames[rowCnt]=name;
			rowCnt++;
		}
		sc.close();
	}
	public static void main(String[] args) throws IOException 
	{
		double start = System.currentTimeMillis();
		File names = new File("chara-names.txt");
		setCharaNames(names);
		File folder = new File(BLAST_2_PATH);
		File[] paths = folder.listFiles();
		
		int paramCnt = paths.length;
		int paramIndex=0;
		RandomAccessFile[] paramsB2 = new RandomAccessFile[paramCnt];
		
		for (File file: paths)
		{
			paramsB2[paramIndex] = new RandomAccessFile(file.getAbsolutePath(), "r");
			paramIndex++;
		}
		
		folder = new File(BLAST_1_PATH);
		paths = folder.listFiles();
		RandomAccessFile[] paramsB1 = new RandomAccessFile[paramCnt];
		paramIndex=0;
		
		for (File file: paths)
		{
			paramsB1[paramIndex] = new RandomAccessFile(file.getAbsolutePath(), "r");
			paramIndex++;
		}
		
		folder = new File(SKL_LST_PATH);
		paths = folder.listFiles();
		
		System.out.println("Export and display a CSV file showing the damage of all Blasts in Budokai Tenkaichi 3.\n"
		+ "The program has other methods, but as a proof of concept, only these damage-related arguments can be used:\n"
		+ "-blk, -block -----> Show blocked Blast damage only\n" + "-bst, -boost -----> Show boosted Blast 2 damage (10% more)\n"
		+ "-mpm, -sparking --> Show boosted damage (20% more) when the Blast 2 is used in MPM/Sparking (MAX Power Mode)\n"
		+ "-cmb, -combo -----> Show damage when the Blast 2 is (70% less)\n");
		if (args.length>0) //check for command line arguments
		{
			for (int i=0; i<args.length; i++)
			{
				if (args[i].equals("-block") || args[i].equals("-blk")) blastDamageFlags[0]=true;
				if (args[i].equals("-boost") || args[i].equals("-bst")) blastDamageFlags[1]=true;
				if (args[i].equals("-sparking") || args[i].equals("-mpm")) blastDamageFlags[2]=true;
				if (args[i].equals("-combo") || args[i].equals("-cmb")) blastDamageFlags[3]=true;
			}
		}
		System.out.println(exportCSV(paths, paramsB1, paramsB2));
		
		double end = System.currentTimeMillis();
		System.out.println("Time: "+(end-start)/1000+" s");
	}
}