package Security;
import java.awt.Point;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;


public class GenerateSessionKey {
	/*
	public static void main(String args[]){
		GenerateSessionKey te = new GenerateSessionKey();
		System.out.println(te.generete_1_AuthenticationKyes());
		
	}	
	*/
	public String generete_1_SessionKey(){
		String sessionKey="";
		sessionKey=getSessionKey(getCurrentGMTTime());
		
		return sessionKey;
	}
	
	
	
	public boolean isSessionKeyValid(String sessionKey){
		String[] sessionKeys=generete_5_AuthenticationKyes();
		for(int i=0;i<sessionKeys.length;i++){
			if(sessionKey.equals(sessionKeys[i])){
				return true;
			}
		}
		
		return false;
	}
	
	private String getKeyCreator(int year,int month,int day,int h,int min){
		int months[] = {785003212,502005134,605289156,294567178,105609590,807657112,698517834,356789156,4270856,950732178,507856790,570875612};
		int dayNbr[]={6093951,3921921,1826950,1921032,6916901,9311120,8010941,1080900,1006012,3642106,6001001,5013210,1050132,6119110,9113210,3216939,1234041,4391029,4219989,6993003,5996600,5339996,7016932,1073201,7361234,3976987,4953456,6929867,2188900,2990019,9999015};
		int hours[] = {983450,962871,933301,803201,865842,899102,776213,704940,738012,605695,691301,638001,501100,594483,562501,421209,469336,473602,304881,336905,379602,248342,212900,261696};
		int minutes[] = {358,254,955,881,462,851,641,915,325,885,441,322,782,157,715,997,801,741,355,301,235,199,505,631,932,518,759,328,125,118,139,161,511,722,314,236,175,281,992,100,220,520,339,601,739,823,911,356,192,789,810,667,512,290,316,226,109,201,698,702};
		int calc1=0,calc2=0,calc3=0;
		try{
			calc1=((months[month]/((dayNbr[day]-hours[h])/year))/(minutes[min]/2))+(hours[h]*hours[h])/(minutes[min]/2);
			calc2=(dayNbr[day]/year)+year+((months[month]+hours[h])/(minutes[min]/2));
			if(calc1<0){
				calc1=-1*(calc1);
			}
			if(calc2<0){
				calc2=-1*(calc2);
			}
			if(calc1>calc2){
				calc3=((calc1+calc2)*2)/(minutes[min]/6);
			}
			if(calc1<calc2){
				calc3=((calc2+calc1)*2)/(minutes[min]/7);
			}
			if(calc3<0){
				calc3=-1*(calc3);
			}
		}catch(Exception ex){
			System.out.println("\n\n\n\n\n############################################################################################################################################################");
			System.out.println("ERROR has accured in:  -- getKeyCreator() -- ");
			System.out.println("############################################################################################################################################################");
			System.out.println("calc1=((months["+month+"]/((dayNbr["+day+"]-hours["+h+"])/year))/minutes["+min+"])+(hours["+h+"]*hours["+h+"])/(minutes["+min+"]/2);");
			System.out.println("calc2=(dayNbr["+day+"]/year)+year+((months["+month+"]+hours["+h+"])/(minutes[min]/2));");ex.printStackTrace();
			System.out.println("(months["+month+"]  : months.length == "+months.length);
			System.out.println("dayNbr["+day+"] : dayNbr.length == "+dayNbr.length);
			System.out.println("hours["+h+"] : hours.length == "+hours.length);
			System.out.println("minutes["+min+"] : minutes.length"+minutes.length);
			if(calc1>calc2){
				System.out.println("calc3=(calc1-calc2)/minutes["+min+"];");
			}else{
				System.out.println("calc3=(calc2-calc1)/minutes["+min+"];");
			}
			System.out.println("############################################################################################################################################################\n\n\n\n\n");
		}
		return String.valueOf(calc3);
	}
	
	private ArrayList getSecretMultipleKeys(String keyCreator){
		String alphabetCode[] = {"g85zh21","i38gz93","v18jy02","fn09zk8","15xw9ls","l8ty74u","p1oq72l","w61ox2s","89mg0iz","x3ah7g9","amq91z2","y31eh8w","c34hf9x","q68yb4f","p4b38dv","a8f2rd0","e19nk48","hwx54if","d2yge36","j82yc16","49z3h28","b0en41j","mq41zr7","fk82z0a","55q19ld","vq39oi1"};
		int increment=2;
		int i=0;
		ArrayList keys = new ArrayList();try{
			for(i=0;i<keyCreator.length();i=i+increment){   
				if(i==keyCreator.length()-2){
					increment=1;
				}
				if(i<keyCreator.length()-2 &&(Character.getNumericValue(keyCreator.charAt(i))+Character.getNumericValue(keyCreator.charAt(i+1)))<26){
					keys.add(alphabetCode[Character.getNumericValue(keyCreator.charAt(i))]);
					keys.add(alphabetCode[Character.getNumericValue(keyCreator.charAt(i+1))]);
					keys.add(alphabetCode[(Character.getNumericValue(keyCreator.charAt(i))+Character.getNumericValue(keyCreator.charAt(i+1)))]);
				}else if(i<keyCreator.length()-2 &&(Character.getNumericValue(keyCreator.charAt(i))+Character.getNumericValue(keyCreator.charAt(i+1)))>25){
					keys.add(alphabetCode[Character.getNumericValue(keyCreator.charAt(i))]);
					keys.add(alphabetCode[Character.getNumericValue(keyCreator.charAt(i+1))]);
				}else if(i<26 && i<keyCreator.length()-1){
					keys.add(alphabetCode[Character.getNumericValue(keyCreator.charAt(i))]);
				}
			}
		}catch(Exception ex){
			System.out.println("\n\n\n\n\n############################################################################################################################################################");
			System.out.println("ERROR has accured in:  -- getSecretMultipleKeys -- ");
			System.out.println("############################################################################################################################################################\n\n\n\n\n");
			ex.printStackTrace();
		}
		return keys;
	}
	
	private String getMixedMultiplesKeys(ArrayList keys){
		char[] authenticationKey=null;
		int p=0,q=0;
		int x=0,size=0;;
		try{
			for(int z=0;z<keys.size();z++){
				size=size+keys.get(z).toString().length();
			}
			authenticationKey= new char[size];
			for(p=0;p<keys.get(0).toString().length();p++){
				for(q=0;q<keys.size() && x<authenticationKey.length;q++){
					authenticationKey[x]=keys.get(q).toString().charAt(p);
					x++;
				}
			}
		}catch(Exception ex){
			System.out.println("\n\n\n\n\n############################################################################################################################################################");
			System.out.println("ERROR has accured in:  -- getMixedMultiplesKeys -- ");
			System.out.println("############################################################################################################################################################");
			
			System.out.println("authenticationKey["+x+"]=keys.get("+q+").toString().charAt("+p+")\n");
			System.out.println("authenticationKey["+x+"]   -- authenticationKey.length == "+authenticationKey.length);
			System.out.println("keys.get("+q+")  --  keys.size() == "+keys.size());
			System.out.println("keys.get("+q+").toString().charAt("+p+") --   keys.get("+q+").toString().length()"/*+keys.get(q).toString().length()*/);
			System.out.println("############################################################################################################################################################\n\n\n\n\n");
			ex.printStackTrace();
		}
		return new String(authenticationKey);
	}
	

	
	
	
	
	private int[] getCurrentGMTTime(){
		int[] time=new int[5];
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:");
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
		String tiime=dateFormat.format(cal.getTime());
		int timeIndex=0,lastSeparator=-1;
		for(int i=0;i<tiime.length() && timeIndex<time.length ;i++){
			if(tiime.charAt(i)==':'){
				time[timeIndex]=Integer.parseInt(tiime.substring((lastSeparator+1), i));
				lastSeparator=i;
				timeIndex++;
			}
		}
		return time;
	}
	
	private int[] getGMTGeneratedTTimes(){
		int[] time=new int[5];
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:");
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
		String tiime=dateFormat.format(cal.getTime());
		System.out.println(tiime);
		int timeIndex=0,lastSeparator=-1;
		for(int i=0;i<tiime.length() && timeIndex<time.length ;i++){
			if(tiime.charAt(i)==':'){
				time[timeIndex]=Integer.parseInt(tiime.substring((lastSeparator+1), i));
				lastSeparator=i;
				timeIndex++;
			}
		}
		return  time;
	}
	
	private int[] addGMTTime(int timeIntervall){
		int[] time=new int[5];
		DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd:HH:mm:");
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
		cal.add(Calendar.MINUTE, timeIntervall);
		String tiime=dateFormat.format(cal.getTime());
		int timeIndex=0,lastSeparator=-1;
		for(int i=0;i<tiime.length() && timeIndex<time.length ;i++){
			if(tiime.charAt(i)==':'){
				time[timeIndex]=Integer.parseInt(tiime.substring((lastSeparator+1), i));
				lastSeparator=i;
				timeIndex++;
			}
		}
		return  time;
	}
	
	private String getSessionKey(int[] yMDhm){
		String keyCreator=getKeyCreator(yMDhm[0],yMDhm[1],yMDhm[2],yMDhm[3],yMDhm[4]);
		ArrayList keys=getSecretMultipleKeys(keyCreator);
		String authenticationKey=getMixedMultiplesKeys(keys);
		return authenticationKey;
	}

	private String[] generete_5_AuthenticationKyes(){
		int timeIntervall=-1;
		String[] sessionKyes= new String[5];
		for(int i=0;i<5;i++){
			sessionKyes[i]=getSessionKey(addGMTTime(timeIntervall));
			timeIntervall++;
		}
		return sessionKyes;
	}
}
