import java.io.*;  
import java.net.*;  
/** 
 *  
 * @author heqichang 
 * 客户端 
 * */  
public class Talk extends Thread{  
	static String name  ;
    static Socket socket;  
    public void run()  
    {  
        try {  
            //BufferedReader br2=new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            String read=new String();  
            DataInputStream din=new DataInputStream(socket.getInputStream());
			DataOutputStream dout=new DataOutputStream(socket.getOutputStream());  
            //接受来自服务端的姓名信息  
             //name=din.readUTF();  
              
            //接受来自服务端的信息  
            while(true)  
            {  
                read=din.readUTF();  
                System.out.println(name+"说或是收到："+read);  
                if(read.startsWith("<#EXIT#>")){
					//得到有玩家退出游戏结束
					//father.handler.sendEmptyMessage(4);
					//this.father.gameview.viewdraw.flag=false;					
					//this.flag=false;
					din.close();
					dout.close();
					socket.close();
            }  
            }  
        } 
            catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
       
      
    public static void main(String[] args) throws IOException  
    {  
         
        System.out.println("new..."); 
        socket=new Socket(InetAddress.getLocalHost(),9998);
        System.out.print(InetAddress.getLocalHost());  
        BufferedReader inputName=new BufferedReader(new InputStreamReader(System.in));  
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
        //PrintWriter pw=new PrintWriter(socket.getOutputStream());  
        DataInputStream din=new DataInputStream(socket.getInputStream());
        DataOutputStream dout=new DataOutputStream(socket.getOutputStream()); 
        System.out.print("已连接上服务端，请输入你的姓名：");  
        name=din.readUTF();
        //String name="<#PLAY#>"+socket.toString(); 
        //启动一个线程进行接收服务端信息  
        Talk thread=new Talk();  
        thread.start();  
      
          
        //发送给服务端客户端的姓名信息  
        dout.writeUTF(name);  
          
        String info=new String();  
          
        //发送消息给服务端  
        while(!info.equals("bye"))  
        {  
        	
            info=br.readLine();  
          
           // dout.writeUTF(String.format("%-20s",info)+"..."+name);
            dout.writeUTF(info);
            dout.flush();  
        }  
          
        dout.close();  
        socket.close();  
    }  
}  