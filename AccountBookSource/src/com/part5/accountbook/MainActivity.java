package com.part5.accountbook;

import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.part5.accountbook.mainList.MainListActivity;
import com.part5.accountbook.signUp.SignUpActivity;

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

/*�û���¼
 * ���������ؼ���
 * ��ͥ�Ų���Ϊ��¼ƾ�ݣ����������������ȷ����Ȼ������˵���¼Ϊ�յ����
 * ���������ֵ
 * ���߲������¼���ٶȷ�����ը����˵��
 * */
public class MainActivity extends Activity {

	//����״̬���������Ƕ����ʧ�ܾ͸�ֵΪerror
	String netWork = "error";
	
	//���ر�ǩ
	private LinearLayout ll_back;
	//ע���ǩ
	private TextView tv_signUp;
	//��¼��ť
	private Button bt_Login;
	//��ֵ����
	public String type;
	//���������ı���
	private EditText et_homeNum;
	private EditText et_userName;
	private EditText et_userPwd;
	
	//����TCPЭ���������Ķ���
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    private TextView mTextView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);         
        //��� idΪ ll_back �ı�ǩʱ������һ������
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
        //�󶨱�ǩ������
        mTextView1 = (TextView)findViewById(R.id.myTextView1);
        //������򵽽���
        et_homeNum = (EditText)findViewById(R.id.et_homeNum);
        et_userName = (EditText)findViewById(R.id.et_userName);
        et_userPwd = (EditText)findViewById(R.id.et_userPwd);
        //�����ť��֤�û������벢����������
        bt_Login = (Button) findViewById(R.id.bt_Login);
        bt_Login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mTextView1.setText("�������ӵ�������...");
				mTextView1.setBackgroundColor(Color.parseColor("#3572bd"));
				//ping�ٶȷ�������������������ϻᵼ�¿��������ֵ��ע���¼�޷�Ӧ��if��������Ϊ1==1
				if(ping()){
					if(et_homeNum.getText().toString().trim().isEmpty())
					{
						mTextView1.setText("��ͥ�Ų���Ϊ��");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(et_userName.getText().toString().trim().isEmpty())
					{
						mTextView1.setText("�û�������Ϊ��");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					if(et_userPwd.getText().toString().trim().isEmpty())
					{
						mTextView1.setText("���벻��Ϊ��");
						mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
						return;
					}
					mTextView1.setText("���ڳ��Ե�¼...");
					mTextView1.setBackgroundColor(Color.parseColor("#35bd4b"));
	            	Client();
    	        	return;
				}
				
				Toast.makeText(MainActivity.this, "�����쳣��������������", Toast.LENGTH_LONG).show();
			}
		});
        //���ע���ǩ����ע�����
        tv_signUp = (TextView)findViewById(R.id.tv_signUp);
        tv_signUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mTextView1.setText("׼��ע��");
				Intent intent =  new Intent(MainActivity.this,SignUpActivity.class);
				startActivity(intent);
			}
		});
    }
	public void Client() {
		// TODO Auto-generated method stub
		//ʵ��Activity֮�䴫ֵ
		final Intent intent_login = new Intent();
		intent_login.setClass(MainActivity.this, MainListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("userName", et_userName.getText().toString()+"_"+ et_homeNum.getText().toString());
		intent_login.putExtras(bundle);
        //����handle�������̺߳����߳�ͨ��
		final Handler mHandler = new Handler(){  
      	  
    	    @Override  
    	    public void handleMessage(Message msg) {  
    	        super.handleMessage(msg);  
    	        //����ɹ�������socket��������²���
    	        if(netWork.equals("great")){
    	        	switch (msg.what) {  
        	        //�ж��˺����벢����������
        	        case 0:
        	        	String data = (String)msg.obj;
        	        	String key = "OK";
        	            if(data.equals(key)){
        	            	mTextView1.setText("��¼�ɹ���");
        	            	mTextView1.setBackgroundColor(Color.parseColor("#35bd4b"));
        	            	startActivityForResult(intent_login, 0);
        	            }
        	            else if(data.equals("filed")){
        	            	mTextView1.setText("�û������������");
        	            	mTextView1.setBackgroundColor(Color.parseColor("#bc364f"));
        	            }
        	            break;  
        	        default:  
        	            break;  
        	        } 
    	        }
    	        //û�����ӵ�������
    	        else
    	        {
    	        	return;
    	        }
    	        	
    	         
    	    }  
    	      
    	};
    	//client ���½�һ���̣߳�����socketǶ�������ӵ�4321
		new Thread() {
			   public void run(){

				   		try{
						
						socket = new Socket("193.112.137.246",4321);
						netWork = "great";
							//193.112.137.246---172.18.126.68
						}
						catch(Exception e){
							System.out.print("socket err");
							return;
						}
						//������������������ȡ����
						try {
							dis = new DataInputStream(socket.getInputStream());
							dos = new DataOutputStream(socket.getOutputStream());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							System.out.print("DataStream create err");
						}
						
						try {
							//Thread.sleep(2000);
							String str = et_homeNum.getText().toString()+"_"+ et_userName.getText().toString()+"_"+ et_userPwd.getText().toString();
							WriteString(str);
							
						} catch (Exception e) {
							// TODO: handle exception
							System.out.print("socket clsoe err");
						}

						//��ȡ����
						
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
			if(type != null){
				Toast.makeText(this, type, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print("Read err");
		}
	}
	//�����ⷽ��
	 private static final boolean ping() {

		 String result = null;

		 try {

		 String ip = "www.baidu.com";// ���ǰٶȹ��ˣ����������Ӧ��û����~

		 Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);//ping3��


		 // ��ȡping�����ݣ��ɲ��ӡ�

		 InputStream input = p.getInputStream();

		 BufferedReader in = new BufferedReader(new InputStreamReader(input));

		 StringBuffer stringBuffer = new StringBuffer();

		 String content = "";

		 while ((content = in.readLine()) != null) {

		 stringBuffer.append(content);

		 }

		 Log.i("TTT", "result content : " + stringBuffer.toString());


		 // PING��״̬

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
