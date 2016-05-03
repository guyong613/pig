package pig;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static pig.Server.*;

public class ServerAgent extends Thread
{
	Socket sc; //网络套接字
	DataInputStream din;
	DataOutputStream dout;
	boolean flag=true;
	int count=17;
	static String mys="<#TURN#>";
	static String otherPai="";
	static ArrayList<String> almy = new ArrayList<String>();
	static int myturn=0;
	static int firstplay=0;
	static int thisover=0;
	static String[] getplayX=new String[10];
	public ServerAgent(Socket sc,DataInputStream din,DataOutputStream dout)
	{		
		this.sc=sc;
		this.din=din;
		this.dout=dout;	
		for(int i=0;i<4;i++) getplayX[i]="0,1";
	}
	
	public void run()
	{
		while(flag)
		{
			try
			{
				String msg=din.readUTF();
				System.out.println(msg);
				if(msg.startsWith("<#PLAY#>"))
				{  //客户端发牌并且告知服务器 并且服务器判断下一个出牌的玩家
					String cards=msg.substring(8);
					ServerAgent next=null;
					String mTemp="<#CURR#>";
					if(currPlayer==player1)
					{
						mTemp=mTemp+"1";
						next=player2;
					}
					else if(currPlayer==player2)
					{
						mTemp=mTemp+"2";
						next=player3;
					}
					else if(currPlayer==player3)
					{
						mTemp=mTemp+"3";
						next=player4;
					}
					else if(currPlayer==player4)
					{
						mTemp=mTemp+"4";
						next=player1;
					}
					//GY取得下一个玩家的数字号0-3,去掉前缀"<#CURR#>"
					//int nextgy=(Integer.parseInt(mTemp.substring(8))-1)%3;
					
					player1.dout.writeUTF(mTemp);
					player2.dout.writeUTF(mTemp);
					player3.dout.writeUTF(mTemp);
					player4.dout.writeUTF(mTemp);
					
					mTemp="<#CARDS#>"+cards;//上一个玩家发的牌的信息
					
					player1.dout.writeUTF(mTemp);
					player2.dout.writeUTF(mTemp);
					player3.dout.writeUTF(mTemp);
					player4.dout.writeUTF(mTemp);
					
					//next.dout.writeUTF("<#YOU#>");//下一个获得牌权
					//currPlayer=next;
					myturn=myturn+1;
					System.out.println(myturn);
					//gy 记录本轮出牌
			if(myturn==1)
					{
						mys=mys+cards;
					}
					else 
					{mys=mys+","+cards;}
						
					player1.dout.writeUTF(mys);
					player2.dout.writeUTF(mys);
					player3.dout.writeUTF(mys);
					player4.dout.writeUTF(mys);		
					
					
					//ggy可能是多余的
					/*
					player1.dout.flush();
					player2.dout.flush();
					player3.dout.flush();
					player4.dout.flush();		
					*/
					
			if(myturn==4)//一轮结速记分并处理
					{	
						//将本轮mys回入多轮序列集合almy
						//almy.add(mys+":"+String.format("%-2s",myturn));
						almy.add(mys);
						//多轮处理，产生下一轮发牌人currPlayer，各轮分牌到String[] getplayX
						getplayX=myDturn();
                        //置currPlayer并发送分牌结果到相应客户端
						switch(firstplay)
						{
						case 0: currPlayer=player1;break;
						case 1: currPlayer=player2;break;
						case 2: currPlayer=player3;break;
						case 3: currPlayer=player4;break;
						default:currPlayer=player1;
						}
						//System.out.println("nextplay is "+firstplay);
						String  getPai="<#GETPAI#>";
						for(int i=0;i<4;i++) 
						{
							if (getplayX[i].length()<2) getplayX[i]="0,1";
						}
						player1.dout.writeUTF(getPai+getplayX[0]);
						player2.dout.writeUTF(getPai+getplayX[1]);
						player3.dout.writeUTF(getPai+getplayX[2]);
						player4.dout.writeUTF(getPai+getplayX[3]);	
						//发送其它玩家的牌以供客户端显示
						/*otherPai = "<#OTHER#>"+getplayX[1]+":"+getplayX[2]+":"+getplayX[3];
						player1.dout.writeUTF(otherPai);
						otherPai = "<#OTHER#>"+getplayX[2]+":"+getplayX[3]+":"+getplayX[0];
						player2.dout.writeUTF(otherPai);
						otherPai = "<#OTHER#>"+getplayX[3]+":"+getplayX[0]+":"+getplayX[1];
						player3.dout.writeUTF(otherPai);
						otherPai = "<#OTHER#>"+getplayX[0]+":"+getplayX[1]+":"+getplayX[2];
						player4.dout.writeUTF(otherPai);*/
						
						//本轮结束清零，下一轮初始化
						mys="<#TURN#>";
						myturn=0;
						next=currPlayer;
					}
		         
			otherPai = "<#OTHER#>"+getplayX[1]+"="+getplayX[2]+"="+getplayX[3];
			player1.dout.writeUTF(otherPai);
			otherPai = "<#OTHER#>"+getplayX[2]+"="+getplayX[3]+"="+getplayX[0];
			player2.dout.writeUTF(otherPai);
			otherPai = "<#OTHER#>"+getplayX[3]+"="+getplayX[0]+"="+getplayX[1];
			player3.dout.writeUTF(otherPai);
			otherPai = "<#OTHER#>"+getplayX[0]+"="+getplayX[1]+"="+getplayX[2];
			player4.dout.writeUTF(otherPai);     
			next.dout.writeUTF("<#YOU#>");//下一个获得牌权
			     currPlayer=next;	
					//endgy
					
				}
				else if(msg.startsWith("<#COUNT#>"))
				{//转发<#COUNT#>  发送要出的牌的牌号和当前玩家的标志位
					player1.dout.writeUTF(msg);
					player2.dout.writeUTF(msg);
					player3.dout.writeUTF(msg);
					player4.dout.writeUTF(msg);
				}
				else if(msg.startsWith("<#I_WIN#>"))
				{//收到<#I_WIN#>(赢的)消息  并发送<#FINISH#>消息
					int currNumTemp=1;
					//int currNumTemp=-1;
					/*if(this==player1)
					{
						currNumTemp=1;
					}
					else if(this==player2)
					{
						currNumTemp=2;
					}
					else if(this==player3)
					{
						currNumTemp=3;
					}
					else if(this==player4)
					{
						currNumTemp=4;
					}*/
					thisover=thisover+1;
					if (thisover==4)
					{
					Server.count=0;//服务器记录连接的人数清零
					thisover=0;//本次出完人数清零
					
					player1.dout.writeUTF("<#FINISH#>"+currNumTemp);
					player2.dout.writeUTF("<#FINISH#>"+currNumTemp);
					player3.dout.writeUTF("<#FINISH#>"+currNumTemp);
					player4.dout.writeUTF("<#FINISH#>"+currNumTemp);
					
					//一局游戏结束 关闭程序虚拟服务器端线程 并且关闭输入输出流和网络套接字
					player1.flag=false;
					player1.dout.close();
					player1.din.close();
					player1.sc.close();
					
					player2.flag=false;
					player2.dout.close();
					player2.din.close();
					player2.sc.close();
					
					player3.flag=false;
					player3.dout.close();
					player3.din.close();
					player3.sc.close();
					
					player4.flag=false;
					player4.dout.close();
					player4.din.close();
					player4.sc.close();
					}
					
				}
				else if(msg.startsWith("<#NO_PLAY#>"))
				{//客户端点击放弃按钮之后 服务器端判断下一个玩家是谁
					ServerAgent next=null;
					if(currPlayer==player1)
					{
						next=player2;
					}
					else if(currPlayer==player2)
					{
						next=player3;
					}
					else if(currPlayer==player3)
					{
						next=player4;
					}
					else if(currPlayer==player4)
					{
						next=player1;
					}
					
					next.dout.writeUTF("<#YOU#>");
					currPlayer=next;
				}
				else if(msg.startsWith("<#EXIT#>"))
				{//收到客户端退出消息 并进行相关的设置
					Server.count=0;
					thisover=0;	
					player1.dout.writeUTF("<#EXIT#>");
					player2.dout.writeUTF("<#EXIT#>");
					player3.dout.writeUTF("<#EXIT#>");
					player4.dout.writeUTF("<#EXIT#>");
					
					//有客户端退出 关闭程序虚拟服务器端线程 并且关闭输入输出流和网络套接字
					player1.flag=false;
					player1.dout.close();
					player1.din.close();
					player1.sc.close();
					
					player2.flag=false;
					player2.dout.close();
					player2.din.close();
					player2.sc.close();
					
					player3.flag=false;
					player3.dout.close();
					player3.din.close();
					player3.sc.close();
					
					player4.flag=false;
					player4.dout.close();
					player4.din.close();
					player4.sc.close();
					
				}
				else if(msg.startsWith("<#SCORE#>"))
				{
					scoresMsg[scoresIndex++]=msg;
				}
			}
			catch(Exception e)
			{
				//re_start();
				flag=false;
				System.out.println("失踪一个或是有异GY常发生"+e);	
				Server.count=0;
				thisover=0;	
			}
		}
	}	
	public String[] myDturn()
	{ 
		String[] playturns={"","","",""};			
		firstplay=0;//最大者
		firstplay=firstplay+FPUtil.pigfirst;//黑桃J先出
		for(String f:almy)
		    {//遍历每轮归集属于大者的牌
			    int j=0;
		    	f=f.substring(8);
		    	String[] tempt = f.split("\\,");
		    	int intfirst=Integer.parseInt(tempt[0]);

		    	    for (int i=1;i<4;i++)
		    	    {
		    	    	int maxP=intfirst;
		    	    	int tempP=Integer.parseInt(tempt[i]);
		    	    	//比较牌大小
		    	  //  if (maxP<tempP)
		    	      if ( ((maxP%13) < (tempP%13)) &&  ( Math.floor(maxP/13) == Math.floor(tempP/13) )) 
		    	          {
		    	    	    intfirst=Integer.parseInt(tempt[i]);
		    	    		j=i;
		    	          }
		    	    }
		    	    firstplay=(firstplay+j)%4;
		    	    //加入最大者加入本轮牌		    	    	   	       
		    	   playturns[firstplay] =playturns[firstplay]+","+ f;
		            
		    } 	
		//去掉前导多一逗号
		for (int i=0;i<4;i++)
		{
			if (playturns[i]!="")
		    {
				playturns[i] =playturns[i].substring(1);
		    }
		    System.out.println(playturns[i] );
		}
		System.out.println("下一轮首发是："+firstplay);
		return playturns;
	         
	}

	public void re_start()
	{
      try {
		Server.count=0;//服务器记录连接的人数清零
		thisover=0;//本次出完人数清零
		int currNumTemp=1;
		player1.dout.writeUTF("<#FINISH#>"+currNumTemp);
		player2.dout.writeUTF("<#FINISH#>"+currNumTemp);
		player3.dout.writeUTF("<#FINISH#>"+currNumTemp);
		player4.dout.writeUTF("<#FINISH#>"+currNumTemp);
		
		//一局游戏结束 关闭程序虚拟服务器端线程 并且关闭输入输出流和网络套接字
		player1.flag=false;
		player1.dout.close();
		player1.din.close();
		player1.sc.close();
		
		player2.flag=false;
		player2.dout.close();
		player2.din.close();
		player2.sc.close();
		
		player3.flag=false;
		player3.dout.close();
		player3.din.close();
		player3.sc.close();
		
		player4.flag=false;
		player4.dout.close();
		player4.din.close();
		player4.sc.close();
	    }
		catch(Exception e)
		{
			
			System.out.println("失踪一个或是有异GYrestart常发生");				
		}
		
	}

}
