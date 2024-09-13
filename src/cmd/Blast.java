package cmd;
//Tenkaichi Blast class by ViveTheModder
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Blast extends MainApp
{
	static boolean[] blastDamageFlags = new boolean[4];
	static RandomAccessFile param; //currently loaded blast 1/2 param
	static String[] blastNames = new String[5]; 
	static final String[] blastTypes = 
	{"Normal","Target Lock","Chargeable","Tri-Beam","Linear Rush","Unused","Target Rush","Cutscene Blast","Assault","Teleport Blast","Grab"};
	static final String[] lifespanTypes = {"Permanent","Timed","Until a Blast is used","Until MPM runs out","Until Kaio-ken runs out"};

	public static boolean affectsGiants(int blastID) throws IOException
	{
		short pos;
		short[] posValues={15,19,23};
		if (blastID>5 || blastID<3) return false;
		pos=posValues[blastID-3];
		
		return hasFlag(pos,8);
	}
	public static boolean canBeamStruggle(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {1,5,9};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return !hasFlag(pos,1);
	}
	public static boolean canDamageOpponent(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {14,18,22};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return hasFlag(pos,8);
	}
	public static boolean canDamageUser(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {13,17,21};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return hasFlag(pos,0x20);
	}
	public static boolean canDestroyPlanet(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {2,6,10};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return hasFlag(pos,0x20);
	}
	public static boolean canFadeOpponentAfterBlast2(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {3,7,11};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return hasFlag(pos,2);
	}
	public static boolean canGoThroughOpponent(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {0,4,8};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return hasFlag(pos,0x20);
	}
	public static boolean canSetPositionToAir(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {15,19,23};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return hasFlag(pos,0x10);
	}
	public static boolean canSetPositionToMapCenter(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {13,17,21};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return hasFlag(pos,8);
	}
	public static boolean canStepOutBeforeBlast2(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {13,17,21};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return hasFlag(pos,2);
	}
	public static boolean canUserFallAfterBlast2(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {13,17,21};
		if (blastID>5 || blastID<3) return false;
		pos = posValues[blastID-3];
		
		return hasFlag(pos,0x40);
	}
	public static boolean isUnblockable(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {10,14,12,16,20};
		if (blastID<=0 || blastID>posValues.length) return false;
		pos = posValues[blastID-1];
		
		if (blastID>2) return hasFlag(pos,1);
		return hasFlag(pos,0x20);
	}
	public static boolean hasFlag(short pos, int flag) throws IOException
	{
		param.seek(pos);
		byte flagset = param.readByte();
		if ((flagset & flag)==flag) return true;
		return false;
	}
	public static boolean isBlast1Stackable(int blastID) throws IOException
	{
		short pos;
		if (blastID>2) return false;
		else if (blastID==2) pos=13;
		else if (blastID==1) pos=9;
		else return false;
		
		return !hasFlag(pos,1);
	}
	public static boolean isBlast2Kamehameha(int blastID) throws IOException
	{
		short pos;
		short[] posValues={12,16,20};
		if (blastID>5 || blastID<3) return false;
		pos=posValues[blastID-3];
		
		return hasFlag(pos,0x10);
	}
	public static boolean isBlastRush(byte blastType)
	{
		if (blastType==4 || blastType==6) return true;
		return false;
	}
	public static byte getBlast1Cost(int blastID) throws IOException
	{
		short pos;
		if (blastID>2 || blastID<0) return 0;
		pos=148;
		if (blastID==2) pos++;
		
		param.seek(pos);
		return param.readByte();
	}
	public static byte getBlast2FallType(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {333,334,335};
		if (blastID>5 || blastID<3) return 0;
		pos = posValues[blastID-3];
		
		param.seek(pos);
		return param.readByte();
	}
	public static byte getBlast2Type(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {318,319,320};
		if (blastID>5 || blastID<3) return -1;
		pos = posValues[blastID-3];
		
		param.seek(pos);
		return param.readByte();
	}
	public static byte getBlast2CutsceneCnt(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {345,346,347};
		if (blastID>5 || blastID<3) return 0;
		pos = posValues[blastID-3];
		
		param.seek(pos);
		return param.readByte();
	}
	public static float getBlastSpeed(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {32,34,508,512,516};
		if (blastID<=0 || blastID>posValues.length) return 0;
		pos=posValues[blastID-1];
		
		if (blastID>2 && !isBlastRush(getBlast2Type(blastID))) pos-=424;
		param.seek(pos);
		return LittleEndian.getFloat(param.readFloat());
	}
	public static float getBlastDuration(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {160,164,36,40,44};
		if (blastID<=0 || blastID>posValues.length) return 0;
		pos=posValues[blastID-1];
		
		param.seek(pos);
		return LittleEndian.getFloat(param.readFloat());
	}
	public static int getBlastDamage(int blastID) throws IOException
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
	public static int getBlast1HealthGain(int blastID, int charaIndex) throws IOException
	{
		if (charaHealthBars!=null) setCharaHealthBars(charaInfo);
		short pos;
		if (blastID>2 || blastID<0) return 0;
		pos=168;
		if (blastID==2) pos+=4;
		
		param.seek(pos);
		int percentage = LittleEndian.getInt(param.readInt());
		return (charaHealthBars[charaIndex]*percentage)/100;
	}
	public static int getBlast1KiGain(int blastID) throws IOException
	{
		short pos;
		if (blastID>2 || blastID<0) return 0;
		pos=176;
		if (blastID==2) pos+=4;
		
		param.seek(pos);
		return LittleEndian.getInt(param.readInt())/20000; //20000 units is 1 ki bar
	}
	public static int getBlast2Cost(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {412,416,420};
		if (blastID>5 || blastID<3) return 0;
		pos=posValues[blastID-3];
		
		param.seek(pos);
		return LittleEndian.getInt(param.readInt())/20000; //cost is returned in ki bars
	}
	public static int getRoundedValue(int source, int divisor)
	{
		if (divisor==0) return 0;
		int remainder = source%divisor;
		if (remainder!=0) source += (divisor-remainder);
		return source; //source is rounded to the next multiple of divisor
	}
	public static short getBlastMagicValue(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {16,18,24,26,28};
		if (blastID<=0 || blastID>posValues.length) return 0;
		pos=posValues[blastID-1];
		
		param.seek(pos);
		return LittleEndian.getShort(param.readShort());
	}
	public static short getBlast2ClashAdv(int blastID) throws IOException
	{
		short pos;
		short[] posValues = {30,32,34};
		if (blastID<=0 || blastID>posValues.length) return 0;
		pos=posValues[blastID-1];
		
		param.seek(pos);
		return LittleEndian.getShort(param.readShort());
	}
	public static String getBlast1LifespanType(int blastID) throws IOException
	{
		short pos;
		if (blastID>2) return null;
		else if (blastID==2) pos=103;
		else if (blastID==1) pos=102;
		else return null;
		
		param.seek(pos);
		return lifespanTypes[param.readByte()];
	}
	public static String getBlast1Stats(int blastID) throws IOException
	{
		short pos=0; String stats=null;
		//stats are ordered like so: Attack, Ki, Defense, Super 
		short[] posValues = {150,152,154,156};
		if (blastID>2 || blastID<0) return null;
		
		for (int i=0; i<posValues.length; i++)
		{
			pos=posValues[i];
			if (blastID==2) pos++;
			param.seek(pos);
			stats+=param.readByte();
			if (i!=(posValues.length-1)) stats+=",";
		}
		return stats;
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
}
