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
    //公共数据库操作对象，这些是Java里面的功能吗？进一步说是java的jdbc中的功能  
    public static Connection connection;
    public static Connection conn=null; 
    public static Statement stmt=null;          //命令集对象  
    public static PreparedStatement pStmt=null; //预编译命令集对象  
    public static ResultSet rs=null;            //结果集对象
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
	//连接云服务器数据库
	private static void connect()
    {
	String driver="com.mysql.jdbc.Driver";  
        String url="jdbc:mysql://127.0.0.1:3306/accountbook?useUnicode=true&characterEncoding=UTF-8";  
        String user="root";  
        String password="123456";  
          
        //加载驱动程序  
        try {  
            Class.forName(driver);  
            System.out.println("驱动程序加载成功");  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        }  
          
        //链接数据库  
         
        try {  
            conn=DriverManager.getConnection(url, user, password);  
            if (!conn.isClosed()) {  
                System.out.println("连接数据库成功");  
            }  
        } catch (SQLException e) {  
            System.out.println("链接数据库失败: "+e.getMessage());  
        } 
    }
	//添加数据
	public void insert(String s) throws SQLException
    {

        connect();
        try 
        {
           statement.executeUpdate("INSERT INTO userinfo VALUES ('"+s+"','"+s.length()+"')");
        } catch (SQLException e) {
           System.out.println("已经有这个词语");
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
	//删除数据库word表的某个词语
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
  //账户的注册
    public void register(String user ,String password)
    {
        connect();
        try 
        {
           statement.executeUpdate("INSERT INTO userinfo VALUES ('"+user+"','"+password+"')");
        } catch (SQLException e) {
           System.out.println("用户已存在");
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
    //用户的登录
    public boolean login(String ID ,String userPwd)
    {
        connect();
        ResultSet set=null;
        System.out.println("已进入登录函数");
        try {
        		Statement stmt = conn.createStatement();
            	set = stmt.executeQuery("SELECT userPwd FROM userinfo where ID='"+ID+"'");
        		System.out.println("获取密码...");
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           return false;
       }
        try {
           while(set.next())
            {
                if(userPwd.equals(set.getString(1))){
                	System.out.println("密码输入正确");
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

   //显示数据服务端方法
	public ArrayList displayData(String homeNum)
    {
        connect();
        ResultSet set=null;
        ResultSet set_2=null;
		ArrayList<String>  strArray = new ArrayList<String> ();
        System.out.println("已进入遍历函数");
        try {
        		Statement stmt = conn.createStatement();
            	set = stmt.executeQuery("SELECT * FROM usernotes where home='"+homeNum+"'");
        		System.out.println("已经获取到所属家庭号内的信息");
        		while(set.next()){
            		String str1 = "用户:"+set.getString("userName")+"\n"+"账单产生原因:"+set.getString("noteName")+"\n"+"金额:"+set.getString("noteMoney")+"\n"+set.getString("noteType")+"\n"+"_";
        			strArray.add(str1);
        		}
        		return strArray;
       } catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }

        return strArray;
    }
	   //显示个人信息方法
		public ArrayList showUser(String userName)
	    {
	        connect();
	        ResultSet set=null;
	        ResultSet set_2=null;
			ArrayList<String>  strArray = new ArrayList<String> ();
	        System.out.println("已进入遍历函数");
	        try {
	        		Statement stmt = conn.createStatement();
	            	set = stmt.executeQuery("SELECT * FROM usernotes where userName='"+userName+"'");
	        		System.out.println("已经获取到当前用户信息");
	        		while(set.next()){
	            		String str1 = "用户:"+set.getString("userName")+"\n"+"账单产生原因:"+set.getString("noteName")+"\n"+"金额:"+set.getString("noteMoney")+"\n"+set.getString("noteType")+"\n"+"_";
	        			strArray.add(str1);
	        		}
	        		return strArray;
	       } catch (SQLException e) {
	           // TODO Auto-generated catch block
	           e.printStackTrace();
	       }

	        return strArray;
	    }
	//将数据写入数据库
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
	
	//添加用户
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
	
	//查看是否有此用户
		public int search(String userName)
	    {
	        connect();
	        ResultSet set=null;
	        ResultSet set_2=null;
	        System.out.println("已进入遍历函数");
	        try {
	        		Statement stmt = conn.createStatement();
	            	set = stmt.executeQuery("SELECT ID FROM userinfo where ID='"+userName+"'");
	        		System.out.println("正在查询是否已被注册");
	        		while(set.next()){
		        		System.out.println("此用户名已被注册");
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
    //自建线程类用于返回所有符合的内容
    class DisplayServiceThread extends Thread{
		public void run(){
			try{
				ds = new ServerSocket(4322);
				System.out.println("显示所有当前家庭内的用户记录服务--服务已启动");
				while(true){
					socket_ds = ds.accept();//zuse thread
					System.out.println("显示所有当前家庭内的用户记录--用户请求获取数据");
					dis_2 = new DataInputStream(socket_ds.getInputStream());
					dos_2 = new DataOutputStream(socket_ds.getOutputStream());
					String msg = "";
					System.out.println("显示所有当前家庭内的用户记录--休眠1秒");
					Thread.sleep(1000);
					msg = dis_2.readUTF();
					String userinfo[] = msg.split("_");
					homeNum = userinfo[1];
					System.out.println("Thread2--"+msg);
					ArrayList<String> resultList = displayData(homeNum);
					//逆序输出数组内容
					Collections.reverse(resultList);
					homeNum = "";
					String result = "";
					for(int i=0 ; i<resultList.size();i++){
						result = result + resultList.get(i);
					}
					//System.out.print(result);
					dos_2.writeUTF(result);
					dos_2.flush();
					System.out.println("显示所有当前家庭内的用户记录--已经将所有所需信息发送到客户端\n");
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
  //自建线程类用于返回当前用户的消费记录
    class showUserServiceThread extends Thread{
		public void run(){
			try{
				sus = new ServerSocket(4330);
				System.out.println("用户个人消费列表显示服务--服务已启动");
				while(true){
					socket_sus = sus.accept();//zuse thread
					System.out.println("用户个人消费列表显示服务--用户正在请求获取信息");
					dis_5 = new DataInputStream(socket_sus.getInputStream());
					dos_5 = new DataOutputStream(socket_sus.getOutputStream());
					String msg = "";
					System.out.println("用户个人消费列表显示服务--休眠1秒");
					Thread.sleep(1000);
					msg = dis_5.readUTF();
					String userNmae = msg;
					System.out.println("用户个人消费列表显示服务--用户名是："+msg+"的用户请求获取数据");
					ArrayList<String> resultList = showUser(userNmae);
					//逆序输出数组内容
					Collections.reverse(resultList);
					String result = "";
					for(int i=0 ; i<resultList.size();i++){
						result = result + resultList.get(i);
					}
					//System.out.print(result);
					dos_5.writeUTF(result);
					dos_5.flush();
					System.out.println("用户个人消费列表显示服务--数据已经成功发送到客户端\n");
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
	//自建线程类用于验证登录
	class ServerThread extends Thread{
		public void run(){
			try{
				ss = new ServerSocket(4321);
				System.out.println("登录服务--服务已启动");
				while(true){
					socket = ss.accept();//zuse thread
					System.out.println("登录服务--一名用户请求登录");
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
					String msg = "";
					System.out.println("登录服务--休眠2秒");
					Thread.sleep(2000);
					msg = dis.readUTF();
					//拆分传递来的字符串存入三个string变量并输出
					String a[] = msg.split("_");
					String home= a[0];
					String ID = a[1];
					String userPwd = a[2];
					System.out.println("登录服务--请求登录的用户基本信息如下"+home+ID+userPwd+"");
					//休眠3秒
					System.out.println("登录服务--休眠3秒");
					Thread.sleep(3000);
					//比对用户名和密码
					boolean resultof_lg = login(ID,userPwd);
					if(resultof_lg==true){
						dos.writeUTF("OK");
						dos.flush();
						System.out.println("登录服务--登录成功\n");
					}
					else{
						dos.writeUTF("filed");
						dos.flush();
						System.out.println("登录服务--程序出错\n");
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
	
	//自建线程类用于添加账单
		class addAccountThread extends Thread{
			public void run(){
				try{
					aAs = new ServerSocket(4323);
					System.out.println("添加账单服务--服务已启动");
					while(true){
						socket_aAs = aAs.accept();//zuse thread
						System.out.println("添加账单服务--一名用户发出了添加请求");
						dis_3 = new DataInputStream(socket_aAs.getInputStream());
						dos_3 = new DataOutputStream(socket_aAs.getOutputStream());
						String msg = "";
						System.out.println("T添加账单服务--休眠2秒");
						Thread.sleep(2000);
						msg = dis_3.readUTF();
						//拆分传递来的字符串存入三个string变量并输出
						String a[] = msg.split("_");
						String ID= a[0];
						String homeNum = a[1];
						String noteName = a[2];
						String Money = a[3];
						String noteType = a[4];
						System.out.println("添加账单服务--用户请求添加的数据："+ID+homeNum+noteName+Money+noteType);
						//休眠3秒
						System.out.println("添加账单服务--正在添加...添加成功\n");
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
		
		//自建线程用于添加用户
				class adduserThread extends Thread{
					public void run(){
						try{
							aus = new ServerSocket(9999);
							System.out.println("注册服务--服务已经启动");
							while(true){
								socket_aus = aus.accept();//zuse thread
								System.out.println("注册服务--一名用户请求注册");
								dis_4 = new DataInputStream(socket_aus.getInputStream());
								dos_4 = new DataOutputStream(socket_aus.getOutputStream());
								String msg = "";
								System.out.println("注册服务--休眠2秒");
								Thread.sleep(2000);
								msg = dis_4.readUTF();
								//拆分传递来的字符串存入三个string变量并输出
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
									System.out.println("注册服务--注册信息如下："+homeNum+userName+pwd);
									//休眠3秒
									System.out.println("注册服务--休眠3秒");
									Thread.sleep(3000);
									adduser(userName,pwd,homeNum);
									dos_4.writeUTF("OK");
									dos_4.flush();
									System.out.println("注册服务--注册成功\n");
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
