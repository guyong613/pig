package pig;

import java.util.ArrayList;
import java.util.Collections;

public class FPUtil {
	public static int pigfirst=0;//首个得猪的	
	public static String[] newGame(){
		ArrayList<Integer> cards = new ArrayList<Integer>();
		for(int i=0;i<52;i++){
			cards.add(i);
		}
	    //去掉红桃2.3
		//cards.remove(52);
		//cards.remove(53);		
		Collections.shuffle(cards);
		String[] result = new String[]{
			"<#START#>","<#START#>","<#START#>","<#START#>"
		};
		
		for(int i=0;i<52;i++){
			//int k = i%3;
			//int c = i%3;
			int k = i%4;
			int c = i%4;
			result[k] = result[k]+cards.get(i)+",";
			//GY
			if (cards.get(i)==9)//黑桃J先出 
					{
				    pigfirst=k;
					}
		}
		//for(int i=0;i<3;i++)
		for(int i=0;i<4;i++)	
		{
			result[i]=result[i].substring(0,result[i].length()-1);
		}
		
		return result;
	}
	
	public static void main(String[] args){
		
	}	
}
