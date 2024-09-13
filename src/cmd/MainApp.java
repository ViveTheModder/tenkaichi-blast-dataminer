package cmd;
//Tenkaichi Blast Dataminer v1.1 by ViveTheModder
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class MainApp 
{
	//flags are in this order: isBlocked, isBoosted, duringMPM, duringBlastCombo
	static File charaInfo = new File("./csv/chara-health.csv");
	static File list; //currently loaded skill list
	static final int CHARA_COUNT=161;
	static int charaIndex=0;
	static int targetID=-1;
	static int[] charaHealthBars = new int[CHARA_COUNT];
	static String currArg=""; //current argument, which gets passed to the output CSV's file name
	static String target="";  //target refers to the last column of the CSV
	static String[] charaNames = new String[CHARA_COUNT];
	static final String BLAST_1_PATH = "./blast1-param/";
	static final String BLAST_2_PATH = "./blast2-param/";
	static final String SKL_LST_PATH = "./skill-lists/";
	static final String[] blastArgs1 = {"-b1cost","-b1hp","-b1ki","-b1span","-b1stats"};
	static final String[] blastArgs2 = 
	{"-b2clash","-b2cost","-b2cuts","-b2fall","-b2type","-b2giants","-b2struggle","-b2damageopp","-b2damageusr",
	"-b2planetdes","-b2fadeopp","-b2gothrough","-b2positair","-b2positmap","-b2stepback","-b2fallafter","-b2kamehame"};
	
	public static String exportCSV(File[] paths, RandomAccessFile[] paramsB1, RandomAccessFile[] paramsB2) throws IOException
	{
		String output="";
		File outputCsv = new File("out"+currArg+".csv");
		FileWriter outputWriter = new FileWriter(outputCsv);
		boolean isTargetB1 = (targetID/100)==1;
		boolean isTargetB2 = (targetID/200)==1;
		
		while (charaIndex<CHARA_COUNT)
		{
			list = paths[charaIndex];
			for (int blastID=1; blastID<=5; blastID++)
			{	
				if (isTargetB1 && blastID==3) break;
				if (isTargetB2 && blastID<3) continue;
				if (blastID>2) 
				{
					Blast.param = paramsB2[charaIndex];
					setTarget(targetID,blastID);
				}
				else
				{
					Blast.param = paramsB1[charaIndex];
					setTarget(targetID,blastID);
				}
				Blast.setBlastNames(blastID);
				if (target.equals("false") || target.equals("0,0,0,0")) continue;
				output+=charaNames[charaIndex]+","+Blast.blastNames[blastID-1]+","+target+"\n";
			}
			charaIndex++;
		}
		System.out.println("\n[out"+currArg+".csv Contents]");
		outputWriter.write(output);
		outputWriter.close();
		return output;
	}
	public static void setTarget(int targetID, int blastID) throws IOException
	{
		target="";
		switch(targetID)
		{
			//general Blast methods
			case 000: target+=Blast.getBlastDamage(blastID); break;
			case 001: target+=Blast.getBlastDuration(blastID); break;
			case 002: target+=Blast.getBlastMagicValue(blastID); break;
			case 003: target+=Blast.getBlastSpeed(blastID); break;
			case 004: target+=Blast.isUnblockable(blastID); break;
			//Blast 1 methods
			case 100: target+=Blast.getBlast1Cost(blastID); break;
			case 101: target+=Blast.getBlast1HealthGain(blastID, charaIndex); break;
			case 102: target+=Blast.getBlast1KiGain(blastID); break;
			case 103: target+=Blast.getBlast1LifespanType(blastID); break;
			case 104: target+=Blast.getBlast1Stats(blastID); break;
			//Blast 2 methods
			case 200: target+=Blast.getBlast2ClashAdv(blastID); break;
			case 201: target+=Blast.getBlast2Cost(blastID); break;
			case 202: target+=Blast.getBlast2CutsceneCnt(blastID); break;
			case 203: target+=Blast.getBlast2FallType(blastID); break;
			case 204: target+=Blast.blastTypes[Blast.getBlast2Type(blastID)]; break;
			case 205: target+=Blast.affectsGiants(blastID); break;
			case 206: target+=Blast.canBeamStruggle(blastID); break;
			case 207: target+=Blast.canDamageOpponent(blastID); break;
			case 208: target+=Blast.canDamageUser(blastID); break;
			case 209: target+=Blast.canDestroyPlanet(blastID); break;
			case 210: target+=Blast.canFadeOpponentAfterBlast2(blastID); break;
			case 211: target+=Blast.canGoThroughOpponent(blastID); break;
			case 212: target+=Blast.canSetPositionToAir(blastID); break;
			case 213: target+=Blast.canSetPositionToMapCenter(blastID); break;
			case 214: target+=Blast.canStepOutBeforeBlast2(blastID); break;
			case 215: target+=Blast.canUserFallAfterBlast2(blastID); break;
			case 216: target+=Blast.isBlast2Kamehameha(blastID); break;
			default: break;
		}
	}
	public static void setCharaHealthBars(File charaInfo) throws IOException
	{
		Scanner sc = new Scanner(charaInfo);
		sc.useDelimiter(",");
		int rowCnt=0;
		while (sc.hasNextLine())
		{
			sc.next(); //skip character name
			charaHealthBars[rowCnt]=Integer.parseInt(sc.nextLine());
			rowCnt++;
		}
		sc.close();
	}
	public static void setCharaNames(File charaInfo) throws IOException
	{
		Scanner sc = new Scanner(charaInfo);
		sc.useDelimiter(",");
		int rowCnt=0;
		while (sc.hasNextLine())
		{
			String name = sc.next();
			sc.nextLine(); //skip character health
			charaNames[rowCnt]=name;
			rowCnt++;
		}
		sc.close();
	}
	public static void main(String[] args) throws IOException 
	{
		double start = System.currentTimeMillis();
		setCharaNames(charaInfo);
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
		
		String helpText = "Export and display a CSV file showing the damage of all Blasts in Budokai Tenkaichi 3.\n"
		+ "Here is a list of all the arguments that can be used. Use -h or -help to print this out again.\n\n"
		+ "[General Blast Arguments]\n" + "* -blid, -magic --> Display the magic value (or Blast Identifier) of every Blast.\n"
		+ "* -dmg, -damage --> Display the damage of every Blast. Additional arguments can be used in any order, such as:\n"
		+ "** blk, block ----> Show blocked Blast damage only\n" + "** bst, boost ----> Show boosted Blast 2 damage (10% more).\n"
		+ "** mpm, spark ----> Show boosted damage (20% more) when the Blast 2 is used in MPM/Sparking (MAX Power Mode).\n"
		+ "** cmb, combo ----> Show damage (70% less) when the Blast 2 is used during a Blast Combo. \n"
		+ "* -dur, -length --> Display the duration/length of every Blast.\n"
		+ "* -spd, -speed ---> Display the speed of every Blast.\n" 
		+ "* -ub, -unblock --> Display every Blast that is unblockable.\n" + "[Blast 1 Arguments]\n"
		+ "* -b1cost --------> Display the number of Blast Stocks consumed upon usage of every Blast 1.\n"
		+ "* -b1hp ----------> Display the number of Health Bars gained upon usage of every Blast 1.\n"
		+ "* -b1ki ----------> Display the number of Ki Bars gained upon usage of every Blast 1.\n"
		+ "* -b1span --------> Display how long (in seconds) the effects of every Blast 1 last.\n"
		+ "* -b1stats -------> Display the stats (Ability Modifiers) of every Blast 1.\n" + "[Blast 2 Arguments]\n"
		+ "* -b2clash -------> Display the Clash Advantage of every Blast 2.\n"
		+ "* -b2cost --------> Display the number of Ki Bars consumed upon usage of every Blast 2.\n"
		+ "* -b2cuts --------> Display the number of cutscene animations for every Blast 2.\n"
		+ "* -b2fall --------> Display the fall type identifier of every Blast 2.\n"
		+ "* -b2type --------> Display every Blast 2's type.\n"
		+ "* -b2giants ------> Display every Blast 2 that affects giant characters.\n"
		+ "* -b2struggle ----> Display every Blast 2 that can perform a Beam Clash.\n"
		+ "* -b2damageopp ---> Display every Blast 2 that can damage the opponent's costume.\n"
		+ "* -b2damageusr ---> Display every Blast 2 that can damage the player's costume.\n"
		+ "* -b2planetdes ---> Display every Blast 2 that can blow up the planet.\n"
		+ "* -b2fallafter ---> Display every Blast 2 that makes the opponent fall after the Blast 2's execution.\n"
		+ "* -b2fadeopp -----> Display every Blast 2 that makes the opponent fade if the Blast 2 has killed them.\n"
		+ "* -b2gothrough ---> Display every Blast 2 that goes through the opponent.\n"
		+ "* -b2kamehame ----> Display every Blast 2 that is affected by the Fierce God Z-Item (only Kamehamehas).\n"
		+ "* -b2positair ----> Display every Blast 2 that sets the opponent's position in the air.\n"
		+ "* -b2positmap ----> Display every Blast 2 that sets the player's position to the map center.\n"
		+ "* -b2stepback ----> Display every Blast 2 that makes the user step back before performing it.";
		System.out.println(helpText);
		
		if (args.length==0) //check for command line arguments
		{
			System.out.println("An argument must be provided from the list. Use -h for help."); System.exit(1);
		}
		for (int i=0; i<args.length; i++)
		{
			if (args[0].equals("-dmg"))
			{
				targetID=0;
				if (args.length==1) break;
				for (int j=1; j<args.length; j++) //damage arguments can be put in any order
				{
					if (args[j].equals("block") || args[i].equals("blk")) Blast.blastDamageFlags[0]=true;
					if (args[j].equals("boost") || args[i].equals("bst")) Blast.blastDamageFlags[1]=true;
					if (args[j].equals("spark") || args[i].equals("mpm")) Blast.blastDamageFlags[2]=true;
					if (args[j].equals("combo") || args[i].equals("cmb")) Blast.blastDamageFlags[3]=true;
				}
			}
		}
		
		switch(args[0]) //only used for general cases
		{
			case "-blid":
			case "-magic": targetID=2; break;
			case "-dur":
			case "-length": targetID=1; break;
			case "-spd":
			case "-speed": targetID=3; break;
			case "-ub":
			case "-unblock": targetID=4; break;
			default: targetID=-1; break;
		}
		//only used for Blast 1 and Blast 2 cases
		for (int i=0; i<blastArgs2.length; i++)
		{
			if (i<blastArgs1.length)
			{
				if (args[0].equals(blastArgs1[i])) targetID=100+i;
				if (args[0].equals(blastArgs2[i])) targetID=200+i;
			}
			else
			{
				if (args[0].equals(blastArgs2[i])) targetID=200+i;
			}
		}
		if (targetID==-1)
		{
			System.out.println("Invalid argument. Use -h for help."); System.exit(2);
		}
		
		currArg = args[0];
		System.out.println(exportCSV(paths, paramsB1, paramsB2));
		double end = System.currentTimeMillis();
		System.out.println("Time: "+(end-start)/1000+" s");
	}
}