package pig;
import static pig.Server.currPlayer;
import static pig.Server.player1;
import static pig.Server.player2;
import static pig.Server.player3;
import static pig.Server.player4;

import java.io.*;
import java.net.*;
public class Server
{
	static int count=0;//玩家数量
	static ServerAgent player1;
	static ServerAgent player2;
	static ServerAgent player3;
	static ServerAgent player4;
	static ServerAgent currPlayer;//当前玩家
	static String[] scoresMsg;
	static int scoresIndex;
	
	public static void main(String args[]) throws Exception
	{
		ServerSocket ss=new ServerSocket(9998);
		System.out.println("Listening on 9998...");
		//gy Thread.sleep(3000);
		while(true)
		{
			Socket sc=ss.accept();
			DataInputStream din=new DataInputStream(sc.getInputStream());
			DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
			System.out.println("<#JOIN PLAYER IS #>---------"+count);
			if(count==0)
			{//进来的是第一个人
				//scoresMsg=new String[3];
				scoresMsg=new String[4];
				scoresIndex=0;
			    //System.out.println("<#ACCEPT#>1");
				dout.writeUTF("<#ACCEPT#>1");
				player1=new ServerAgent(sc,din,dout);
				player1.start();
				count++;
				Thread.sleep(3000);
				
			}
			else if(count==1)
			{//进来的是第二个人
			    //System.out.println("<#ACCEPT#>2");
				dout.writeUTF("<#ACCEPT#>2");
				player2=new ServerAgent(sc,din,dout);
				player2.start();
				count++;
				Thread.sleep(3000);
			}
			else if(count==2)
			{//进来的是第二个人
			    //System.out.println("<#ACCEPT#>3");
				dout.writeUTF("<#ACCEPT#>3");
				player3=new ServerAgent(sc,din,dout);
				player3.start();
				count++;
				Thread.sleep(3000);
			}
			else if(count==3)
			{//进来的是第三个人
			    //System.out.println("<#ACCEPT#>4");
				dout.writeUTF("<#ACCEPT#>4");	
				player4=new ServerAgent(sc,din,dout);
				player4.start();			
				count++;
				Thread.sleep(3000);
			
				
				String[] cards=FPUtil.newGame();
				
				//三家发牌，切屏到游戏界面
				player1.dout.writeUTF(cards[0]);
				player2.dout.writeUTF(cards[1]);
				player3.dout.writeUTF(cards[2]);
				player4.dout.writeUTF(cards[3]);
				//给玩家1牌权
				//player1.dout.writeUTF("<#YOU#>");
				//currPlayer=player1;
				switch(FPUtil.pigfirst)
				{
				case 0: currPlayer=player1;break;
				case 1: currPlayer=player2;break;
				case 2: currPlayer=player3;break;
				case 3: currPlayer=player4;break;
				default:currPlayer=player1;
				}
				
				currPlayer.dout.writeUTF("<#YOU#>");
				
				//给三个玩家发得分
			/*	while(scoresIndex<3)
				{
					try
					{
						Thread.sleep(100);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}					
				}
				for(String s:scoresMsg)
				{
					player1.dout.writeUTF(s);
					player2.dout.writeUTF(s);
					player3.dout.writeUTF(s);
					player4.dout.writeUTF(s);
				}
				*/
			}
			else if(count>15)
			//	else if(count==3)
			{//客户端人数已满
			    System.out.println("<#FULL#>");
				dout.writeUTF("<#FULL#>");
                dout.flush();
				dout.close();
				din.close();
				sc.close();
			}
		}
	}
}
