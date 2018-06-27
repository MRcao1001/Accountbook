package com.part5.accountbook.signUp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.part5.accountbook.MainActivity;
import com.part5.accountbook.R;
import com.part5.accountbook.mainList.MainListActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SignUpActivity extends Activity {
	//网络状态，如果连接嵌套字失败就赋值为error
	String netWork = "error";
	private TextView cancle;
	boolean status	;
	//返回标签
	private LinearLayout ll_back;
	//登录按钮
	private Button bt_signUp;
	//传值变量
	public String type;
	//三个输入文本框
	private EditText et_homeNum;
	private EditText et_userName;
	private EditText et_signPwd1;
	private EditText et_signPwd2;
	//基于TCP协议网络程序的对象
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    private TextView mTextView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //实例化所有输入框
        et_homeNum = (EditText)findViewById(R.id.et_signhome);
        et_userName = (EditText)findViewById(R.id.et_signuserName);
        et_signPwd1 = (EditText)findViewById(R.id.et_signPwd1);
        et_signPwd2 = (EditText)findViewById(R.id.et_signPwd2);
        //取消注册按钮
        cancle = (TextView)findViewById(R.id.cancle);
        cancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
      //点击 id为 ll_back 的标签时返回上一个界面
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        //点击注册按钮
        bt_signUp = (Button)findViewById(R.id.bt_signUp);
        mTextView1 = (TextView)findViewById(R.id.myTextView2);
        bt_signUp.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mTextView1.setText("正在连接到服务器...");
				mTextView1.setBackgroundColor(Color.parseColor("#3572bd"));
				//网络连通性判断
				//网络通畅
				if(ping()){
					//输入判空
					if(et_homeNum.getText().toString().trim().isEmpty())
					{
						mTextView1.setText("家庭号不能为空");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(et_userName.getText().toString().trim().isEmpty())
					{
						mTextView1.setText("用户名不能为空");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(et_signPwd1.getText().toString().trim().isEmpty())
					{
						mTextView1.setText("密码不能为空");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					//两次输入相同的密码
					if(!(   et_signPwd1.getText().toString().equals(   et_signPwd2.getText().toString()   )   )   )
					{
						mTextView1.setText("请输入相同的密码");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					//判断是否包含下划线
					if(status = et_homeNum.getText().toString().contains("_")){
						mTextView1.setText("密码不可包含下划线");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(status = et_userName.getText().toString().contains("_")){
						mTextView1.setText("密码不可包含下划线");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(status = et_signPwd1.getText().toString().contains("_")){
						mTextView1.setText("密码不可包含下划线");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					//通过所有判断
					mTextView1.setText("正在尝试注册...");
					mTextView1.setBackgroundColor(Color.parseColor("#35bd4b"));
					SignUp();
    	        	return;
				}
				//无网络
				Toast.makeText(SignUpActivity.this, "网络异常，请检查网络连接", Toast.LENGTH_LONG).show();
				return;
			}
		});
        
    }
    
    
    public void SignUp() {
		// TODO Auto-generated method stub
		//实现Activity之间传值
		final Intent intent_signUp = new Intent();
		intent_signUp.setClass(SignUpActivity.this, MainListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("userName", et_userName.getText().toString()+"_"+ et_homeNum.getText().toString());
		intent_signUp.putExtras(bundle);
        //创建handle用于主线程和子线程通信
		final Handler mHandler = new Handler(){  
      	  
    	    @Override  
    	    public void handleMessage(Message msg) {  
    	        super.handleMessage(msg);  
    	        //如果成功连接了socket则进行以下操作
    	        if(netWork.equals("great")){
    	        	switch (msg.what) {  
        	        //判断账号密码并返回主界面
        	        case 0:
        	        	String data = (String)msg.obj;
        	        	String key = "OK";
        	            if(data.equals(key)){
        	            	mTextView1.setText("注册成功！");
        	            	mTextView1.setBackgroundColor(Color.parseColor("#35bd4b"));
        	            	startActivityForResult(intent_signUp, 0);
        	            }
        	            else if(data.equals("filed")){
        	            	mTextView1.setText("用户已被注册");
        	            	mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
        	            }
        	            break;  
        	        default:  
        	            break;  
        	        } 
    	        }
    	        //没有连接到服务器
    	        else
    	        {
    	        	return;
    	        }
    	        	
    	         
    	    }  
    	      
    	};
    	//client 中新建一个线程，创建socket嵌套字连接到4321
		new Thread() {
			   public void run(){

				   		try{
						
						socket = new Socket("193.112.137.246",9999);
						netWork = "great";
							//193.112.137.246---172.18.126.68
						}
						catch(Exception e){
							System.out.print("socket err");
							return;
						}
						//创建输入输出流对象读取数据
						try {
							dis = new DataInputStream(socket.getInputStream());
							dos = new DataOutputStream(socket.getOutputStream());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.print("DataStream create err");
						}
						
						try {
							//Thread.sleep(2000);
							String str = et_homeNum.getText().toString()+"_"+ et_userName.getText().toString()+"_"+ et_signPwd1.getText().toString();
							WriteString(str);
							
						} catch (Exception e) {
							// TODO: handle exception
							System.out.print("socket clsoe err");
						}

						//读取数据
						
						try {
							Thread.sleep(3000);
							ReadStr();
							socket.close();
							Message message = new Message();
				            message.obj = type;
				            mHandler.sendMessage(message);
						} catch (Exception e) {
							// TODO: handle exception
							System.out.print("socket clsoe err");
						}
						
						
						
					}
			   
			}.start();
			 		
	}
	public void WriteString(String str) {
		// TODO Auto-generated method stub
		try {
			dos.writeUTF(str);
			System.out.print("Read OK1");
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("write err");
		}
	}
	public void ReadStr() {
		// TODO Auto-generated method stub
		try {
			dis = new DataInputStream(socket.getInputStream());
			type = dis.readUTF();
			dis.close();
			dos.close();
//			if(type != null){
//				Toast.makeText(this, type, Toast.LENGTH_LONG).show();
//			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("Read err");
		}
	}
	//网络检测方法
	 private static final boolean ping() {

		 String result = null;

		 try {

		 String ip = "www.baidu.com";// 除非百度挂了，否则用这个应该没问题~

		 Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);//ping3次


		 // 读取ping的内容，可不加。

		 InputStream input = p.getInputStream();

		 BufferedReader in = new BufferedReader(new InputStreamReader(input));

		 StringBuffer stringBuffer = new StringBuffer();

		 String content = "";

		 while ((content = in.readLine()) != null) {

		 stringBuffer.append(content);

		 }

		 Log.i("TTT", "result content : " + stringBuffer.toString());


		 // PING的状态

		 int status = p.waitFor();

		 if (status == 0) {

		 result = "successful~";

		 return true;

		 } else {

		 result = "failed~ cannot reach the IP address";

		 }

		 } catch (IOException e) {

		 result = "failed~ IOException";

		 } catch (InterruptedException e) {

		 result = "failed~ InterruptedException";

		 } finally {

		 Log.i("TTT", "result = " + result);

		 }

		 return false;

	 }
    


}
