import java.io.*;  
import java.net.*;  
/** 
 *  
 * @author heqichang 
 * �ͻ��� 
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
            //�������Է���˵�������Ϣ  
             //name=din.readUTF();  
              
            //�������Է���˵���Ϣ  
            while(true)  
            {  
                read=din.readUTF();  
                System.out.println(name+"˵�����յ���"+read);  
                if(read.startsWith("<#EXIT#>")){
					//�õ�������˳���Ϸ����
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
        System.out.print("�������Ϸ���ˣ����������������");  
        name=din.readUTF();
        //String name="<#PLAY#>"+socket.toString(); 
        //����һ���߳̽��н��շ������Ϣ  
        Talk thread=new Talk();  
        thread.start();  
      
          
        //���͸�����˿ͻ��˵�������Ϣ  
        dout.writeUTF(name);  
          
        String info=new String();  
          
        //������Ϣ�������  
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