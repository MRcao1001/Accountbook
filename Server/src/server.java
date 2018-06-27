import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.mysql.jdbc.PreparedStatement;


public class server{

	private ServerSocket ss;
	private ServerSocket ds;
	private ServerSocket aAs;
	private ServerSocket aus;
	private ServerSocket sus;

	private Socket socket;
	private Socket socket_ds;
	private Socket socket_aAs;
	private Socket socket_aus;
	private Socket socket_sus;


	private DataInputStream dis;
	private DataInputStream dis_2;
	private DataInputStream dis_3;
	private DataInputStream dis_4;
	private DataInputStream dis_5;


	private DataOutputStream dos;
	private DataOutputStream dos_2;
	private DataOutputStream dos_3;
	private DataOutputStream dos_4;
	private DataOutputStream dos_5;


	static Statement statement;
	public String homeNum;
    //�������ݿ����������Щ��Java����Ĺ����𣿽�һ��˵��java��jdbc�еĹ���  
    public static Connection connection;
    public static Connection conn=null; 
    public static Statement stmt=null;          //�������  
    public static PreparedStatement pStmt=null; //Ԥ�����������  
    public static ResultSet rs=null;            //���������
	public server(){
		for(int i=0;i<=50;i++){
			System.out.println();
		}
		new ServerThread().start();
		new DisplayServiceThread().start();
		new addAccountThread().start();
		new adduserThread().start();
		new showUserServiceThread().start();
	}
	//�����Ʒ��������ݿ�
	private static void connect()
    {
	String driver="com.mysql.jdbc.Driver";  
        String url="jdbc:mysql://127.0.0.1:3306/accountbook?useUnicode=true&characterEncoding=UTF-8";  
        String user="root";  
        String password="123456";  
          
        //������������  
        try {  
            Class.forName(driver);  
            System.out.println("����������سɹ�");  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }  
          
        //�������ݿ�  
         
        try {  
            conn=DriverManager.getConnection(url, user, password);  
            if (!conn.isClosed()) {  
                System.out.println("�������ݿ�ɹ�");  
            }  
        } catch (SQLException e) {  
            System.out.println("�������ݿ�ʧ��: "+e.getMessage());  
        } 
    }
	//�������
	public void insert(String s) throws SQLException
    {

        connect();
        try 
        {
           statement.executeUpdate("INSERT INTO userinfo VALUES ('"+s+"','"+s.length()+"')");
        } catch (SQLException e) {
           System.out.println("�Ѿ����������");
           e.printStackTrace();
           }
        try 
        {
           statement.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           }
        try 
        {
           conn.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           }
    }
	//ɾ�����ݿ�word���ĳ������
    public void delete(String s )
    {
        connect();
        try 
        {
           statement.executeUpdate("DELETE FROM usernotes WHERE noteTime='"+s+"'");
        } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           }
        try 
        {
           statement.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           }
        try 
        {
           conn.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           }
    }
  //�˻���ע��
    public void register(String user ,String password)
    {
        connect();
        try 
        {
           statement.executeUpdate("INSERT INTO userinfo VALUES ('"+user+"','"+password+"')");
        } catch (SQLException e) {
           System.out.println("�û��Ѵ���");
           e.printStackTrace();
           }
        try 
        {
           statement.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           }
        try 
        {
           connection.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           }
    }
    //�û��ĵ�¼
    public boolean login(String ID ,String userPwd)
    {
        connect();
        ResultSet set=null;
        System.out.println("�ѽ����¼����");
        try {
        		Statement stmt = conn.createStatement();
            	set = stmt.executeQuery("SELECT userPwd FROM userinfo where ID='"+ID+"'");
        		System.out.println("��ȡ����...");
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           return false;
       }
        try {
           while(set.next())
            {
                if(userPwd.equals(set.getString(1))){
                	System.out.println("����������ȷ");
                    return true;
                }
                	
                else
                    return false;
            }
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }

        return false;
    }

   //��ʾ���ݷ���˷���
	public ArrayList displayData(String homeNum)
    {
        connect();
        ResultSet set=null;
        ResultSet set_2=null;
		ArrayList<String>  strArray = new ArrayList<String> ();
        System.out.println("�ѽ����������");
        try {
        		Statement stmt = conn.createStatement();
            	set = stmt.executeQuery("SELECT * FROM usernotes where home='"+homeNum+"'");
        		System.out.println("�Ѿ���ȡ��������ͥ���ڵ���Ϣ");
        		while(set.next()){
            		String str1 = "�û�:"+set.getString("userName")+"\n"+"�˵�����ԭ��:"+set.getString("noteName")+"\n"+"���:"+set.getString("noteMoney")+"\n"+set.getString("noteType")+"\n"+"_";
        			strArray.add(str1);
        		}
        		return strArray;
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }

        return strArray;
    }
	   //��ʾ������Ϣ����
		public ArrayList showUser(String userName)
	    {
	        connect();
	        ResultSet set=null;
	        ResultSet set_2=null;
			ArrayList<String>  strArray = new ArrayList<String> ();
	        System.out.println("�ѽ����������");
	        try {
	        		Statement stmt = conn.createStatement();
	            	set = stmt.executeQuery("SELECT * FROM usernotes where userName='"+userName+"'");
	        		System.out.println("�Ѿ���ȡ����ǰ�û���Ϣ");
	        		while(set.next()){
	            		String str1 = "�û�:"+set.getString("userName")+"\n"+"�˵�����ԭ��:"+set.getString("noteName")+"\n"+"���:"+set.getString("noteMoney")+"\n"+set.getString("noteType")+"\n"+"_";
	        			strArray.add(str1);
	        		}
	        		return strArray;
	       } catch (SQLException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }

	        return strArray;
	    }
	//������д�����ݿ�
	public void addAccount(String userName, String home, String noteName, String noteMoney, String noteType ) throws SQLException
    {
        connect();
        Statement stmt = conn.createStatement();
        try 
        {
        	stmt.executeUpdate("INSERT INTO usernotes(userName, home, noteName, noteMoney, noteType) VALUES ('"+userName+"','"+home+"','"+noteName+"','"+noteMoney+"','"+noteType+"')");
        	System.out.print("insert success");
        } catch (SQLException e) {
           System.out.println("inserterror");
           e.printStackTrace();
           }
        try 
        {
           stmt.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
        	System.out.println("statement error");
           e.printStackTrace();
           }
        try 
        {
           conn.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
        	System.out.println("connection error");
           e.printStackTrace();
           }
    }
	
	//����û�
	public void adduser(String userName, String Pwd, String homeNum ) throws SQLException
    {
        connect();
        Statement stmt = conn.createStatement();
        try 
        {	
        	stmt.executeUpdate("INSERT INTO userinfo(ID, userPwd, homeNum) VALUES ('"+userName+"','"+Pwd+"','"+homeNum+"')");
        	System.out.print("insert success");
        } catch (SQLException e) {
           System.out.println("inserterror");
           e.printStackTrace();
           }
        try 
        {
           stmt.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
        	System.out.println("statement error");
           e.printStackTrace();
           }
        try 
        {
           conn.close();
        } catch (SQLException e) {
           // TODO Auto-generated catch block
        	System.out.println("connection error");
           e.printStackTrace();
           }
    }
	
	//�鿴�Ƿ��д��û�
		public int search(String userName)
	    {
	        connect();
	        ResultSet set=null;
	        ResultSet set_2=null;
	        System.out.println("�ѽ����������");
	        try {
	        		Statement stmt = conn.createStatement();
	            	set = stmt.executeQuery("SELECT ID FROM userinfo where ID='"+userName+"'");
	        		System.out.println("���ڲ�ѯ�Ƿ��ѱ�ע��");
	        		while(set.next()){
		        		System.out.println("���û����ѱ�ע��");
	        			return 1;
	        		}
	        		return 0;
	       } catch (SQLException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	           return 0;
	       }
//	        
	    }
    //�Խ��߳������ڷ������з��ϵ�����
    class DisplayServiceThread extends Thread{
		public void run(){
			try{
				ds = new ServerSocket(4322);
				System.out.println("��ʾ���е�ǰ��ͥ�ڵ��û���¼����--����������");
				while(true){
					socket_ds = ds.accept();//zuse thread
					System.out.println("��ʾ���е�ǰ��ͥ�ڵ��û���¼--�û������ȡ����");
					dis_2 = new DataInputStream(socket_ds.getInputStream());
					dos_2 = new DataOutputStream(socket_ds.getOutputStream());
					String msg = "";
					System.out.println("��ʾ���е�ǰ��ͥ�ڵ��û���¼--����1��");
					Thread.sleep(1000);
					msg = dis_2.readUTF();
					String userinfo[] = msg.split("_");
					homeNum = userinfo[1];
					System.out.println("Thread2--"+msg);
					ArrayList<String> resultList = displayData(homeNum);
					//���������������
					Collections.reverse(resultList);
					homeNum = "";
					String result = "";
					for(int i=0 ; i<resultList.size();i++){
						result = result + resultList.get(i);
					}
					//System.out.print(result);
					dos_2.writeUTF(result);
					dos_2.flush();
					System.out.println("��ʾ���е�ǰ��ͥ�ڵ��û���¼--�Ѿ�������������Ϣ���͵��ͻ���\n");
				}
			}
			catch(Exception e){
				System.out.println("1--error");
			}
			finally{
				try{
					dis_2.close();
					dos_2.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
  //�Խ��߳������ڷ��ص�ǰ�û������Ѽ�¼
    class showUserServiceThread extends Thread{
		public void run(){
			try{
				sus = new ServerSocket(4330);
				System.out.println("�û����������б���ʾ����--����������");
				while(true){
					socket_sus = sus.accept();//zuse thread
					System.out.println("�û����������б���ʾ����--�û����������ȡ��Ϣ");
					dis_5 = new DataInputStream(socket_sus.getInputStream());
					dos_5 = new DataOutputStream(socket_sus.getOutputStream());
					String msg = "";
					System.out.println("�û����������б���ʾ����--����1��");
					Thread.sleep(1000);
					msg = dis_5.readUTF();
					String userNmae = msg;
					System.out.println("�û����������б���ʾ����--�û����ǣ�"+msg+"���û������ȡ����");
					ArrayList<String> resultList = showUser(userNmae);
					//���������������
					Collections.reverse(resultList);
					String result = "";
					for(int i=0 ; i<resultList.size();i++){
						result = result + resultList.get(i);
					}
					//System.out.print(result);
					dos_5.writeUTF(result);
					dos_5.flush();
					System.out.println("�û����������б���ʾ����--�����Ѿ��ɹ����͵��ͻ���\n");
				}
			}
			catch(Exception e){
				System.out.println("1--error");
			}
			finally{
				try{
					dis_5.close();
					dos_5.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	//�Խ��߳���������֤��¼
	class ServerThread extends Thread{
		public void run(){
			try{
				ss = new ServerSocket(4321);
				System.out.println("��¼����--����������");
				while(true){
					socket = ss.accept();//zuse thread
					System.out.println("��¼����--һ���û������¼");
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
					String msg = "";
					System.out.println("��¼����--����2��");
					Thread.sleep(2000);
					msg = dis.readUTF();
					//��ִ��������ַ�����������string���������
					String a[] = msg.split("_");
					String home= a[0];
					String ID = a[1];
					String userPwd = a[2];
					System.out.println("��¼����--�����¼���û�������Ϣ����"+home+ID+userPwd+"");
					//����3��
					System.out.println("��¼����--����3��");
					Thread.sleep(3000);
					//�ȶ��û���������
					boolean resultof_lg = login(ID,userPwd);
					if(resultof_lg==true){
						dos.writeUTF("OK");
						dos.flush();
						System.out.println("��¼����--��¼�ɹ�\n");
					}
					else{
						dos.writeUTF("filed");
						dos.flush();
						System.out.println("��¼����--�������\n");
					}
					if(msg != null){
						System.out.println(msg);
					}
				}
			}
			catch(Exception e){
				System.out.println("Thread1--error");
			}
			finally{
				try{
					dis.close();
					dos.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	//�Խ��߳�����������˵�
		class addAccountThread extends Thread{
			public void run(){
				try{
					aAs = new ServerSocket(4323);
					System.out.println("����˵�����--����������");
					while(true){
						socket_aAs = aAs.accept();//zuse thread
						System.out.println("����˵�����--һ���û��������������");
						dis_3 = new DataInputStream(socket_aAs.getInputStream());
						dos_3 = new DataOutputStream(socket_aAs.getOutputStream());
						String msg = "";
						System.out.println("T����˵�����--����2��");
						Thread.sleep(2000);
						msg = dis_3.readUTF();
						//��ִ��������ַ�����������string���������
						String a[] = msg.split("_");
						String ID= a[0];
						String homeNum = a[1];
						String noteName = a[2];
						String Money = a[3];
						String noteType = a[4];
						System.out.println("����˵�����--�û�������ӵ����ݣ�"+ID+homeNum+noteName+Money+noteType);
						//����3��
						System.out.println("����˵�����--�������...��ӳɹ�\n");
						Thread.sleep(3000);
						addAccount(ID,homeNum,noteName,Money,noteType);
						dos_3.writeUTF("OK");
						dos_3.flush();
						
					}
				}
				catch(Exception e){
					System.out.println("Thread3--error");
				}
				finally{
					try{
						dis_3.close();
						dos_3.close();
					}
					catch(IOException e){
						e.printStackTrace();
					}
				}
			}
		}
		
		//�Խ��߳���������û�
				class adduserThread extends Thread{
					public void run(){
						try{
							aus = new ServerSocket(9999);
							System.out.println("ע�����--�����Ѿ�����");
							while(true){
								socket_aus = aus.accept();//zuse thread
								System.out.println("ע�����--һ���û�����ע��");
								dis_4 = new DataInputStream(socket_aus.getInputStream());
								dos_4 = new DataOutputStream(socket_aus.getOutputStream());
								String msg = "";
								System.out.println("ע�����--����2��");
								Thread.sleep(2000);
								msg = dis_4.readUTF();
								//��ִ��������ַ�����������string���������
								String a[] = msg.split("_");
								String homeNum= a[0];
								String userName = a[1];
								String pwd = a[2];
								int num = search(userName);
								if(num==1){
									dos_4.writeUTF("filed");
									dos_4.flush();
									return;
								}
								if(num==0){
									System.out.println("ע�����--ע����Ϣ���£�"+homeNum+userName+pwd);
									//����3��
									System.out.println("ע�����--����3��");
									Thread.sleep(3000);
									adduser(userName,pwd,homeNum);
									dos_4.writeUTF("OK");
									dos_4.flush();
									System.out.println("ע�����--ע��ɹ�\n");
									return;
								}
								
//								dos.writeUTF("filed");
//								dos.flush();
								
							}
						}
						catch(Exception e){
							e.printStackTrace();
							System.out.println("Thread4--error");
						}
						finally{
							try{
								//dis_4.close();
								dos_4.close();
							}
							catch(IOException e){
								e.printStackTrace();
							}
						}
					}
				}
	public static void main(String[] args) throws IOException
	{
		new server();
	}
}
